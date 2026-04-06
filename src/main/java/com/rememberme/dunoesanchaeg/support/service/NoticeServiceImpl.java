package com.rememberme.dunoesanchaeg.support.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.support.dto.response.NoticeDetailResponse;
import com.rememberme.dunoesanchaeg.support.dto.response.NoticeListResponse;
import com.rememberme.dunoesanchaeg.support.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final NoticeMapper noticeMapper;

    @Override
    public List<NoticeListResponse> searchNotice(int page, int size) {
        int offset = page * size;
        return noticeMapper.searchNotice(size, offset);
    }

    @Override
    public NoticeDetailResponse searchNoticeDetail(Long noticeId) {
        NoticeDetailResponse noticeDetail = noticeMapper.searchNoticeDetail(noticeId);
        if (noticeDetail == null) {
            throw  new BaseException(404, "공지사항을 찾을 수 없습니다.");
        }
        return noticeDetail;
    }
}
