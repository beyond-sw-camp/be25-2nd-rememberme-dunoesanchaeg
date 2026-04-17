package com.rememberme.dunoesanchaeg.statistics.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.member.mapper.MemberMapper;
import com.rememberme.dunoesanchaeg.statistics.domain.RecentGameScore;
import com.rememberme.dunoesanchaeg.statistics.domain.enums.GameType;
import com.rememberme.dunoesanchaeg.statistics.dto.request.StatisticsRequest;
import com.rememberme.dunoesanchaeg.statistics.dto.response.StatisticsItemResponse;
import com.rememberme.dunoesanchaeg.statistics.dto.response.StatisticsResponse;
import com.rememberme.dunoesanchaeg.statistics.mapper.StatisticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsMapper statisticsMapper;
    private final MemberMapper memberMapper;

    @Override
    @Transactional(readOnly = true)
    public StatisticsResponse getWeeklyTypeStatistics(Long memberId, StatisticsRequest request) {
        validateMemberExists(memberId);

        LocalDate targetDate = LocalDate.parse(request.getTargetDate());

        List<StatisticsItemResponse> stats = List.of(
                buildStatisticsItem(memberId, GameType.WORD_MEMORY, "기억력", targetDate),
                buildStatisticsItem(memberId, GameType.ARITHMETIC, "계산력", targetDate),
                buildStatisticsItem(memberId, GameType.DESCARTES_RPS, "판단력", targetDate)
        );

        return StatisticsResponse.builder()
                .targetDate(targetDate.toString())
                .stats(stats)
                .build();
    }

    private StatisticsItemResponse buildStatisticsItem(
            Long memberId,
            GameType gameType,
            String gameName,
            LocalDate targetDate
    ) {
        List<RecentGameScore> recentGames =
                statisticsMapper.findScoresByType(memberId, gameType, targetDate);

        if (recentGames == null || recentGames.isEmpty()) {
            return StatisticsItemResponse.builder()
                    .gameType(gameType)
                    .gameName(gameName)
                    .playCount(0)
                    .totalQuestions(0)
                    .totalCorrect(0)
                    .accuracy(null)
                    .scores(Collections.emptyList())
                    .build();
        }

        // DB에서는 최신순으로 가져오므로, 오래된 순 -> 최신 순으로 뒤집기
        Collections.reverse(recentGames);

        int playCount = recentGames.size();
        int totalQuestions = playCount * 3;
        int totalCorrect = recentGames.stream()
                .mapToInt(RecentGameScore::getCorrectCount)
                .sum();

        Integer accuracy = totalQuestions == 0
                ? null
                : (totalCorrect * 100) / totalQuestions;

        List<Integer> scores = new ArrayList<>();
        int cumulativeCorrect = 0;
        int cumulativeQuestions = 0;

        for (RecentGameScore game : recentGames) {
            cumulativeCorrect += game.getCorrectCount();
            cumulativeQuestions += 3;
            scores.add((cumulativeCorrect * 100) / cumulativeQuestions);
        }

        return StatisticsItemResponse.builder()
                .gameType(gameType)
                .gameName(gameName)
                .playCount(playCount)
                .totalQuestions(totalQuestions)
                .totalCorrect(totalCorrect)
                .accuracy(accuracy)
                .scores(scores)
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