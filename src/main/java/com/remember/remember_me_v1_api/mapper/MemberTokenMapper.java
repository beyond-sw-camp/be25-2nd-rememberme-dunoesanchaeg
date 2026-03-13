package com.remember.remember_me_v1_api.mapper;

import com.remember.remember_me_v1_api.dto.MemberTokenDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberTokenMapper {
    void insertToken(MemberTokenDto tokenDto);
}