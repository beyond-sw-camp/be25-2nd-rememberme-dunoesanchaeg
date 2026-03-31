package com.rememberme.dunoesanchaeg.contents.mapper;

import com.rememberme.dunoesanchaeg.contents.domain.enums.GameType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CognitiveGameMapper {

    void insertGameResult(
            @Param("memberId") Long memberId,
            @Param("gameType") GameType gameType,
            @Param("correctCount") int correctCount,
            @Param("totalTryCount") int totalTryCount
    );
}