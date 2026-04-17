package com.rememberme.dunoesanchaeg.statistics.mapper;

import com.rememberme.dunoesanchaeg.statistics.domain.RecentGameScore;
import com.rememberme.dunoesanchaeg.statistics.domain.enums.GameType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface StatisticsMapper {

    List<RecentGameScore> findScoresByType(
            @Param("memberId") Long memberId,
            @Param("gameType") GameType gameType,
            @Param("targetDate") LocalDate targetDate
    );
}