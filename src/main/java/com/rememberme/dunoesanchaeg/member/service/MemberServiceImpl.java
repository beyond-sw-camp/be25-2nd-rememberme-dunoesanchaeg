package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.member.domain.Member;
import com.rememberme.dunoesanchaeg.member.dto.request.AdditionalInfoRequest;
import com.rememberme.dunoesanchaeg.member.dto.response.AdditionalInfoResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.RetrieveMemberResponse;
import com.rememberme.dunoesanchaeg.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberMapper memberMapper;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        if (memberMapper.findExistMemberPhone(additionalInfoRequest.getPhone(), memberId)){
            throw new BaseException(409, "이미 등록된 연락처입니다. 본인의 번호인지 확인해주세요");
        }

        // 보호자 동의가 true인 상태에서 보호자이메일, 보호자 전화번호 둘 다 없는 경우 예외처리
        // 보호자 동의가 false인데 값이 들어온 경우 null로 처리
        if(additionalInfoRequest.getGuardianConsent()){
            if (!StringUtils.hasText(guardianEmail)
                    && !StringUtils.hasText(guardianPhone)){
                throw new BaseException(400, "보호자 동의시 보호자 연락처 또는 이메일이 필수 입니다.");
            }

            // 보호자 이메일과 자기 자신의 이메일을 동일하게 작성하는 경우
            // (member.getEmail()을 통해 기존에 DB에 저장된 이메일과 비교)
            if(StringUtils.hasText(guardianEmail) && guardianEmail.equals(member.getEmail())){
                throw new BaseException(400, "보호자 이메일과 본인의 이메일은 동일할 수 없습니다.");
            }

            //보호자 휴대전화와 자신의 휴대전화 번호를 동일하게 작성하는 경우
            // (additionalInfoRequest.getPhone()을 통해 사용자가 현재 입력한 전화번호와 비교)
            if(StringUtils.hasText(guardianPhone) && guardianPhone.equals(additionalInfoRequest.getPhone())){
                throw new BaseException(400, "보호자 전화번호와 본인의 전화번호는 동일할 수 없습니다.");
            }

        }else{
            guardianEmail = null;
            guardianPhone = null;

        }

        member.completeProfile(additionalInfoRequest.getName(),
                                LocalDate.parse(additionalInfoRequest.getBirthDate(), DATE_FORMATTER),
                                additionalInfoRequest.getPhone(),
                                guardianEmail,
                                guardianPhone,
                                additionalInfoRequest.getGuardianConsent(),
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

    @Override
    public RetrieveMemberResponse retrieveMember(Long memberId) {
        Member member = memberMapper.findByMemberId(memberId);
        if(member == null) {
            throw new BaseException(404, "사용자 정보를 찾을 수 없습니다.");
        }

        if(!member.isProfileCompleted()){
            throw new BaseException(403, "프로필 작성이 완료되지 않았습니다. 프로필 등록이 필요합니다.");
        }


        String email = maskEmail(member.getEmail());

        return RetrieveMemberResponse.builder()
                .name(member.getName())
                .email(email)
                .phone(member.getPhone())
                .fontSize(member.getFontSize())
                .role(member.getRole())
                .isHighContrast(member.isHighContrast())
                .isProfileCompleted(member.isProfileCompleted())
                .userStatus(member.getUserStatus())
                .deletedAt(member.getDeletedAt())
                .build();

    }

    // 이메일 마스킹 로직
    private String maskEmail(String email){
        if (!StringUtils.hasText(email)) {
            throw new BaseException(403, "이메일이 존재하지 않습니다.");
        }
        String[] split = email.split("@");
        String id = split[0];
        String domain = split[1];
        if(id.length() <= 2){
           return id.charAt(0)+"***@"+ domain;
        }

        return id.substring(0, 3) + "***@"+ domain;
    }
}
