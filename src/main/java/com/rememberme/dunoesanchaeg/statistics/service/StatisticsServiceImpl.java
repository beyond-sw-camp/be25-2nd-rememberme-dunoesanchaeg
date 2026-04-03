package com.rememberme.dunoesanchaeg.statistics.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.member.domain.Member;
import com.rememberme.dunoesanchaeg.member.mapper.MemberMapper;
import com.rememberme.dunoesanchaeg.statistics.domain.enums.GameType;
import com.rememberme.dunoesanchaeg.statistics.dto.request.StatisticsRequest;
import com.rememberme.dunoesanchaeg.statistics.dto.response.StatisticsItemResponse;
import com.rememberme.dunoesanchaeg.statistics.dto.response.StatisticsResponse;
import com.rememberme.dunoesanchaeg.statistics.mapper.StatisticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsMapper weeklyGameTypeStatisticsMapper;
    private final MemberMapper memberMapper;

    @Override
    @Transactional(readOnly = true)
    public StatisticsResponse getWeeklyTypeStatistics(Long memberId, StatisticsRequest request) {

        validateMember(memberId);

        LocalDate targetDate = parseTargetDate(request.getTargetDate());

        validateNotFuture(targetDate);

        List<StatisticsItemResponse> rawStats = weeklyGameTypeStatisticsMapper.findWeeklyTypeStatistics(memberId, targetDate);

        Map<GameType, StatisticsItemResponse> statMap = new EnumMap<>(GameType.class);

        for (StatisticsItemResponse rawStat : rawStats) {
            statMap.put(rawStat.getGameType(), rawStat);
        }

        List<StatisticsItemResponse> stats = List.of(
                buildItem(GameType.WORD_MEMORY, statMap.get(GameType.WORD_MEMORY)),
                buildItem(GameType.ARITHMETIC, statMap.get(GameType.ARITHMETIC)),
                buildItem(GameType.DESCARTES_RPS, statMap.get(GameType.DESCARTES_RPS))
        );

        return StatisticsResponse.builder()
                .targetDate(targetDate.toString())
                .stats(stats)
                .build();
    }

    private StatisticsItemResponse buildItem(GameType gameType, StatisticsItemResponse rawStat) {
        if (rawStat == null) {
            return StatisticsItemResponse.builder()
                    .gameType(gameType)
                    .gameName(gameType.getDisplayName())
                    .playCount(0)
                    .totalQuestions(0)
                    .totalCorrect(0)
                    .accuracy(null)
                    .build();
        }

        return StatisticsItemResponse.builder()
                .gameType(gameType)
                .gameName(gameType.getDisplayName())
                .playCount(defaultZero(rawStat.getPlayCount()))
                .totalQuestions(defaultZero(rawStat.getTotalQuestions()))
                .totalCorrect(defaultZero(rawStat.getTotalCorrect()))
                .accuracy(rawStat.getAccuracy())
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

    private LocalDate parseTargetDate(String targetDate) {
        try {
            return LocalDate.parse(targetDate);
        } catch (DateTimeParseException e) {
            throw new BaseException(400, "잘못된 요청입니다. 입력값을 확인해주세요.");
        }
    }

    private void validateNotFuture(LocalDate targetDate) {
        LocalDate today = LocalDate.now();
        if (targetDate.isAfter(today)) {
            throw new BaseException(
                    400,
                    "미래의 날짜는 조회할 수 없습니다. 오늘 또는 과거의 날짜를 선택해주세요."
            );
        }
    }

    private int defaultZero(Integer value) {
        return value == null ? 0 : value;
    }
}