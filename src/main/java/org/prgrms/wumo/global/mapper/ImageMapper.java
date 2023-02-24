package org.prgrms.wumo.global.mapper;

import org.prgrms.wumo.domain.image.dto.response.ImageRegisterResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageMapper {

	public static ImageRegisterResponse toImageRegisterResponse(String imageUrl) {
		return new ImageRegisterResponse(imageUrl);
	}

}
