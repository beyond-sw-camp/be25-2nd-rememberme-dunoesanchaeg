package com.remember.remember_me_v1_api.mapper;

import com.remember.remember_me_v1_api.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    Optional<MemberDto> findByProviderAndProviderId(@Param("provider") String provider, @Param("providerId") String providerId);
    void insertMember(MemberDto member);
}
