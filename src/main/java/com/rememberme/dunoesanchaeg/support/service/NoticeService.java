package com.rememberme.dunoesanchaeg.support.service;

import com.rememberme.dunoesanchaeg.support.dto.response.NoticeDetailResponse;
import com.rememberme.dunoesanchaeg.support.dto.response.NoticeListResponse;

import java.util.List;

public interface NoticeService {
    List<NoticeListResponse> searchNotice(int page, int size);

    NoticeDetailResponse searchNoticeDetail(Long noticeId);
}
