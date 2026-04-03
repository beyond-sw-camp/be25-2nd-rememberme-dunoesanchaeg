package com.rememberme.dunoesanchaeg.contents.mapper;

import com.rememberme.dunoesanchaeg.routines.domain.enums.AssignedGameType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface CognitiveGameMapper {

    void insertGameResult(
            @Param("memberId") Long memberId,
            @Param("playedDate") LocalDate playedDate,
            @Param("gameType") AssignedGameType gameType,
            @Param("correctCount") Integer correctCount,
            @Param("totalTryCount") Integer totalTryCount,
            @Param("playTimeSeconds") Integer playTimeSeconds,
            @Param("isValid") Boolean isValid
    );
}