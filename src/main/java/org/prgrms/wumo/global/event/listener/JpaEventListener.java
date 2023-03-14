package org.prgrms.wumo.global.event.listener;

import java.util.function.Consumer;

import org.hibernate.Transaction;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

public interface JpaEventListener {

	default void commitOrRollback(
			SharedSessionContractImplementor session,
			Consumer<SharedSessionContractImplementor> task
	) {
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			task.accept(session);
			tx.commit();
		} catch (RuntimeException ex) {
			if (tx != null) {
				tx.rollback();
			}
			throw ex;
		}
	}

}
