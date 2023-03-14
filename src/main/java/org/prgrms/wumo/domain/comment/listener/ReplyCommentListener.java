package org.prgrms.wumo.domain.comment.listener;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.prgrms.wumo.domain.comment.model.Comment;
import org.prgrms.wumo.domain.comment.repository.ReplyCommentRepository;
import org.prgrms.wumo.global.event.listener.JpaEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ReplyCommentListener implements JpaEventListener, PostDeleteEventListener {

	private static ReplyCommentRepository replyCommentRepository;

	@Autowired
	public void setInvitationRepository(ReplyCommentRepository replyCommentRepository) {
		ReplyCommentListener.replyCommentRepository = replyCommentRepository;
	}

	@Async
	@Override
	public void onPostDelete(PostDeleteEvent event) {
		Object entity = event.getEntity();

		if (entity instanceof Comment comment) {
			event.getSession().getActionQueue().registerProcess(((success, sessionImplementor) -> {
				if (success) {
					commitOrRollback(sessionImplementor, session -> replyCommentRepository.deleteAllByCommentId(comment.getId()));
				}
			}));
		}
	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		return true;
	}

}
