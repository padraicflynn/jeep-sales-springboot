package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;






import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.management.RuntimeErrorException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.promineotech.jeep.Constants;
import com.promineotech.jeep.controller.support.FetchJeepTestSupport;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.service.JeepSalesService;
 



class FetchJeepTest {
	
	@Nested
	@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
	@ActiveProfiles("test")
	@Sql(scripts = { 
	    "classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
	    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"},
	config = @SqlConfig(encoding = "utf-8"))
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!tests that pollute context!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
class TestsThatPolluteTheApplicationContext {
		 @Autowired
		  private TestRestTemplate getRestTemplate;
		  @LocalServerPort
		  private int serverPort;
	 @MockBean
	 private JeepSalesService jeepSalesService;
		
	 @Test
	  void testThatAnUnplannedErroresultsInA500Status() {
	    
	  JeepModel model = JeepModel.WRANGLER;
	  
	  String trim = "INVALID";
	  
	  //***********in the video he uses getBaseUri() instead of serverPort, makes s difference?***********
	  String uri = String.format("http://localhost:%d/jeeps?model=%s&trim=%s", serverPort, model, trim);
	  
	  doThrow(new RuntimeException("Oh no, Error!")).when(jeepSalesService).fetchJeeps(model, trim);

	  //When: a connection is made to the URI
	  ResponseEntity<Map<String, Object>> response = 
	      getRestTemplate.exchange(uri,  HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
	   
	  //Then: a not found 500 INTERNAL SERVER ERROR
	  assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	  
	  //And Error message is returned 
	 Map<String, Object> error = response.getBody();
	  
	 assertErrorMessageValid(error, HttpStatus.INTERNAL_SERVER_ERROR);
	 
		
	}

	 
	 
	 
	 
	 
	 

private void assertErrorMessageValid(Map<String, Object> error, HttpStatus status) {
	// @formatter:off
	 assertThat(error)
	 .containsKey("message")
	 .containsEntry("status code", status.value())
	 .containsEntry("uri", "/jeeps")
	 .containsKey("timestamp")
	 .containsEntry("reason", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	 
	 
	 // @formatter:on
}
  

	}

	
	





	
	// ############tests that do not pollute start here
	
	@Nested
	@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
	@ActiveProfiles("test")
	@Sql(scripts = { 
	    "classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
	    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"},
	config = @SqlConfig(encoding = "utf-8"))
	class TestsThatDoNotPolluteTheApplicationContext {
  @Autowired
  private TestRestTemplate getRestTemplate;
  @LocalServerPort
  private int serverPort;
  
  // &*&*&*&*&*&*&TEST ONE*&*&*&*&*&*&*&
  @Test
  void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() {
    
   // Given: we are looking for a valid model, trim and URI 
  JeepModel model = JeepModel.WRANGLER;
  
  String trim = "Sport";
  
  String uri = String.format("http://localhost:%d/jeeps?model=%s&trim=%s", serverPort, model, trim);

  //When: a connection is made to the URI
  ResponseEntity<List<Jeep>> response = 
      getRestTemplate.exchange(uri,  HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
  
  //And Then: a success, 200 OK response status code is returned 
  assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  
 
  // And then: actual list is returned as the expected list
  

 // List<Jeep> actual = response.getBody();
  List<Jeep> expected = buildExpected();
  
  //actual.forEach(jeep -> jeep.setModelPK(null));
  
  //print the two jeeps we asked with the variables that we manually put in:
   System.out.println(expected);
  
  //check that the got response is what is expected 
assertThat(response.getBody()).isEqualTo(expected);
  
//method break
  }

   //*****************************builder!**********************************************************
  //method for returning an expected list of jeep, making a new list and returning it
  private List<Jeep> buildExpected() {
 
   List<Jeep> list = new LinkedList<>();
   
   // @formatter:off
   
   list.add(Jeep.builder()
       .modelId(JeepModel.WRANGLER)
       .trimLevel("Sport")
       .numDoors(2)
       .wheelSize(17)
       .basePrice(new BigDecimal("28475.00"))
       .build());
   
   list.add(Jeep.builder()
       .modelId(JeepModel.WRANGLER)
       .trimLevel("Sport")
       .numDoors(4)
       .wheelSize(17)
       .basePrice(new BigDecimal("31975.00"))
       .build());
   
   // @formatter:on  
   Collections.sort(list);
   return list;
  }
  //**********************************************end builder*************************************************************
  
  //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^second, new test method^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  @Test
  void testThatAnErrorMessageIsReturnedWhenAnUnknownTrimIsSuppllied() {
    
  JeepModel model = JeepModel.WRANGLER;
  
  String trim = "Unknown value";
  
  //***********in the video he uses getBaseUri() instead of serverPort, makes s difference?***********
  String uri = String.format("http://localhost:%d/jeeps?model=%s&trim=%s", serverPort, model, trim);

  //When: a connection is made to the URI
  ResponseEntity<Map<String, Object>> response = 
      getRestTemplate.exchange(uri,  HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
   
  //Then: a not found 404 is returned 
  assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  
  //And Error message is returned 
 Map<String, Object> error = response.getBody();
  
 assertErrorMessageValid(error, HttpStatus.NOT_FOUND);
 
 
 // @formatter:on
//method break
  }
  
  
  //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&   end second test method   &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
  
  //**************************************************** start third test ******************************************************************
  /*
  @ParameterizedTest
  @MethodSource("com.promineotech.jeep.controller.FetchJeepTest#parametersForInvalidInput")
  void testThatAnErrorMessageIsReturnedWhenAnInvalidTrimIsSuppllied(String model, String trim, String reason) {
	    
	 
	  
	 
	  String uri = String.format("http://localhost:%d/jeeps?model=%s&trim=%s", serverPort, model, trim);

	  //When: a connection is made to the URI
	  ResponseEntity<Map<String, Object>> response = 
	      getRestTemplate.exchange(uri,  HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
	   
	  //Then: 
	  assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	  
	  //And Error 
	 Map<String, Object> error = response.getBody();
	  
	 
	 //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	assertErrorMessageValidTwo(error, HttpStatus.BAD_REQUEST);
	//method break
	  }
  //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& end third test &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

  */
  

private void assertErrorMessageValid(Map<String, Object> error, HttpStatus status) {
	// @formatter:off
	 assertThat(error)
	 .containsKey("message")
	 .containsEntry("status code", status.value())
	 .containsEntry("uri", "/jeeps")
	 .containsKey("timestamp")
	 .containsEntry("reason", HttpStatus.NOT_FOUND.getReasonPhrase());
	 
	 
	 // @formatter:on
}
  
/*
private void assertErrorMessageValidTwo(Map<String, Object> error, HttpStatus status) {
	// @formatter:off
	 assertThat(error)
	 .containsKey("message")
	 .containsEntry("status code", status.value())
	 .containsEntry("uri", "/jeeps")
	 .containsKey("timestamp")
	 .containsEntry("reason", HttpStatus.BAD_REQUEST.getReasonPhrase());
	 
	 
	 // @formatter:on
}
*/




static Stream<Arguments> parametersForInvalidInput(){
	// @formatter: off
	return Stream.of(
			arguments("WRANGLER", "$$%$%$%$%$", "Trim contains -not letters."),
	arguments("WRANGLER", "C".repeat(Constants.TRIM_MAX_LENGTH + 1), "Trim to long"),
	arguments("INVALID", "Sport", "Model is not enum value")
	
			);
	// @formatter:on
}

	}
  //package end
}

