package org.prgrms.wumo.domain.image.listener;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.prgrms.wumo.domain.comment.model.Comment;
import org.prgrms.wumo.domain.image.repository.ImageRepository;
import org.prgrms.wumo.domain.location.model.Location;
import org.prgrms.wumo.domain.member.model.Member;
import org.prgrms.wumo.domain.party.model.Party;
import org.prgrms.wumo.global.event.listener.JpaEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ImageListener implements JpaEventListener, PostDeleteEventListener {

	private static ImageRepository imageRepository;

	@Autowired
	public void setInvitationRepository(ImageRepository imageRepository) {
		ImageListener.imageRepository = imageRepository;
	}

	@Async
	@Override
	public void onPostDelete(PostDeleteEvent event) {
		Object entity = event.getEntity();

		if (entity instanceof Member member) {
			event.getSession().getActionQueue().registerProcess(((success, sessionImplementor) -> {
				if (success) {
					commitOrRollback(sessionImplementor, session -> imageRepository.delete(member.getProfileImage()));
				}
			}));
		} else if (entity instanceof Party party) {
			event.getSession().getActionQueue().registerProcess(((success, sessionImplementor) -> {
				if (success) {
					commitOrRollback(sessionImplementor, session -> imageRepository.delete(party.getCoverImage()));
				}
			}));
		} else if (entity instanceof Location location) {
			event.getSession().getActionQueue().registerProcess(((success, sessionImplementor) -> {
				if (success) {
					commitOrRollback(sessionImplementor, session -> imageRepository.delete(location.getImage()));
				}
			}));
		} else if (entity instanceof Comment comment) {
			event.getSession().getActionQueue().registerProcess(((success, sessionImplementor) -> {
				if (success) {
					commitOrRollback(sessionImplementor, session -> imageRepository.delete(comment.getImage()));
				}
			}));
		}
	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		return true;
	}

}
