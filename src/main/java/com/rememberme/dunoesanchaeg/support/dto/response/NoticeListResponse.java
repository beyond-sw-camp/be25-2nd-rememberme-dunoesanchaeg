package com.rememberme.dunoesanchaeg.support.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NoticeListResponse {
    Long noticeId;

    String title;

    String createdAt;

}
