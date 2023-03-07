package org.prgrms.wumo.batch.like;

import java.util.Comparator;
import java.util.Map;

import org.prgrms.wumo.domain.like.repository.RouteLikeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RouteLikeBatchComponent {

	private static final int BATCH_FREQUENCY = 600000;

	private static final int BATCH_SIZE = 1000;

	private final RouteLikeRepository routeLikeRepository;

	@Transactional
	@Scheduled(fixedRate = BATCH_FREQUENCY)
	public void synchronizeRouteLikeCount() {
		Map<Long, Long> routeLikes;
		Long cursorId = null;
		do {
			routeLikes = routeLikeRepository.countAllByRouteId(cursorId, BATCH_SIZE);

			cursorId = routeLikes.keySet().isEmpty() ? -1L : routeLikes.keySet().stream().max(Comparator.naturalOrder()).get();
			
			routeLikeRepository.updateLikeCount(routeLikes);
		} while (!cursorId.equals(-1L));
	}

}
