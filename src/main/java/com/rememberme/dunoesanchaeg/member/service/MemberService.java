package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.member.dto.request.AdditionalInfoRequest;
import com.rememberme.dunoesanchaeg.member.dto.request.RecoveryRequest;
import com.rememberme.dunoesanchaeg.member.dto.request.UpdateMemberRequest;
import com.rememberme.dunoesanchaeg.member.dto.response.AdditionalInfoResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.RecoveryResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.SearchMemberResponse;
import com.rememberme.dunoesanchaeg.member.dto.response.UpdateMemberResponse;


public interface MemberService {
    // 프로필 완료
    AdditionalInfoResponse completeProfile(
            Long memberId,
            AdditionalInfoRequest additionalInfoRequest
    );
    
    // 회원 정보 조회
    SearchMemberResponse searchMember(Long memberId);

    // 회원 정보 수정
    UpdateMemberResponse updateMember(Long memberId, UpdateMemberRequest request);

    //회원 탈퇴
    void withdrawMember(Long memberId);

    //회원 복구
    RecoveryResponse recoveryMember(Long memberId, RecoveryRequest request, String userAgent);
}
