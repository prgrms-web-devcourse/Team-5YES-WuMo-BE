package org.prgrms.wumo.domain.location.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.wumo.MysqlTestContainer;
import org.prgrms.wumo.domain.location.LocationTestUtils;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("LocationController 를 통해 ")
public class LocationControllerTest extends MysqlTestContainer {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	LocationRepository locationRepository;

	@Autowired
	private ObjectMapper objectMapper;

	// GIVEN
	LocationTestUtils locationTestUtils = new LocationTestUtils();

	@AfterEach
	void afterEach(){
		locationRepository.deleteAll();
	}

	@Test
	@DisplayName("후보 장소를 등록한다")
	void registerLocationTest() throws Exception {
		// Given
		LocationRegisterRequest locationRegisterRequest
				= new LocationRegisterRequest(
				"프로그래머스 강남 교육장",
				"강남역 2번출구",
				locationTestUtils.getLatitude1(),
				locationTestUtils.getLongitude1(),
				"http://programmers_gangnam_image.com",
				Category.STUDY,
				"이번에 새로 오픈한 프로그래머스 강남 교육장!! 모니터도 있고 좋은데 화장실이 좀....",
				locationTestUtils.getDayToVisit()
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
										objectMapper.writeValueAsString(locationRegisterRequest)
								)
				);

		// Then
		resultActions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andDo(print());

	}

	@Test
	@DisplayName("후보 장소 하나를 상세 조회할 수 있다.")
	void getLocationTest() throws Exception {
		// Given
		Long locationId = locationRepository.save(locationTestUtils.getLocation()).getId();

		// When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/locations/{locationId}",locationId));

		// Then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.name").value("프로그래머스 강남 교육장"))
				.andExpect(jsonPath("$.latitude").value(locationTestUtils.getLatitude1()))
				.andExpect(jsonPath("$.longitude").value(locationTestUtils.getLongitude1()))
				.andDo(print());
	}

	@Test
	@DisplayName(" 특정 모임 내의 후보 장소 전체를 조회할 수 있다.")
	void getAllLocationTest() throws Exception {
		// Given
		// List<Location> locations = List.of(location1, location2, location3);
		locationRepository.saveAll(locationTestUtils.getLocations());

		// When
		ResultActions resultActions = mockMvc.perform(
				get("/api/v1/locations")
						.param("cursorId", "0" )
						.param("pageSize", "5")
						.param("partyId", "1")
		);

		// Then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.locations").isArray())
				.andExpect(jsonPath("$.locations").isNotEmpty())
				.andExpect(jsonPath("$.locations[0].id").isNotEmpty())
				.andExpect(jsonPath("$.locations[0].latitude").value(locationTestUtils.getLatitude1()))
				.andExpect(jsonPath("$.locations[0].longitude").value(locationTestUtils.getLongitude1()))
				.andExpect(jsonPath("$.locations[0].spending").value(2000))
				.andExpect(jsonPath("$.locations[0].expectedCost").value(5000))
				.andDo(print());
	}
}
