package com.rememberme.dunoesanchaeg.member.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.rememberme.dunoesanchaeg.common.security.JwtProvider;
import com.rememberme.dunoesanchaeg.member.domain.MemberToken;
import com.rememberme.dunoesanchaeg.member.domain.enums.Action;
import com.rememberme.dunoesanchaeg.member.domain.enums.Role;
import com.rememberme.dunoesanchaeg.member.dto.request.RecoveryRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.member.domain.Member;
import com.rememberme.dunoesanchaeg.member.domain.enums.UserStatus;
import com.rememberme.dunoesanchaeg.member.dto.request.AdditionalInfoRequest;
import com.rememberme.dunoesanchaeg.member.dto.request.UpdateMemberRequest;
import com.rememberme.dunoesanchaeg.member.dto.response.AdditionalInfoResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.RecoveryResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.SearchMemberResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.UpdateMemberResponse;
import com.rememberme.dunoesanchaeg.member.mapper.MemberMapper;
import com.rememberme.dunoesanchaeg.member.mapper.MemberTokenMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberMapper memberMapper;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final MemberTokenMapper memberTokenMapper;
    private final JwtProvider jwtProvider;

    // 프로필 완료
    @Override
    public AdditionalInfoResponse completeProfile(Long memberId, AdditionalInfoRequest additionalInfoRequest) {
        int result;
        Member member = memberMapper.findByMemberId(memberId);
        String guardianEmail = additionalInfoRequest.getGuardianEmail();
        String guardianPhone = additionalInfoRequest.getGuardianPhone();

        if(member == null){
            throw new BaseException(404, "사용자 정보를 찾을 수 없습니다.");
        }

        // 전화번호 중복 확인
        checkDuplicatePhoneNumber(memberId, additionalInfoRequest.getPhone());

        // 보호자 동의가 true인 상태에서 보호자이메일, 보호자 전화번호 둘 다 없는 경우 예외처리
        // 보호자 동의가 false인데 값이 들어온 경우 null로 처리
        GuardianValidation guardianValidation = guardianValidation(
                member.getEmail(),
                additionalInfoRequest.getPhone(),
                additionalInfoRequest.getGuardianConsent(),
                guardianEmail,
                guardianPhone
        );

        LocalDate newBirthDate = birthDateValidation(additionalInfoRequest.getBirthDate());

        member.completeProfile(additionalInfoRequest.getName(),
                newBirthDate,
                additionalInfoRequest.getPhone(),
                guardianValidation.email(),
                guardianValidation.phone(),
                guardianValidation.consent(),
                additionalInfoRequest.getFontSize(),
                additionalInfoRequest.getIsHighContrast()
        );

        result = memberMapper.updateProfile(member);

        if(result != 1){
            throw new BaseException(500, "프로필 업데이트 실패");
        }

        return AdditionalInfoResponse.builder()
                .memberId(memberId)
                .isProfileCompleted(member.isProfileCompleted())
                .userStatus(member.getUserStatus())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    // 회원정보 조회
    @Override
    public SearchMemberResponse searchMember(Long memberId) {
        Member member = memberMapper.findByMemberId(memberId);
        if(member == null) {
            throw new BaseException(404, "사용자 정보를 찾을 수 없습니다.");
        }

        if(UserStatus.WITHDRAWN.equals(member.getUserStatus())){
            return SearchMemberResponse.ofWithdrawn(member);
        }

        if(!member.isProfileCompleted()){
            throw new BaseException(403, "프로필 작성이 완료되지 않았습니다. 프로필 등록이 필요합니다.");
        }

        String email = maskEmail(member.getEmail());

        return SearchMemberResponse.builder()
                .name(member.getName())
                .email(email)
                .phone(member.getPhone())
                .birthDate(member.getBirthDate() != null ? member.getBirthDate().format(DATE_FORMATTER) : null)
                .guardianConsent(member.isGuardianConsent())
                .guardianEmail(member.getGuardianEmail())
                .guardianPhone(member.getGuardianPhone())
                .fontSize(member.getFontSize())
                .role(member.getRole())
                .isHighContrast(member.isHighContrast())
                .isProfileCompleted(member.isProfileCompleted())
                .userStatus(member.getUserStatus())
                .deletedAt(member.getDeletedAt())
                .build();

    }

    // 회원 정보 수정
    @Override
    public UpdateMemberResponse updateMember(Long memberId, UpdateMemberRequest request) {
        int result;
        Member member = memberMapper.findByMemberId(memberId);

        if(member == null) {
            throw new BaseException(404, "사용자 정보를 찾을 수 없습니다.");
        }

        if(!member.isProfileCompleted()){
            throw new BaseException(403, "프로필 작성이 완료되지 않았습니다. 프로필 등록이 필요합니다.");
        }

        if(UserStatus.WITHDRAWN.equals(member.getUserStatus())){
            throw new BaseException(403, "탈퇴한 회원입니다. 30일 이내 복구 가능합니다.");
        }

        // 전화번호 중복 검증
        if (request.getPhone() != null){
            checkDuplicatePhoneNumber(memberId, request.getPhone());
        }

        // 생년월일 검증
        LocalDate validBirthDate = (request.getBirthDate() != null)
                ? birthDateValidation(request.getBirthDate())
                : member.getBirthDate();

        // 1. 내 전화번호
        String targetPhone = (request.getPhone() != null) ? request.getPhone() : member.getPhone();

        // 2. 보호자 동의 여부
        Boolean targetConsent = (request.getGuardianConsent() != null)
                ? request.getGuardianConsent()
                : member.isGuardianConsent();

        // 3. 보호자 이메일
        String targetGuardianEmail = (request.getGuardianEmail() != null)
                ? request.getGuardianEmail()
                : member.getGuardianEmail();

        // 4. 보호자 전화번호
        String targetGuardianPhone = (request.getGuardianPhone() != null)
                ? request.getGuardianPhone()
                : member.getGuardianPhone();

        // 보호자 동의 유효성 검증
        GuardianValidation validationResult = guardianValidation(
                member.getEmail(),
                targetPhone,
                targetConsent,
                targetGuardianEmail,
                targetGuardianPhone
        );

        member.patchProfile(request, validBirthDate, validationResult.email(), validationResult.phone(), validationResult.consent());

        result = memberMapper.updateProfile(member);

        if(result != 1){
            throw new BaseException(500, "프로필 업데이트 실패");
        }

        Member updateMember = memberMapper.findByMemberId(memberId);

        return UpdateMemberResponse
                .builder()
                .memberId(updateMember.getMemberId())
                .name(updateMember.getName())
                .email(updateMember.getEmail())
                .phone(updateMember.getPhone())
                .birthDate(updateMember.getBirthDate().format(DATE_FORMATTER))
                .guardianConsent(updateMember.isGuardianConsent())
                .guardianEmail(updateMember.getGuardianEmail())
                .guardianPhone(updateMember.getGuardianPhone())
                .fontSize(updateMember.getFontSize())
                .isHighContrast(updateMember.isHighContrast())
                .userStatus(updateMember.getUserStatus())
                .isProfileCompleted(updateMember.isProfileCompleted())
                .updatedAt(updateMember.getUpdatedAt())
                .build();
    }

    //계정 탈퇴
    @Override
    public void withdrawMember(Long memberId) {
        int result;
        Member member = memberMapper.findByMemberId(memberId);

        if(member == null){
            throw new BaseException(404, "사용자 정보를 찾을 수 없습니다.");
        }

        if(UserStatus.WITHDRAWN.equals(member.getUserStatus())){
            throw new BaseException(403, "이미 탈퇴 신청이 완료된 계정입니다.");
        }
        // 1. 객체 상태 변경
        member.withdraw();

        // 2. DB 반영
        result = memberMapper.withdrawMember(memberId);
        if(result != 1){
            log.error("회원 탈퇴 실패 memberId: {}", memberId);
            throw new BaseException(500, "회원 탈퇴 처리중 오류가 발생했습니다.");
        }

        memberTokenMapper.logoutAllMemberToken(memberId);
        log.info("회원 탈퇴 성공 memberId: {}", memberId);
    }

    //계정 복구
    @Override
    public RecoveryResponse recoveryMember(Long memberId, RecoveryRequest request, String userAgent) {
        int result;

        if(!Action.RESTORE.equals(request.getAction())){
            throw new BaseException(400, "잘못된 요청입니다.");
        }

        Member member = memberMapper.findByMemberId(memberId);

        if(member == null){
            throw new BaseException(500, "회원 정보를 찾을 수 없습니다.");
        }

        if(!Role.WITHDRAWN.equals(member.getRole())){
            throw new BaseException(403, "유효하지 않은 접근입니다.");
        }

        if(UserStatus.WITHDRAWN.equals(member.getUserStatus())
                && member.getDeletedAt().plusDays(30).isBefore(LocalDateTime.now())){
            throw new BaseException(403,"이미 삭제된 정보입니다.");
        }

        member.restore();
        result = memberMapper.recoveryMember(member.getMemberId());
        if(result != 1){
            throw new BaseException(500, "회원 정보 복구 실패");
        }
        log.info("회원 정보 복구 완료: {}", memberId);

        //회원 복구 완료 후 새 리프레시 토큰과 엑세스 토큰을 만들어줘야함

        String refreshToken = jwtProvider.createRefreshToken(memberId, member.getRole());
        String accessToken = jwtProvider.createAccessToken(memberId, member.getRole());
        
        MemberToken memberToken = MemberToken.builder()
                .memberId(member.getMemberId())
                .refreshToken(refreshToken)
                .userAgent(userAgent)
                .expiresAt(jwtProvider.getRefreshTokenExpire())
                .lastUsedAt(LocalDateTime.now())
                .isRevoked(false)
                .build();

        result = memberTokenMapper.upsertMemberToken(memberToken);
        if(result < 1){
            throw new BaseException(500, "토큰 갱신 실패");
        }
        
        return RecoveryResponse.builder()
                .userStatus(member.getUserStatus())
                .isProfileCompleted(member.isProfileCompleted())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }


    // 보호자 동의 유효성 검증
    private record GuardianValidation(
            String email,
            String phone,
            Boolean consent
    ){}

    private GuardianValidation guardianValidation(
            String currentUserEmail,   // DB에 저장된 현재 내 이메일
            String targetPhone,        // 이번에 저장될 내 전화번호 (보호자 전화번호와 중복 체크용)
            Boolean guardianConsent,   // 보호자 동의 여부
            String guardianEmail,      // 입력받은 보호자 이메일
            String guardianPhone       // 입력받은 보호자 전화번호
    ){
        // 보호자 동의가 true인 상태에서 보호자이메일, 보호자 전화번호 둘 다 없는 경우 예외처리
        // 보호자 동의가 false인데 값이 들어온 경우 null로 처리
        if(Boolean.TRUE.equals(guardianConsent)){
            if (!StringUtils.hasText(guardianEmail)
                    && !StringUtils.hasText(guardianPhone)){
                throw new BaseException(400, "보호자 동의시 보호자 연락처 또는 이메일이 필수 입니다.");
            }

            // 보호자 이메일과 자기 자신의 이메일을 동일하게 작성하는 경우
            // (member.getEmail()을 통해 기존에 DB에 저장된 이메일과 비교)
            if(StringUtils.hasText(guardianEmail) && guardianEmail.equals(currentUserEmail)){
                throw new BaseException(400, "보호자 이메일과 본인의 이메일은 동일할 수 없습니다.");
            }

            //보호자 휴대전화와 자신의 휴대전화 번호를 동일하게 작성하는 경우
            // (additionalInfoRequest.getPhone()을 통해 사용자가 현재 입력한 전화번호와 비교)
            if(StringUtils.hasText(guardianPhone) && guardianPhone.equals(targetPhone)){
                throw new BaseException(400, "보호자 전화번호와 본인의 전화번호는 동일할 수 없습니다.");
            }
            return new GuardianValidation(guardianEmail, guardianPhone, true);

        }else{
            return  new GuardianValidation(null, null, false);

        }
    }

    // 생년월일 검증
    private LocalDate birthDateValidation(String birthDate) {
        if(!StringUtils.hasText(birthDate)){
            throw new BaseException(400, "생년월일을 입력해주세요.");
        }
        try {
            LocalDate newBirthDate = LocalDate.parse(birthDate, DATE_FORMATTER);
            // 미래 날짜 검증
            if(newBirthDate.isAfter(LocalDate.now())){
                throw new BaseException(400, "생년월일은 미래 날짜일 수 없습니다.");
            }
            return newBirthDate;
        }catch (DateTimeParseException e) {
            throw new BaseException(400, "생년월일 형식이 올바르지 않습니다. (YYYY-MM-DD)");
        }
    }

    // 이메일 마스킹 로직
    private String maskEmail(String email){
        if (!StringUtils.hasText(email)) {
            throw new BaseException(500, "이메일이 존재하지 않습니다.");
        }
        String[] split = email.split("@");
        String id = split[0];
        String domain = split[1];
        if(id.length() <= 2){
           return id.charAt(0)+"***@"+ domain;
        }

        return id.substring(0, 3) + "***@"+ domain;
    }

    // 전화번호 중복 확인
    private void checkDuplicatePhoneNumber(Long memberId,String phone) {
        if (memberMapper.findExistMemberPhone(phone, memberId)){
            throw new BaseException(409, "이미 등록된 연락처입니다. 본인의 번호인지 확인해주세요");
        }
    }
    
}
