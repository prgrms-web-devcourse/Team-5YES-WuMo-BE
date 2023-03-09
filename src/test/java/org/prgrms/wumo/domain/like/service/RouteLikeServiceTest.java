package org.prgrms.wumo.domain.like.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.wumo.domain.like.model.RouteLike;
import org.prgrms.wumo.domain.like.repository.RouteLikeRepository;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.member.repository.MemberRepository;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.domain.route.repository.RouteRepository;
import org.prgrms.wumo.global.exception.custom.DuplicateException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@DisplayName("RouteLikeService 의")
class RouteLikeServiceTest {

	@Mock
	MemberRepository memberRepository;

	@Mock
	RouteRepository routeRepository;

	@Mock
	RouteLikeRepository routeLikeRepository;

	@InjectMocks
	RouteLikeService routeLikeService;

	//given
	Member member;
	Party party;
	Location location;
	Route route;

	@BeforeEach
	void setUp() {
		member = Member.builder()
				.id(1L)
				.email("5yes@gmail.com")
				.nickname("오예스오리지널")
				.password("qwe12345")
				.build();
		party = Party.builder()
				.id(1L)
				.name("오예스 워크샵")
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plusDays(1))
				.description("팀 설립 기념 워크샵")
				.coverImage("https://~.jpeg")
				.build();
		location = Location.builder()
				.id(1L)
				.category(Category.CULTURE)
				.name("프로그래머스 대륭 서초 타워")
				.description("그렙!!")
				.latitude(12.123)
				.longitude(12.123)
				.address("서울특별시 서초구 강남대로327 2층 프로그래머스(서초동, 대륭서초타워)")
				.searchAddress("서울특별시")
				.visitDate(LocalDateTime.now())
				.image("http://grepp_image")
				.spending(10000)
				.expectedCost(10000)
				.partyId(party.getId())
				.build();
		route = Route.builder()
				.id(1L)
				.locations(List.of(location))
				.party(party)
				.build();

		setAuthentication(member.getId());
	}

	@Nested
	@DisplayName("registerRouteLike 메소드는 등록 요청시")
	class RegisterRouteLike {

		@Test
		@DisplayName("사용자가 좋아요를 누르지 않은 루트라면 좋아요를 등록한다.")
		void success() {
			//mocking
			given(memberRepository.findById(member.getId()))
					.willReturn(Optional.of(member));
			given(routeRepository.findById(route.getId()))
					.willReturn(Optional.of(route));
			given(routeLikeRepository.existsByRouteIdAndMemberId(route.getId(), member.getId()))
					.willReturn(false);

			//when
			routeLikeService.registerRouteLike(route.getId());

			//then
			then(memberRepository)
					.should()
					.findById(member.getId());
			then(routeRepository)
					.should()
					.findById(route.getId());
			then(routeLikeRepository)
					.should()
					.existsByRouteIdAndMemberId(route.getId(), member.getId());
			then(routeLikeRepository)
					.should()
					.save(any(RouteLike.class));
		}

		@Test
		@DisplayName("이미 사용자가 좋아요를 누른 루트면 예외가 발생한다.")
		void failed() {
			//mocking
			given(memberRepository.findById(member.getId()))
					.willReturn(Optional.of(member));
			given(routeRepository.findById(route.getId()))
					.willReturn(Optional.of(route));
			given(routeLikeRepository.existsByRouteIdAndMemberId(route.getId(), member.getId()))
					.willReturn(true);

			//when
			//then
			Assertions.assertThrows(DuplicateException.class, () -> routeLikeService.registerRouteLike(route.getId()));

			then(memberRepository)
					.should()
					.findById(member.getId());
			then(routeRepository)
					.should()
					.findById(route.getId());
			then(routeLikeRepository)
					.should()
					.existsByRouteIdAndMemberId(route.getId(), member.getId());
		}

	}

	@Nested
	@DisplayName("deleteRouteLike 메소드는 삭제 요청시")
	class DeleteRouteLike {

		@Test
		@DisplayName("사용자가 좋아요를 눌렀던 루트라면 좋아요를 삭제한다.")
		void success() {
			//mocking
			given(memberRepository.findById(member.getId()))
					.willReturn(Optional.of(member));
			given(routeRepository.findById(route.getId()))
					.willReturn(Optional.of(route));
			given(routeLikeRepository.deleteByRouteIdAndMemberId(route.getId(), member.getId()))
					.willReturn(1);

			//when
			routeLikeService.deleteRouteLike(route.getId());

			//then
			then(memberRepository)
					.should()
					.findById(member.getId());
			then(routeRepository)
					.should()
					.findById(route.getId());
			then(routeLikeRepository)
					.should()
					.deleteByRouteIdAndMemberId(route.getId(), member.getId());
		}

		@Test
		@DisplayName("사용자가 좋아요를 누르지 않은 상태라면 예외가 발생한다.")
		void failed() {
			//mocking
			given(memberRepository.findById(member.getId()))
					.willReturn(Optional.of(member));
			given(routeRepository.findById(route.getId()))
					.willReturn(Optional.of(route));
			given(routeLikeRepository.deleteByRouteIdAndMemberId(route.getId(), member.getId()))
					.willReturn(0);

			//when
			//then
			Assertions.assertThrows(EntityNotFoundException.class, () -> routeLikeService.deleteRouteLike(route.getId()));

			then(memberRepository)
					.should()
					.findById(member.getId());
			then(routeRepository)
					.should()
					.findById(route.getId());
			then(routeLikeRepository)
					.should()
					.deleteByRouteIdAndMemberId(route.getId(), member.getId());
		}

	}

	private void setAuthentication(Long memberId) {
		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList());
		context.setAuthentication(usernamePasswordAuthenticationToken);
	}

}