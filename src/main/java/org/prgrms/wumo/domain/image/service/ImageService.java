package org.prgrms.wumo.domain.image.service;

import static org.prgrms.wumo.global.mapper.ImageMapper.toImageRegisterResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

import org.prgrms.wumo.domain.image.dto.request.ImageDeleteRequest;
import org.prgrms.wumo.domain.image.dto.request.ImageRegisterRequest;
import org.prgrms.wumo.domain.image.dto.response.ImageRegisterResponse;
import org.prgrms.wumo.global.exception.custom.ImageDeleteFailedException;
import org.prgrms.wumo.global.exception.custom.ImageUploadFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageService {

	private final String bucket;

	private final AmazonS3 amazonS3;

	public ImageService(@Value("${cloud.aws.s3.bucket:#{null}}") String bucket, AmazonS3 amazonS3) {
		this.bucket = Objects.requireNonNull(bucket, "AWS S3 Bucket 정보를 불러오지 못했습니다.");
		this.amazonS3 = amazonS3;
	}

	public ImageRegisterResponse registerImage(ImageRegisterRequest imageRegisterRequest) {
		if (!validateContentType(imageRegisterRequest.image())) {
			throw new IllegalArgumentException("이미지가 아닌 데이터를 요청했습니다.");
		}

		return toImageRegisterResponse(uploadImage(imageRegisterRequest.image()));
	}

	public void deleteImage(ImageDeleteRequest imageDeleteRequest) {
		try {
			amazonS3.deleteObject(bucket, extractImagePath(imageDeleteRequest.imageUrl()));
		} catch (AmazonServiceException e) {
			throw new ImageDeleteFailedException("버킷에서 이미지 삭제에 실패했습니다.");
		}
	}

	private boolean validateContentType(MultipartFile multipartFile) {
		return multipartFile.getContentType().startsWith("image/");
	}

	private String uploadImage(MultipartFile multipartFile) {
		ObjectMetadata objectMetadata = new ObjectMetadata();

		try {
			objectMetadata.setContentType(multipartFile.getContentType());
			objectMetadata.setContentLength(multipartFile.getInputStream().available());
			String uniqueName = getUniqueName(multipartFile.getOriginalFilename());

			amazonS3.putObject(bucket, uniqueName, multipartFile.getInputStream(), objectMetadata);
			return amazonS3.getUrl(bucket, uniqueName).toString();
		} catch (IOException e) {
			throw new ImageUploadFailedException("버킷에 이미지 업로드를 실패했습니다.");
		}
	}

	private String getUniqueName(String originName) {
		try {
			String date = LocalDate.now(ZoneId.of("Asia/Tokyo")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			return String.format("%s/%s%s", date, UUID.randomUUID(), originName.substring(originName.lastIndexOf(".")));
		} catch (Exception e) {
			throw new IllegalArgumentException("올바르지 않은 파일명 또는 형식입니다.");
		}
	}

	private String extractImagePath(String imageUrl) {
		String url = imageUrl.replaceAll("(http|https)://", "");
		return url.substring(url.indexOf("/") + 1);
	}

}
