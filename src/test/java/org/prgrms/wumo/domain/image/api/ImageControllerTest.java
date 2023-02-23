package org.prgrms.wumo.domain.image.api;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.wumo.MysqlTestContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("ImageController 를 통해 ")
class ImageControllerTest extends MysqlTestContainer {

	@Autowired
	private MockMvc mockMvc;

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
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/images")
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
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/images")
				.file(inValidFormatImage));

		//then
		resultActions
				.andExpect(status().isBadRequest())
				.andDo(print());
	}

}