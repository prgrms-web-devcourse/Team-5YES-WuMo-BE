package org.prgrms.wumo.domain.image.repository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.prgrms.wumo.global.exception.custom.ImageDeleteFailedException;
import org.prgrms.wumo.global.exception.custom.ImageUploadFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ImageRepository {

	private final String bucket;

	private final AmazonS3 amazonS3;

	private final String BUCKET_URL;

	public ImageRepository(@Value("${cloud.aws.s3.bucket:#{''}}") String bucket, AmazonS3 amazonS3) {
		if (bucket.isBlank()) {
			log.error("AWS S3 Bucket 정보를 불러오지 못했습니다.");
		}

		this.bucket = bucket;
		this.amazonS3 = amazonS3;
		this.BUCKET_URL = String.format("%s.s3.%s.amazonaws.com", bucket, amazonS3.getBucketLocation(bucket));
	}

	public String save(MultipartFile multipartFile) {
		if (!validateContentType(multipartFile)) {
			throw new IllegalArgumentException("이미지가 아닌 데이터를 요청했습니다.");
		}

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

	public void delete(String imageUrl) {
		try {
			String imageUrlWithHost = removeProtocols(imageUrl);
			validateHost(imageUrlWithHost);

			amazonS3.deleteObject(bucket, removeHost(imageUrlWithHost));
		} catch (AmazonServiceException e) {
			throw new ImageDeleteFailedException("버킷에서 이미지 삭제에 실패했습니다.");
		}
	}

	private boolean validateContentType(MultipartFile multipartFile) {
		return multipartFile.getContentType().startsWith("image/");
	}

	private String getUniqueName(String originName) {
		try {
			String date = LocalDate.now(ZoneId.of("Asia/Tokyo")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			return String.format("%s/%s%s", date, UUID.randomUUID(), originName.substring(originName.lastIndexOf(".")));
		} catch (Exception e) {
			throw new IllegalArgumentException("올바르지 않은 파일명 또는 형식입니다.");
		}
	}

	private String removeProtocols(String imageUrl) {
		return imageUrl.replaceAll("(http|https)://", "");
	}

	private void validateHost(String imageUrl) {
		try {
			if (!imageUrl.substring(0, imageUrl.indexOf("/")).equals(BUCKET_URL)) {
				log.info("버킷에 저장되지 않은 이미지를 삭제 요청했습니다. ({})", imageUrl);
			}
		} catch (IndexOutOfBoundsException e) {
			log.info("비정상적인 이미지 경로입니다. ({})", imageUrl);
		}
	}

	private String removeHost(String imageUrl) {
		return imageUrl.substring(imageUrl.indexOf("/") + 1);
	}

}
