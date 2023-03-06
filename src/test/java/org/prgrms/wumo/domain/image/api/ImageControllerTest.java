package org.prgrms.wumo.domain.image.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.wumo.domain.image.dto.request.ImageDeleteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("ImageController 를 통해 ")
class ImageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Test
	@DisplayName("Content-Type 이 image/* 인 이미지를 저장할 수 있다.")
	void registerValidFormatImage() throws Exception {
		//given
		MockMultipartFile validFormatImage = new MockMultipartFile(
				"image",
				"test.png",
				MediaType.IMAGE_PNG_VALUE,
				"This is a binary data".getBytes()
		);

		//when
		ResultActions resultActions = mockMvc.perform(multipart("/api/v1/images")
				.file(validFormatImage));

		//then
		resultActions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.imageUrl").isNotEmpty())
				.andDo(print());
	}

	@Test
	@DisplayName("Content-Type 이 이미지가 아닌경우 오류가 발생한다.")
	void registerInvalidFormatImage() throws Exception {
		//given
		MockMultipartFile inValidFormatImage = new MockMultipartFile(
				"image",
				"test.pdf",
				MediaType.APPLICATION_PDF_VALUE,
				"This is a binary data".getBytes()
		);

		//when
		ResultActions resultActions = mockMvc.perform(multipart("/api/v1/images")
				.file(inValidFormatImage));

		//then
		resultActions
				.andExpect(status().isBadRequest())
				.andDo(print());
	}

	@Test
	@DisplayName("이미지 URL을 통해 버킷에서 이미지를 삭제할 수 있다.")
	void deleteImageFromValidHost() throws Exception {
		//given
		ImageDeleteRequest imageDeleteRequest
				= new ImageDeleteRequest(String.format("https://%s.s3.%s.amazonaws.com/test.png", bucket, region));

		//when
		ResultActions resultActions = mockMvc.perform(delete("/api/v1/images")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(imageDeleteRequest)));

		//then
		resultActions
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	@DisplayName("버킷의 이미지 URL이 아니면 오류가 발생한다.")
	void deleteImageFromInvalidHost() throws Exception {
		//given (유효하지 않은 이미지 경로 사용)
		ImageDeleteRequest imageDeleteRequest
				= new ImageDeleteRequest("http://localhost/test.png");

		//when
		ResultActions resultActions = mockMvc.perform(delete("/api/v1/images")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(imageDeleteRequest)));

		//then
		resultActions
				.andExpect(status().isBadRequest())
				.andDo(print());
	}

}