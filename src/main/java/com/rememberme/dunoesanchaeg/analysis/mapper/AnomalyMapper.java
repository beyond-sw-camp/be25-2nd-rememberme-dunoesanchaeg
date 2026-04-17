package com.rememberme.dunoesanchaeg.analysis.mapper;

import com.rememberme.dunoesanchaeg.analysis.domain.AlertState;
import com.rememberme.dunoesanchaeg.analysis.domain.enums.AlertType;
import com.rememberme.dunoesanchaeg.analysis.domain.enums.MetricScope;
import com.rememberme.dunoesanchaeg.analysis.dto.AnomalyLogDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnomalyMapper {

        // memberId로 특정 미니게임 최근 N건 조회
        // is_valid = true인 것만
        List<AnomalyLogDto> findRecentGameLogs(
                        @Param("memberId") Long memberId,
                        @Param("gameType") MetricScope gameType,
                        @Param("limit") int limit);

        // memberId로 최근 질문 로그 N건 조회
        // status = COMPLETED인 것만
        List<AnomalyLogDto> findRecentQuestionLogs(
                        @Param("memberId") Long memberId,
                        @Param("limit") int limit);

        // 알림 상태 테이블 조회
        AlertState findAlertState(
                        @Param("memberId") Long memberId,
                        @Param("alertType") AlertType alertType,
                        @Param("metricScope") MetricScope metricScope);

        // 알림 상태 테이블 삽입 또는 갱신
        void upsertAlertState(AlertState alertState);

        // 알림 기록 테이블 삽입
        void insertAlertHistory(
                        @Param("memberId") Long memberId,
                        @Param("alertType") AlertType alertType,
                        @Param("metricScope") MetricScope metricScope,
                        @Param("status") String status,
                        @Param("reason") String reason);

        // 보호자 이메일 조회 (수신 동의한 경우만)
        String findGuardianEmail(@Param("memberId") Long memberId);

        // 회원 이름 조회
        String findMemberName(@Param("memberId") Long memberId);

}
