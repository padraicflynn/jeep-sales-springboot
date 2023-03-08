package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;



import static org.junit.jupiter.api.Assertions.*;

import org.apache.catalina.loader.ResourceEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Order;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = { 
    "classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"},
	config = @SqlConfig(encoding = "utf-8"))


class CreateOrderTest {
	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort
	private int serverPort;
	
	@Test
	void testCreateOrderReturnsSuccess201() {
		// Given: an order as JSON
		String body = createOrderBody();
		
		String uri = String.format("http://localhost:%d/orders", serverPort);
		
	//	int numRowsOrders = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders");
	//	int numRowsOptions = JdbcTestUtils.countRowsInTable(jdbcTemplate, "order_options");
		
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers);
		
		// When : order is sent
		
		ResponseEntity<Order> response = restTemplate.exchange(uri, HttpMethod.POST, bodyEntity, Order.class);
		
		// Then: 201 status returned 
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		
		// And: returned order is correct
		
		assertThat(response.getBody()).isNotNull();
		
		Order order = response.getBody();
		
		assertThat(order.getCustomer().getCustomerId()).isEqualTo("MORISON_LINA");
		assertThat(order.getModel().getModelId()).isEqualTo(JeepModel.COMPASS);
		assertThat(order.getModel().getTrimLevel()).isEqualTo("Latitude");
		assertThat(order.getModel().getNumDoors()).isEqualTo(2);
		assertThat(order.getColor().getColorId()).isEqualTo("EXT_DIAMOND_BLACK");
		assertThat(order.getEngine().getEngineId()).isEqualTo("3_0_DIESEL");
		assertThat(order.getTire().getTireId()).isEqualTo("265_MICHELIN");
		assertThat(order.getOptions()).hasSize(1);
		
	//	assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders")).isEqualTo(numRowsOrders + 1);
	//	assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "order_options")).isEqualTo(numRowsOptions + 1);
		
	//end testCreateOrderReturnsSuccess201 method	
	}

	protected String createOrderBody() {
		// @formatter:off
		return  "{\n"
				+ "   \"customer\":\"MORISON_LINA\",\n"
				+ "	  \"model\":\"COMPASS\",\n"
				+ "	  \"trim\":\"Latitude\",\n"
				+ "   \"doors\":2,\n"
				+ "	  \"color\":\"EXT_DIAMOND_BLACK\",\n"
				+ "   \"engine\":\"3_0_DIESEL\",\n"
				+ "	  \"tire\":\"265_MICHELIN\",\n"
				+ "	  \"options\":[\n"
				+ "	    \"DOOR_BESTOP_COMBO\"\n"
				+ " ]\n"
				+  "}";
		// @formatter:on
	}
	
	//class end
}








//video code
//	String uri = getBaseUriForOrders();

// When: order is sent

// video code
//	ResourceEntry<Order> = getRestTemplate().exchange(null, null, null, null)

// Then: a 201 status is returned 


// And the returned order is correct
