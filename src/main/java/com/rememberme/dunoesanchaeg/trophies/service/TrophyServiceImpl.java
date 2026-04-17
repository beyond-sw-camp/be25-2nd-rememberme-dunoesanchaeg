package com.rememberme.dunoesanchaeg.trophies.service;

import com.rememberme.dunoesanchaeg.common.exception.BaseException;
import com.rememberme.dunoesanchaeg.member.domain.Member;
import com.rememberme.dunoesanchaeg.member.mapper.MemberMapper;
import com.rememberme.dunoesanchaeg.trophies.domain.Trophy;
import com.rememberme.dunoesanchaeg.trophies.dto.response.TrophyItemResponse;
import com.rememberme.dunoesanchaeg.trophies.dto.response.TrophyListResponse;
import com.rememberme.dunoesanchaeg.trophies.mapper.TrophyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrophyServiceImpl implements TrophyService {

	private static final int TROPHY_UNIT = 10;
	private static final int MAX_TROPHY_COUNT = 50;

	private final TrophyMapper trophyMapper;
	private final MemberMapper memberMapper;

	@Override
	@Transactional(readOnly = true)
	public TrophyListResponse getMyTrophies(Long memberId) {
		validateMember(memberId);

		List<TrophyItemResponse> trophyList = trophyMapper.findTrophiesByMemberId(memberId);

		return TrophyListResponse.builder()
				.totalCount(trophyList.size())
				.trophyList(trophyList)
				.build();
	}

	@Override
	@Transactional
	public void awardRoutineCountTrophy(Long memberId) {
		Member member = validateMember(memberId);

		int totalRoutineCount = member.getTotalRoutineCount() == null
				? 0
				: member.getTotalRoutineCount();

		if (!isAwardTarget(totalRoutineCount)) {
			return;
		}

		String trophyName = createTrophyName(totalRoutineCount);

		boolean alreadyExists = trophyMapper.existsByMemberIdAndTrophyName(memberId, trophyName);
		if (alreadyExists) {
			return;
		}

		Trophy trophy = Trophy.builder()
				.memberId(memberId)
				.trophyName(trophyName)
				.build();

		trophyMapper.insertTrophy(trophy);
	}

	private Member validateMember(Long memberId) {
		if (memberId == null) {
			throw new BaseException(401, "로그인이 필요합니다.");
		}

		Member member = memberMapper.findByMemberId(memberId);
		if (member == null) {
			throw new BaseException(404, "사용자 정보를 찾을 수 없습니다. 다시 로그인해 주세요.");
		}

		return member;
	}

	private boolean isAwardTarget(int totalRoutineCount) {
		return totalRoutineCount > 0
				&& totalRoutineCount <= MAX_TROPHY_COUNT
				&& totalRoutineCount % TROPHY_UNIT == 0;
	}

	private String createTrophyName(int totalRoutineCount) {
		return "total_routine_count_" + totalRoutineCount;
	}
}