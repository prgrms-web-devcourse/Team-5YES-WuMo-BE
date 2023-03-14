package org.prgrms.wumo.domain.like.listener;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.prgrms.wumo.domain.like.repository.RouteLikeRepository;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.global.event.listener.JpaEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RouteLikeListener implements JpaEventListener, PostDeleteEventListener {

	private static RouteLikeRepository routeLikeRepository;

	@Autowired
	public void setRouteLikeRepository(RouteLikeRepository routeLikeRepository) {
		RouteLikeListener.routeLikeRepository = routeLikeRepository;
	}

	@Async
	@Override
	public void onPostDelete(PostDeleteEvent event) {
		Object entity = event.getEntity();
		if (!(entity instanceof Route route)) {
			return;
		}

		event.getSession().getActionQueue().registerProcess(((success, sessionImplementor) -> {
			if (success) {
				commitOrRollback(sessionImplementor, session -> routeLikeRepository.deleteAllByRouteId(route.getId()));
			}
		}));

	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		return true;
	}

}
