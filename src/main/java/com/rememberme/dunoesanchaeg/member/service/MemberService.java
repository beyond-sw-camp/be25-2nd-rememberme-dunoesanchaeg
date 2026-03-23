package com.rememberme.dunoesanchaeg.member.service;

import com.rememberme.dunoesanchaeg.member.dto.request.AdditionalInfoRequest;
import com.rememberme.dunoesanchaeg.member.dto.response.AdditionalInfoResponse;



public interface MemberService {
    AdditionalInfoResponse completeProfile(
            Long memberId,
            AdditionalInfoRequest additionalInfoRequest
    );


}
