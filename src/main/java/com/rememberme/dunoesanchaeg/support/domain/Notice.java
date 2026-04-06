package com.rememberme.dunoesanchaeg.support.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
    private Long noticeId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
