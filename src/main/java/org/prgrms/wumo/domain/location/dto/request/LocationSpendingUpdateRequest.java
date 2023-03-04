package org.prgrms.wumo.domain.location.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

@Schema(name = "후보지에서 실제로 사용한 금액 갱신 요청 정보")
public record LocationSpendingUpdateRequest(

		@NotNull
		@Schema(description = "사용 금액을 갱신할 후보지 식별자", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		Long locationId,

		@NotNull
		@Schema(description = "갱신할 금액", example = "16000", requiredMode = Schema.RequiredMode.REQUIRED)
		int spending
) {
}
