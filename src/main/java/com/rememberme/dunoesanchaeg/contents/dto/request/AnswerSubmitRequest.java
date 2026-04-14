package com.rememberme.dunoesanchaeg.contents.dto.request;

import com.rememberme.dunoesanchaeg.routines.domain.enums.AssignedGameType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerSubmitRequest {

    @NotNull(message = "게임 타입은 필수입니다.")
    private AssignedGameType gameType;

    @NotNull(message = "정답 개수는 필수입니다.")
    @Min(value = 0, message = "정답 개수는 0 이상이어야 합니다.")
    private Integer correctCount;

    @NotNull(message = "오답 횟수는 필수입니다.")
    @Min(value = 0, message = "오답 횟수는 0 이상이어야 합니다")
    private Integer wrongCount;

    @NotNull(message = "시간 초과 횟수가 올바르지 않습니다")
    @Min(value = 0, message = "시간 초과 횟수는 0 이상이어야 합니다.")
    private Integer timeoutCount;

    @NotNull(message = "총 시도 횟수는 필수입니다.")
    @Min(value = 0, message = "총 시도 횟수는 0 이상이어야 합니다.")
    private Integer totalTryCount;

    @NotNull(message = "플레이 시간은 필수입니다.")
    @Min(value = 0, message = "플레이 시간은 0 이상이어야 합니다.")
    private Integer totalPlayedTime;
}