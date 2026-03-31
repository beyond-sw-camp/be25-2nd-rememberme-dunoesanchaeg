package com.rememberme.dunoesanchaeg.trophies.mapper;

import com.rememberme.dunoesanchaeg.trophies.domain.Trophy;
import com.rememberme.dunoesanchaeg.trophies.dto.response.TrophyItemResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrophyMapper {

	boolean existsByMemberIdAndTrophyName(Long memberId, String trophyName);

	int insertTrophy(Trophy trophy);

	List<TrophyItemResponse> findTrophiesByMemberId(Long memberId);
}