package com.rememberme.dunoesanchaeg.routines.service;

import com.rememberme.dunoesanchaeg.routines.dto.response.RoutineResponse;

public interface RoutineService {

    RoutineResponse getTodayRoutine(Long memberId);
}
