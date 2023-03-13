package org.prgrms.wumo.global.event.listener;

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.prgrms.wumo.domain.image.listener.ImageListener;
import org.prgrms.wumo.domain.like.listener.RouteLikeListener;
import org.prgrms.wumo.domain.party.listener.InvitationListener;
import org.prgrms.wumo.domain.party.listener.PartyMemberListener;

public class EventListenerIntegrator implements Integrator {

	@Override
	public void integrate(
			Metadata metadata,
			SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry
	) {
		EventListenerRegistry eventListenerRegistry = serviceRegistry.getService(EventListenerRegistry.class);
		eventListenerRegistry.getEventListenerGroup(EventType.POST_DELETE)
				.appendListeners(
						new ImageListener(),
						new InvitationListener(),
						new PartyMemberListener(),
						new RouteLikeListener()
				);
	}

	@Override
	public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {

	}

}
