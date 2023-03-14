package org.prgrms.wumo.domain.route.listener;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.domain.route.repository.RouteRepository;
import org.prgrms.wumo.global.event.listener.JpaEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RouteListener implements JpaEventListener, PostDeleteEventListener {

	private static RouteRepository routeRepository;

	@Autowired
	public void setInvitationRepository(RouteRepository routeRepository) {
		RouteListener.routeRepository = routeRepository;
	}

	@Async
	@Override
	public void onPostDelete(PostDeleteEvent event) {
		Object entity = event.getEntity();
		if (!(entity instanceof Party party)) {
			return;
		}

		event.getSession().getActionQueue().registerProcess(((success, sessionImplementor) -> {
			if (success) {
				commitOrRollback(sessionImplementor, session -> routeRepository.deleteAllByPartyId(party.getId()));
			}
		}));

	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		return true;
	}

}
