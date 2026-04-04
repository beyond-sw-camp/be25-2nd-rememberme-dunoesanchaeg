package com.rememberme.dunoesanchaeg.support.mapper;

import com.rememberme.dunoesanchaeg.support.dto.response.NoticeDetailResponse;
import com.rememberme.dunoesanchaeg.support.dto.response.NoticeListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {
    List<NoticeListResponse> searchNotice(@Param("size") int size, @Param("offset") int offset);

    NoticeDetailResponse searchNoticeDetail(@Param("noticeId") Long noticeId);
}
