package org.prgrms.wumo.domain.location.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "후보지에서 사용한 실제 금액 갱신 응답 정보")
public record LocationSpendingUpdateResponse(

		@Schema(description = "갱신된 실제 사용 금액")
		int spending
) {
}
