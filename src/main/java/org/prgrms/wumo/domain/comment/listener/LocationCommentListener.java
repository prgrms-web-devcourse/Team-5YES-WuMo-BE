package org.prgrms.wumo.domain.comment.listener;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.prgrms.wumo.domain.comment.repository.LocationCommentRepository;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.global.event.listener.JpaEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class LocationCommentListener implements JpaEventListener, PostDeleteEventListener {

	private static LocationCommentRepository locationCommentRepository;

	@Autowired
	public void setInvitationRepository(LocationCommentRepository locationCommentRepository) {
		LocationCommentListener.locationCommentRepository = locationCommentRepository;
	}

	@Async
	@Override
	public void onPostDelete(PostDeleteEvent event) {
		Object entity = event.getEntity();

		if (entity instanceof Location location) {
			event.getSession().getActionQueue().registerProcess(((success, sessionImplementor) -> {
				if (success) {
					commitOrRollback(sessionImplementor, session -> locationCommentRepository.deleteAllByLocationId(location.getId()));
				}
			}));
		}
	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		return true;
	}

}
