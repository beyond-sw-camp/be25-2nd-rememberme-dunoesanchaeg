package com.rememberme.dunoesanchaeg.contents.mapper;

import com.rememberme.dunoesanchaeg.routines.domain.enums.AssignedGameType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CognitiveGameMapper {

    void insertGameResult(
            @Param("memberId") Long memberId,
            @Param("gameType") AssignedGameType gameType,
            @Param("correctCount") int correctCount,
            @Param("totalTryCount") int totalTryCount
    );
}