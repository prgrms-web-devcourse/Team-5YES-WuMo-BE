package org.prgrms.wumo.domain.like.service;

import static org.prgrms.wumo.global.mapper.LikeMapper.toRouteLike;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.like.repository.RouteLikeRepository;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.domain.route.repository.RouteRepository;
import org.prgrms.wumo.global.exception.custom.DuplicateException;
import org.prgrms.wumo.global.jwt.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteLikeService {

	private final MemberRepository memberRepository;

	private final RouteRepository routeRepository;

	private final RouteLikeRepository routeLikeRepository;

	@Transactional
	public void registerRouteLike(Long routeId) {
		Route route = getRouteEntity(routeId);
		Member member = getMemberEntity(JwtUtil.getMemberId());

		if (routeLikeRepository.existsByRouteIdAndMemberId(route.getId(), member.getId())) {
			throw new DuplicateException("이미 좋아요를 누른 루트입니다.");
		}

		routeLikeRepository.save(toRouteLike(route, member));
	}

	private Route getRouteEntity(Long routeId) {
		return routeRepository.findById(routeId)
				.orElseThrow(() -> new EntityNotFoundException("일치하는 루트가 없습니다."));
	}

	private Member getMemberEntity(Long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new EntityNotFoundException("일치하는 회원이 없습니다."));
	}

}
