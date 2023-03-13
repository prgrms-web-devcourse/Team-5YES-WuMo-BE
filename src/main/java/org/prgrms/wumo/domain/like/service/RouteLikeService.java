package org.prgrms.wumo.domain.like.service;

import static org.prgrms.wumo.domain.like.mapper.LikeMapper.toRouteLike;
import static org.prgrms.wumo.global.exception.ExceptionMessage.ENTITY_NOT_FOUND;
import static org.prgrms.wumo.global.exception.ExceptionMessage.MEMBER;
import static org.prgrms.wumo.global.exception.ExceptionMessage.ROUTE;

import java.util.concurrent.TimeUnit;

import javax.persistence.EntityNotFoundException;

import org.prgrms.wumo.domain.like.repository.RouteLikeRepository;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.domain.route.repository.RouteRepository;
import org.prgrms.wumo.global.exception.custom.DuplicateException;
import org.prgrms.wumo.global.jwt.JwtUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteLikeService {

	private final MemberRepository memberRepository;

	private final RouteRepository routeRepository;

	private final RouteLikeRepository routeLikeRepository;

	private final RedissonClient redissonClient;

	private final PlatformTransactionManager transactionManager;

	public void registerRouteLike(Long routeId) {
		Route route = getRouteEntity(routeId);
		Member member = getMemberEntity(JwtUtil.getMemberId());

		String key = String.format("%d %d", route.getId(), member.getId());
		RLock lock = redissonClient.getLock(key);
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			try {
				// 최대 2초 동안 Lock 획득 대기 (Lock 획득 후 3초 간 점유)
				if (!lock.tryLock(2, 3, TimeUnit.SECONDS)) {
					throw new DuplicateException("이미 해당 요청에 대한 처리가 진행 중입니다.");
				}

				if (routeLikeRepository.existsByRouteIdAndMemberId(route.getId(), member.getId())) {
					throw new DuplicateException("이미 좋아요를 누른 루트입니다.");
				}

				routeLikeRepository.save(toRouteLike(route, member));
				transactionManager.commit(status);
			} catch (DuplicateException e) {
				transactionManager.rollback(status);
				throw e;
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			if (lock.isLocked() && lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	@Transactional
	public void deleteRouteLike(Long routeId) {
		Route route = getRouteEntity(routeId);
		Member member = getMemberEntity(JwtUtil.getMemberId());

		if (routeLikeRepository.deleteByRouteIdAndMemberId(route.getId(), member.getId()) == 0) {
			throw new EntityNotFoundException("좋아요를 누르지 않은 루트의 좋아요를 취소할 수 없습니다.");
		}
	}

	private Route getRouteEntity(Long routeId) {
		return routeRepository.findById(routeId)
				.orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND.name(), ROUTE.name())));
	}

	private Member getMemberEntity(Long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND.name(), MEMBER.name())));
	}

}
