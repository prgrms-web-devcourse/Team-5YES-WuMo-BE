package org.prgrms.wumo.batch.like;

import java.util.List;

import org.prgrms.wumo.domain.like.repository.RouteLikeRepository;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RouteLikeBatchComponent {

	private static final int BATCH_FREQUENCY = 600000; 	// MilliSeconds

	private static final int BATCH_SIZE = 1000;					// COUNT Record Result Size

	private final RouteLikeRepository routeLikeRepository;

	@Transactional
	@Scheduled(fixedRate = BATCH_FREQUENCY)
	public void synchronizeRouteLikeCount() {
		List<Pair<Long, Long>> resultSet;
		Long cursorId = null;
		do {
			resultSet = routeLikeRepository.countAllByRouteId(cursorId, BATCH_SIZE);

			cursorId = resultSet.isEmpty() ? -1L : resultSet.get(resultSet.size() - 1).getFirst();

			routeLikeRepository.updateLikeCount(resultSet);
		} while (!cursorId.equals(-1L));
	}

}
