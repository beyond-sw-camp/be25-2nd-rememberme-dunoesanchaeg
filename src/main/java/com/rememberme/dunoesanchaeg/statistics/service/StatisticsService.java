package com.rememberme.dunoesanchaeg.statistics.service;

import com.rememberme.dunoesanchaeg.statistics.dto.request.StatisticsRequest;
import com.rememberme.dunoesanchaeg.statistics.dto.response.StatisticsResponse;

public interface StatisticsService {

    StatisticsResponse getWeeklyTypeStatistics(Long memberId, StatisticsRequest request);
}