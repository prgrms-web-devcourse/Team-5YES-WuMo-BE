package org.prgrms.wumo.domain.image.service;

import static org.prgrms.wumo.domain.image.mapper.ImageMapper.toImageRegisterResponse;

import org.prgrms.wumo.domain.image.dto.request.ImageDeleteRequest;
import org.prgrms.wumo.domain.image.dto.request.ImageRegisterRequest;
import org.prgrms.wumo.domain.image.dto.response.ImageRegisterResponse;
import org.prgrms.wumo.domain.image.repository.ImageRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;

	public ImageRegisterResponse registerImage(ImageRegisterRequest imageRegisterRequest) {
		return toImageRegisterResponse(imageRepository.save(imageRegisterRequest.image()));
	}

	public void deleteImage(ImageDeleteRequest imageDeleteRequest) {
		imageRepository.delete(imageDeleteRequest.imageUrl());
	}

}
