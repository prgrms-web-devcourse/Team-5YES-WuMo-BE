package org.prgrms.wumo.domain.location.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.wumo.MysqlTestContainer;
import org.prgrms.wumo.domain.location.dto.request.LocationRegisterRequest;
import org.prgrms.wumo.domain.location.model.Category;
import org.prgrms.wumo.domain.location.model.Location;
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
	float longitude1 = 127.028f;
	float latitude1 = 37.497f;
	LocalDateTime dayToVisit = LocalDateTime.now().plusDays(10);

	Location location1 = Location.builder()
			.id(1L).image("http://programmers_gangnam_image.com")
			.description("이번에 새로 오픈한 프로그래머스 강남 교육장!! 모니터도 있고 좋은데 화장실이 좀....")
			.latitude(latitude1).longitude(longitude1)
			.address("강남역 2번출구").visitDate(dayToVisit)
			.category(Category.STUDY).name("프로그래머스 강남 교육장")
			.spending(3000).expectedCost(4000).partyId(1L)
			.build();

	Location location2 = Location.builder()
			.id(2L).image("http://grepp_image")
			.description("그렙!!")
			.latitude(latitude1).longitude(longitude1)
			.address("서울특별시 서초구 강남대로327 2층 프로그래머스(서초동, 대륭서초타워)")
			.visitDate(dayToVisit)
			.category(Category.STUDY).name("프로그래머스 대륭 서초 타워")
			.spending(2000).expectedCost(5000)
			.partyId(1L)
			.build();

	Location location3 = Location.builder()
			.id(3L).image("http://gang_name_station_starbucks")
			.description("하태하태 강남역 스벅 강남 R점")
			.latitude(latitude1).longitude(longitude1)
			.address("서울 강남구 강남대로 30로").visitDate(dayToVisit)
			.category(Category.COFFEE).name("강남역 R점")
			.spending(6000).expectedCost(5500)
			.partyId(1L)
			.build();

	@Test
	@DisplayName("후보 장소를 등록한다")
	void registerLocationTest() throws Exception {
		// Given
		LocationRegisterRequest locationRegisterRequest
				= new LocationRegisterRequest(
				"프로그래머스 강남 교육장",
				"강남역 2번출구",
				latitude1,
				longitude1,
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
		locationRepository.save(location1);

		// When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/locations/{locationId}", 1));

		// Then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("프로그래머스 강남 교육장"))
				.andExpect(jsonPath("$.latitude").value(latitude1))
				.andExpect(jsonPath("$.longitude").value(longitude1))
				.andDo(print());
	}

	@Test
	@DisplayName("후보 장소 전체를 조회할 수 있다.")
	void getAllLocationTest() throws Exception {
		// Given
		List<Location> locations = List.of(location1, location2, location3);
		locationRepository.saveAll(locations);

		// When
		ResultActions resultActions = mockMvc.perform(
				get("/api/v1/locations")
						.param("cursorId", (String)null)
						.param("pageSize", "10")
						.param("partyId", "1")
		);

		// Then
		resultActions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.locations").isArray())
				.andExpect(jsonPath("$.locations").isNotEmpty())
				.andExpect(jsonPath("$.locations[0].id").value(1))
				.andExpect(jsonPath("$.locations[0].latitude").value(latitude1))
				.andExpect(jsonPath("$.locations[0].longitude").value(longitude1))
				.andExpect(jsonPath("$.locations[0].spending").value(3000))
				.andExpect(jsonPath("$.locations[0].expectedCost").value(4000))
				.andDo(print());
	}
}
