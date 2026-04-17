package com.rememberme.dunoesanchaeg.support.dto.response;


import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class NoticeDetailResponse {
    Long noticeId;

    String title;

    String content;

    LocalDateTime createdAt;


}
