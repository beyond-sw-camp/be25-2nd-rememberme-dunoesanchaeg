package com.rememberme.dunoesanchaeg.member.dto.request;

import com.rememberme.dunoesanchaeg.member.domain.enums.Action;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecoveryRequest {

    @NotNull(message = "수행할 작업(action)은 필수입니다.")
    private Action action;
}
