package org.prgrms.wumo.domain.location.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.wumo.MysqlTestContainer;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Location Controller 를 통해 ")
public class LocationControllerTest extends MysqlTestContainer {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	private Jackson2ObjectMapperBuilder objectMapperBuilder;

	float longitude = 127.0283f;
	float latitude = 37.4975f;
	LocalDateTime dayToVisit = LocalDateTime.now().plusDays(10);

	@Test
	@DisplayName("후보 장소를 등록한다")
	void registerLocationTest() throws Exception {
		// Given
		LocationRegisterRequest locationRegisterRequest
				= new LocationRegisterRequest(
				"프로그래머스 강남 교육장",
				"강남역 2번출구",
				latitude,
				longitude,
				"http://programmers_gangnam_image.com",
				Category.STUDY,
				"이번에 새로 오픈한 프로그래머스 강남 교육장!! 모니터도 있고 좋은데 화장실이 좀....",
				dayToVisit
				, 4000,
				1L
		);

		// When
		ResultActions resultActions =
				mockMvc.perform(
						post("/api/v1/locations")
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.characterEncoding("UTF-8")
								.content(
										objectMapperBuilder.build().writeValueAsString(locationRegisterRequest)
								)
				);

		// Then
		resultActions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andDo(print());

	}
}
