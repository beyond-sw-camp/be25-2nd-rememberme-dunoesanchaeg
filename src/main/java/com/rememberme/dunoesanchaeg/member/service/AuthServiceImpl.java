package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.common.security.JwtProvider;
import com.rememberme.dunoesanchaeg.member.domain.Member;
import com.rememberme.dunoesanchaeg.member.domain.MemberToken;
import com.rememberme.dunoesanchaeg.member.dto.response.KakaoLoginResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.TokenReissueResponse;
import com.rememberme.dunoesanchaeg.member.dto.target.KakaoUserInfo;
import com.rememberme.dunoesanchaeg.member.mapper.MemberMapper;
import com.rememberme.dunoesanchaeg.member.mapper.MemberTokenMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.rememberme.dunoesanchaeg.member.domain.enums.FontSize.SMALL;
import static com.rememberme.dunoesanchaeg.member.domain.enums.Role.USER;
import static com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus.ACTIVE;
import static com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus.WITHDRAWN;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final MemberMapper memberMapper;
    private final MemberTokenMapper memberTokenMapper;
    private final JwtProvider jwtProvider;
    private final TokenManager tokenManager;
    private final KakaoClient kakaoClient;

    @Override
    public KakaoLoginResponse kakaoAuth(String code, String userAgent) {
        int result;

        // 카카오 로그인 후 받아오는 code에서 엑세스 토큰 추출
        String kakaoAccessToken = kakaoClient.getKakaoAccessToken(code);
        KakaoUserInfo kakaoUserInfo = kakaoClient.getKakaoUserInfo(kakaoAccessToken);
        Long kakaoId = kakaoUserInfo.getKakaoId();
        String email = kakaoUserInfo.getEmail();

        Member member = memberMapper.findByKakaoId(kakaoId);
        // 신규유저면 insertMember 아니면 기존유저
        // 기존 유저에서 getUserStatus 가 WITHDRAWN이면 회원 복구로직 OR 스케줄러로 삭제
        // 기존유저이면서 ACTIVE이면 updateLastLoginAt 갱신
        if (member == null) {
            if(email != null && memberMapper.findByEmail(email) != null){
                throw new BaseException(400, "이미 다른 계정으로 로그인 되었습니다. 해당 계정으로 로그인해주세요");
            }
            Member newMember = Member.builder()
                    .kakaoId(kakaoId)
                    .email(email)
                    .isHighContrast(false)
                    .fontSize(SMALL)
                    .isProfileCompleted(false)
                    .role(USER)
                    .userStatus(ACTIVE)
                    .build();
            result = memberMapper.insertMember(newMember);
            if (result != 1) {
                throw new BaseException(500, "유저 저장 실패");
            }
            member = newMember;

        } else {
            // kakaoId는 같은데 email이 다른 경우
            if(email != null && !email.equals(member.getEmail())){
                // 입력받은 이메일을 다른 사람이 사용하고 있는경우
                if(memberMapper.findByEmail(email) != null){
                    log.warn("이메일 변경 시도중 중복 발생 memberId: {} conflictEmail: {}",member.getMemberId(), email);
                    throw new BaseException(400, "이미 다른 계정에서 사용중인 이메일입니다.");
                }
                result = memberMapper.updateEmail(member.getMemberId(), email);
                if(result != 1){
                    throw new BaseException(500, "이메일 갱신 실패");
                }
                member.updateEmail(email);
            }

        }

        // 신규회원이든 기존 회원이든 로그인하면 마지막 로그인 시간 갱신
        result = memberMapper.updateLastLoginAt(member.getMemberId());
        if (result != 1) {
            throw new BaseException(500, "마지막 로그인 갱신 실패");
        }

        // JWT 토큰 로직 구현하면 변경해야함-------------------
        // 새 토큰 발행: 로그인이 성공했으므로 새로운 AccessToken과 RefreshToken을 생성
        String accessToken = jwtProvider.createAccessToken(member.getMemberId(),member.getRole());
        String refreshToken = jwtProvider.createRefreshToken(member.getMemberId(),member.getRole());
        LocalDateTime expireDay = jwtProvider.getRefreshTokenExpire();
        //------------------------------------------------

        // 기존 세션 확인: findByMemberIdAndUserAgent로 "이 유저가 이 기기로 들어온 적이 있는지" 확인
        // memberToken == null 이면 새 리프레시토큰과 나머지 설정
        // not null이면 update토큰
        MemberToken memberToken = memberTokenMapper.findByMemberIdAndUserAgent(member.getMemberId(), userAgent);
        if (memberToken == null) {
            MemberToken newMemberToken = MemberToken.builder()
                    .memberId(member.getMemberId())
                    .refreshToken(refreshToken)
                    .userAgent(userAgent)
                    .expiresAt(expireDay)
                    .build();
            result = memberTokenMapper.upsertMemberToken(newMemberToken);
            if (result < 1) {
                throw new BaseException(500, "유저 토큰 저장 실패");
            }

        } else {
//            memberToken.setRefreshToken(refreshToken);
//            memberToken.setExpiresAt(expireDay);
//            memberToken.setRevoked(false);
//
//            result = memberTokenMapper.updateMemberToken(memberToken);
//            if (result != 1) {
//                throw new BaseException(500, "유저 토큰 수정 실패");
//            }

            // 기존 유저의 경우 reactivateToken으로 확인
            tokenManager.reactivateToken(memberToken, refreshToken, expireDay);
        }

        return KakaoLoginResponse.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .isProfileCompleted(member.isProfileCompleted())
                .userStatus(member.getUserStatus())
                .fontSize(member.getFontSize())
                .isHighContrast(member.isHighContrast())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenReissueResponse reissue(String refreshToken, String userAgent) {
        int result;

        // 리프레시 토큰 자체가 안들어온 경우
        if(refreshToken == null){
            throw new BaseException(401, "세션이 만료되었거나 유효하지 않은 접근입니다.");
        }

        MemberToken token = memberTokenMapper.findByRefreshToken(refreshToken);

        // 토큰이 DB없는 경우
        if(token == null){
            throw new BaseException(401,"유효하지 않은 접근입니다. 다시 로그인해주세요.");
        }

        // 토큰이 폐기된 경우
        if (token.isRevoked()) {
            throw new BaseException(403,"이미 만료된 세션입니다. 다시 로그인해주세요.");
        }

        // 같은 토큰이 다른 userAgent로 로그인하는 경우 (토큰 탈취)
        if(!(token.getUserAgent().equals(userAgent))){
            // @Transactional로 인해 롤백되어버림
            //token.setRevoked(true);
            //result = memberTokenMapper.updateMemberToken(token);

            //if(result != 1){
            //    log.error("보안조치 필요 토큰 수정 실패 - memberId : {}", token.getMemberId());
            //    throw new BaseException(500,"토큰 수정 실패");
            //}
            tokenManager.revokeToken(token);

            log.warn("토큰이 탈취되었습니다. {}", token.getMemberId());
            throw new BaseException(403, "토큰이 탈취되었습니다.");
        }

        // 토큰이 만료된 경우 (현재 시간보다 token이 과거인 경우)
        if(token.getExpiresAt().isBefore(LocalDateTime.now())){
            tokenManager.revokeToken(token);
            log.warn("토큰이 만료되었습니다. {}", token.getMemberId());
            throw new BaseException(401, "토큰이 만료되었습니다. 다시 로그인해주세요.");
        }

        // 사용자가 WITHDRAWN인 경우
        Member member = memberMapper.findByMemberId(token.getMemberId());
        if (member == null) {
            throw new BaseException(404, "사용자 정보를 찾을 수 없습니다.");
        }

        if(member.getUserStatus() == WITHDRAWN){
            throw new BaseException(400, "탈퇴한 회원입니다. 30일 이내 복구 가능합니다.");
        }

        // 정상발급
        // 새로운 토큰 세트 생성
        String newAccessToken = jwtProvider.createAccessToken(member.getMemberId(), member.getRole());
        String newRefreshToken = jwtProvider.createRefreshToken(member.getMemberId(), member.getRole());

        //토큰객체에 넣어야함
        token = token.toBuilder()
                .refreshToken(newRefreshToken)
                .expiresAt(jwtProvider.getRefreshTokenExpire()).
                build();

        result = memberTokenMapper.upsertMemberToken(token);
        if (result < 1){
            throw new BaseException(500,"토큰 수정 실패");
        }


        return TokenReissueResponse
                .builder()
                .refreshToken(newRefreshToken)
                .accessToken(newAccessToken)
                .name(member.getName())
                .userStatus(member.getUserStatus())
                .isProfileCompleted(member.isProfileCompleted())
                .build();
    }

    @Override
    public int logout(Long memberId, String userAgent) {
        int result;
        Member member = memberMapper.findByMemberId(memberId);
        if (member == null) {
            throw new BaseException(404, "존재하지 않는 회원입니다.");
        }

        result = tokenManager.logoutTransactional(memberId, userAgent);

        kakaoClient.logout(member.getKakaoId());

        return result;
    }

    @Override
    public int logoutAll(Long memberId) {
        int result;
        Member member = memberMapper.findByMemberId(memberId);
        if (member == null) {
            throw new BaseException(404, "존재하지 않는 회원입니다.");
        }

        kakaoClient.logout(member.getKakaoId());

        log.info("모든 유저 기기에서 로그아웃 - memberId: {}", memberId);
        result = tokenManager.logoutAllTransactional(memberId);

        return result;
    }
}
