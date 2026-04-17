package com.rememberme.dunoesanchaeg.memory.mapper;

import com.rememberme.dunoesanchaeg.memory.domain.DailyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface DailyRecordMapper {

    Long findDailyRecordId(@Param("memberId") Long memberId,
                           @Param("recordDate") LocalDate recordDate);

    int insertDailyRecord(DailyRecord dailyRecord);

    int updateDailyRecord(DailyRecord dailyRecord);

    int updateRecordFinished(@Param("memberId") Long memberId,
                             @Param("recordDate") LocalDate recordDate);

    DailyRecord selectDailyRecord(@Param("memberId") Long memberId,
                                  @Param("recordDate") LocalDate recordDate);
}