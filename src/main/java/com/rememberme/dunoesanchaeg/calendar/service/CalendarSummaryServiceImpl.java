package com.rememberme.dunoesanchaeg.calendar.service;

import com.rememberme.dunoesanchaeg.calendar.dto.request.CalendarMonthRequest;
import com.rememberme.dunoesanchaeg.calendar.dto.request.CalendarSummaryRequest;
import com.rememberme.dunoesanchaeg.calendar.dto.response.CalendarMonthResponse;
import com.rememberme.dunoesanchaeg.calendar.dto.response.CalendarSummaryResponse;
import com.rememberme.dunoesanchaeg.calendar.dto.response.DailyRecordDetail;
import com.rememberme.dunoesanchaeg.calendar.dto.response.GameRecord;
import com.rememberme.dunoesanchaeg.calendar.dto.response.QuestionRecord;
import com.rememberme.dunoesanchaeg.calendar.mapper.CalendarSummaryMapper;
import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarSummaryServiceImpl implements CalendarSummaryService {

	private final CalendarSummaryMapper calendarSummaryMapper;
	private final MemberMapper memberMapper;

	@Override
	@Transactional(readOnly = true)
	public CalendarSummaryResponse getCalendarSummary(Long memberId, CalendarSummaryRequest request) {
		validateMemberExists(memberId);

		LocalDate targetDate = request.toLocalDate();

		// 1. DB 조회 (데이터가 없으면 null이 반환될 수 있음)
		GameRecord gameRecord = calendarSummaryMapper.findGameRecord(memberId, targetDate);
		QuestionRecord questionRecord = calendarSummaryMapper.findQuestionRecord(memberId, targetDate);
		DailyRecordDetail dailyRecord = calendarSummaryMapper.findDailyRecord(memberId, targetDate);

		// 2. 객체 Null 체크 및 기본값 할당 (NPE 방지)
		if (gameRecord == null) {
			gameRecord = GameRecord.builder().isPlayed(false).build();
		}
		if (questionRecord == null) {
			questionRecord = QuestionRecord.builder().isAnswered(false).build();
		}
		if (dailyRecord == null) {
			dailyRecord = DailyRecordDetail.builder().isWritten(false).build();
		}

		int progressRate = calculateProgressRate(
				gameRecord.getIsPlayed(),
				questionRecord.getIsAnswered(),
				dailyRecord.getIsWritten()
		);

		return CalendarSummaryResponse.builder()
				.targetDate(targetDate)
				.progressRate(progressRate)
				.gameRecord(gameRecord)
				.questionRecord(questionRecord)
				.dailyRecord(dailyRecord)
				.build();
	}

	private int calculateProgressRate(Boolean... statuses) {
		// 1. 방어 로직: 인자가 없거나 null인 경우 0% 반환
		if (statuses == null || statuses.length == 0) {
			return 0;
		}

		// 2. 완료된 미션 개수 카운트 (null 체크 포함)
		long completedCount = Arrays.stream(statuses)
				.filter(status -> Boolean.TRUE.equals(status))
				.count();

		// 3. 계산 (이미 위에서 length == 0을 걸러냈으므로 안전합니다)
		return (int) ((completedCount * 100) / statuses.length);
	}

	@Override
	@Transactional(readOnly = true)
	public CalendarMonthResponse getMonthlyCompletedRoutineDays(
			Long memberId,
			CalendarMonthRequest request
	) {
		validateMemberExists(memberId);

		LocalDate targetDate = request.toLocalDate();
		YearMonth targetMonth = YearMonth.from(targetDate);

		LocalDate startDate = targetMonth.atDay(1);
		LocalDate endDate = targetMonth.atEndOfMonth();

		List<LocalDate> completedDates =
				calendarSummaryMapper.findCompletedRoutineDates(memberId, startDate, endDate);

		List<String> completedDateStrings = completedDates.stream()
				.map(LocalDate::toString)
				.toList();

		return CalendarMonthResponse.builder()
				.targetMonth(targetMonth.toString())
				.completedDates(completedDateStrings)
				.build();
	}

	private void validateMemberExists(Long memberId) {
		if (memberId == null) {
			throw new BaseException(401, "로그인이 필요합니다.");
		}
		if (memberMapper.findByMemberId(memberId) == null) {
			throw new BaseException(404, "사용자 정보를 찾을 수 없습니다.");
		}
	}
}