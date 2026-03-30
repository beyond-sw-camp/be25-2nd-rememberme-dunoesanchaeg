package com.rememberme.dunoesanchaeg.trophies.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.member.domain.Member;
import com.rememberme.dunoesanchaeg.member.mapper.MemberMapper;
import com.rememberme.dunoesanchaeg.trophies.domain.Trophy;
import com.rememberme.dunoesanchaeg.trophies.domain.TrophyPolicy;
import com.rememberme.dunoesanchaeg.trophies.dto.response.TrophyItemResponse;
import com.rememberme.dunoesanchaeg.trophies.dto.response.TrophyListResponse;
import com.rememberme.dunoesanchaeg.trophies.mapper.TrophyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrophyServiceImpl implements TrophyService {

	private final TrophyMapper trophyMapper;
	private final MemberMapper memberMapper;

	@Override
	@Transactional(readOnly = true)
	public TrophyListResponse getMyTrophies(Long memberId) {


		if (memberId == null) {
			throw new BaseException(401, "로그인이 필요합니다.");
		}

		Member member = memberMapper.findByMemberId(memberId);
		if (member == null) {
			throw new BaseException(404, "사용자 정보를 찾을 수 없습니다. 다시 로그인해 주세요.");
		}


		List<TrophyItemResponse> trophyList = trophyMapper.findTrophiesByMemberId(memberId);

		return TrophyListResponse.builder()
				.totalCount(trophyList.size())
				.trophyList(trophyList)
				.build();
	}

	@Override
	@Transactional
	public void awardRoutineCountTrophy(Long memberId) {
		if (memberId == null) {
			throw new BaseException(401, "로그인이 필요합니다.");
		}

		Member member = memberMapper.findByMemberId(memberId);

		if (member == null) {
			throw new BaseException(404, "사용자 정보를 찾을 수 없습니다. 다시 로그인해 주세요.");
		}

		int totalRoutineCount = member.getTotalRoutineCount() == null
				? 0
				: member.getTotalRoutineCount();

		if (totalRoutineCount <= 0 || totalRoutineCount % 10 != 0) {
			return;
		}

		Optional<TrophyPolicy> policyOptional = TrophyPolicy.findByTargetCount(totalRoutineCount);
		if (policyOptional.isEmpty()) {
			return;
		}

		TrophyPolicy policy = policyOptional.get();

		boolean alreadyExists = trophyMapper.existsByMemberIdAndTrophyType(
				memberId,
				policy.getTrophyType()
		);

		if (alreadyExists) {
			return;
		}

		Trophy trophy = Trophy.builder()
				.memberId(memberId)
				.trophyType(policy.getTrophyType())
				.trophyName(policy.getTrophyName())
				.build();

		trophyMapper.insertTrophy(trophy);
	}
}