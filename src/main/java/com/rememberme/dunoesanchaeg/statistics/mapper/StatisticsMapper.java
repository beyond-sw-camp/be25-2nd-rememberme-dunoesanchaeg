package com.rememberme.dunoesanchaeg.statistics.mapper;

import com.rememberme.dunoesanchaeg.statistics.dto.response.StatisticsItemResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface StatisticsMapper {

    List<StatisticsItemResponse> findWeeklyTypeStatistics(
            @Param("memberId") Long memberId,
            @Param("targetDate") LocalDate targetDate
    );
}