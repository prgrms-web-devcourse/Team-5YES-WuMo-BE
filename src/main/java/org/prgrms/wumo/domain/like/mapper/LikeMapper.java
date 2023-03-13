package org.prgrms.wumo.domain.like.mapper;

import org.prgrms.wumo.domain.like.model.RouteLike;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.route.model.Route;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeMapper {

	public static RouteLike toRouteLike(Route route, Member member) {
		return RouteLike.builder()
				.routeId(route.getId())
				.memberId(member.getId())
				.build();
	}

}
