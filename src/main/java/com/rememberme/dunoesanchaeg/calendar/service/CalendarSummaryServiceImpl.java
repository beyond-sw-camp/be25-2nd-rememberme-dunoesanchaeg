package com.rememberme.dunoesanchaeg.calendar.service;

import com.rememberme.dunoesanchaeg.calendar.dto.request.CalendarSummaryRequest;
import com.rememberme.dunoesanchaeg.calendar.dto.response.CalendarSummaryResponse;
import com.rememberme.dunoesanchaeg.calendar.dto.response.DailyRecordDetail;
import com.rememberme.dunoesanchaeg.calendar.dto.response.GameRecord;
import com.rememberme.dunoesanchaeg.calendar.dto.response.QuestionRecord;
import com.rememberme.dunoesanchaeg.calendar.mapper.CalendarSummaryMapper;
import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.member.domain.Member;
import com.rememberme.dunoesanchaeg.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CalendarSummaryServiceImpl implements CalendarSummaryService {

	private static final int TOTAL_DAILY_MISSIONS = 3;

	private final CalendarSummaryMapper calendarSummaryMapper;
	private final MemberMapper memberMapper;

	@Override
	@Transactional(readOnly = true)
	public CalendarSummaryResponse getCalendarSummary(Long memberId, CalendarSummaryRequest request) {
		validateMember(memberId);

		LocalDate targetDate = request.toLocalDate();

		GameRecord gameRecord = calendarSummaryMapper.findGameRecord(memberId, targetDate);
		QuestionRecord questionRecord = calendarSummaryMapper.findQuestionRecord(memberId, targetDate);
		DailyRecordDetail dailyRecord = calendarSummaryMapper.findDailyRecord(memberId, targetDate);

		int progressRate = calculateProgressRate(
				gameRecord.getIsPlayed(),
				questionRecord.getIsAnswered(),
				dailyRecord.getIsWritten()
		);

		return CalendarSummaryResponse.builder()
				.targetDate(targetDate.toString())
				.progressRate(progressRate)
				.gameRecord(gameRecord)
				.questionRecord(questionRecord)
				.dailyRecord(dailyRecord)
				.build();
	}

	private Member validateMember(Long memberId) {
		if (memberId == null) {
			throw new BaseException(401, "로그인이 필요합니다.");
		}

		Member member = memberMapper.findByMemberId(memberId);
		if (member == null) {
			throw new BaseException(404, "사용자 정보를 찾을 수 없습니다. 다시 로그인해 주세요.");
		}

		return member;
	}

	private int calculateProgressRate(Boolean isPlayed, Boolean isAnswered, Boolean isWritten) {
		int completedCount = 0;

		if (Boolean.TRUE.equals(isPlayed)) {
			completedCount++;
		}
		if (Boolean.TRUE.equals(isAnswered)) {
			completedCount++;
		}
		if (Boolean.TRUE.equals(isWritten)) {
			completedCount++;
		}

		return completedCount * 100 / TOTAL_DAILY_MISSIONS;
	}
}