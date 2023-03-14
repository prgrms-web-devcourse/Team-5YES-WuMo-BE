package org.prgrms.wumo.domain.comment.listener;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.prgrms.wumo.domain.comment.repository.PartyRouteCommentRepository;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.party.model.PartyMember;
import org.prgrms.wumo.domain.route.model.Route;
import org.prgrms.wumo.global.event.listener.JpaEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PartyRouteCommentListener implements JpaEventListener, PostDeleteEventListener {

	private static PartyRouteCommentRepository partyRouteCommentRepository;

	@Autowired
	public void setPartyRouteCommentRepository(PartyRouteCommentRepository partyRouteCommentRepository) {
		PartyRouteCommentListener.partyRouteCommentRepository = partyRouteCommentRepository;
	}

	@Async
	@Override
	public void onPostDelete(PostDeleteEvent event) {
		Object entity = event.getEntity();

		if (entity instanceof Location location) {
			event.getSession().getActionQueue().registerProcess(((success, sessionImplementor) -> {
				if (success) {
					commitOrRollback(sessionImplementor, session ->
							partyRouteCommentRepository.deleteAllByLocationId(location.getId()));
				}
			}));
		} else if (entity instanceof Route route) {
			event.getSession().getActionQueue().registerProcess(((success, sessionImplementor) -> {
				if (success) {
					commitOrRollback(sessionImplementor, session ->
							partyRouteCommentRepository.deleteAllByRouteId(route.getId()));
				}
			}));
		} else if (entity instanceof PartyMember partyMember) {
			event.getSession().getActionQueue().registerProcess(((success, sessionImplementor) -> {
				if (success) {
					commitOrRollback(sessionImplementor, session ->
							partyRouteCommentRepository.deleteAllByPartyMemberId(partyMember.getId()));
				}
			}));
		}
	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		return true;
	}

}
