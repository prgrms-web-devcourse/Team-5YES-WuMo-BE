package org.prgrms.wumo.domain.location.listener;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.global.event.listener.JpaEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class LocationListener implements JpaEventListener, PostDeleteEventListener {

	private static LocationRepository locationRepository;

	@Autowired
	public void setInvitationRepository(LocationRepository locationRepository) {
		LocationListener.locationRepository = locationRepository;
	}

	@Async
	@Override
	public void onPostDelete(PostDeleteEvent event) {
		Object entity = event.getEntity();

		if (entity instanceof Party party) {
			event.getSession().getActionQueue().registerProcess(((success, sessionImplementor) -> {
				if (success) {
					commitOrRollback(sessionImplementor, session -> locationRepository.deleteAllByPartyId(party.getId()));
				}
			}));
		}
	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		return true;
	}

}
