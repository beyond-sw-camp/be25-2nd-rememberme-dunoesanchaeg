package com.rememberme.dunoesanchaeg.calendar.mapper;

import com.rememberme.dunoesanchaeg.calendar.dto.response.DailyRecordDetail;
import com.rememberme.dunoesanchaeg.calendar.dto.response.GameRecord;
import com.rememberme.dunoesanchaeg.calendar.dto.response.QuestionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CalendarSummaryMapper {

	GameRecord findGameRecord(
			@Param("memberId") Long memberId,
			@Param("targetDate") LocalDate targetDate
	);

	QuestionRecord findQuestionRecord(
			@Param("memberId") Long memberId,
			@Param("targetDate") LocalDate targetDate
	);

	DailyRecordDetail findDailyRecord(
			@Param("memberId") Long memberId,
			@Param("targetDate") LocalDate targetDate
	);

	List<LocalDate> findCompletedRoutineDates(
			@Param("memberId") Long memberId,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate
	);
}