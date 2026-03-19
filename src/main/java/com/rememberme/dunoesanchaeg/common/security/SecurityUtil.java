package com.rememberme.dunoesanchaeg.common.security;


public class SecurityUtil {
    public static Long getCurrentMemberId() {
        // TODO: JWT 로직이 완성되면 여기서 토큰을 파싱해 ID를 반환
        // 지금은 개발 중이므로 무조건 1번 유저(테스트용)를 반환함
        return 1L;
    }
}

// 쿼리에 user_id가 필요하면 SecurityUtil.getCurrentMemberId()를 호출
// 예시: 루틴 서비스 로직
//public void createRoutine(RoutineRequestDto dto) {
//    유틸리티 호출
//    Long memberId = SecurityUtil.getCurrentMemberId();
//
//    // 이제 이 ID로 DB에 저장하거나 조회하면 끝!
//    routineMapper.insertRoutine(dto, currentMemberId);
//}