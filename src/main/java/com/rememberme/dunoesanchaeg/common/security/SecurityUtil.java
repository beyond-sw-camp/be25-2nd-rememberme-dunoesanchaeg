package com.rememberme.dunoesanchaeg.common.security;


import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public final class SecurityUtil {
    private SecurityUtil(){
        throw  new AssertionError("인스턴스 생성 불가");
    }

    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null){
            throw new BaseException(401, "로그인이 필요합니다.");
        }

        if(!(authentication.getPrincipal() instanceof Long)){
            throw new BaseException(401, "유효하지 않은 인증 형식입니다.");
        }


        return(Long) authentication.getPrincipal();
    }
}
