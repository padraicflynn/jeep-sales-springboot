package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;




import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
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
import com.promineotech.jeep.controller.support.FetchJeepTestSupport;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

@ActiveProfiles("test")

@Sql(scripts = { 
    "classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"},
config = @SqlConfig(encoding = "utf-8"))

class FetchJeepTest {
  
  @Autowired
  private TestRestTemplate getRestTemplate;
  
  @LocalServerPort
  private int serverPort;
  
  @Test
  void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() {
    
   // Given: we are looking for a valid model, trim and URI 
  JeepModel model = JeepModel.WRANGLER;
  
  String trim = "Sport";
  
  String uri = String.format("http://localhost:%d/jeeps?model=%s&trim=%s", serverPort, model, trim);

  //When: a connection is made to the URI
  ResponseEntity<List<Jeep>> response = 
      getRestTemplate.exchange(uri,  HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
  
  // Then: a success, 200 OK response status code is returned 
  assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  
  
  
  //########################################################################################################################
  //This is the method in step 6, comment this out when making the video and add it back when you get to that step
  // And then: actual list is returned as the expected list
  
// 
  
  List<Jeep> expected = buildExpected();
  
  //print the two jeeps we asked with the variables that we manually put in:
   System.out.println(expected);
  
  //check that the got response is what is expected 
assertThat(response.getBody()).isEqualTo(expected);
  
//method break
  }

//remove bracket above when taking this out for video

  //***comment out step 6 **************************************************************************************************************************************
   
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
   
   return list;
  }
 }
  
















  //code used in video

  /*
  @Test
  void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() {
     // Given: a valid model, trim and URI
    JeepModel model = JeepModel.WRANGLER;
    String trim = "Sport";
    String uri = String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim);
    
    System.out.println(uri);
    
    
    // When: a connection is made to the URI
    ResponseEntity<Jeep> response = getRestTemplate().getForEntity(uri, Jeep.class);
    
    // Then: a success, or (ok - 200) is returned 
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }


*/
 /*
  * //disabled for now while we run the above test
  //@Disabled
   * 
  @Autowired
  private JdbcTemplate jdbcTemplate;
  //test the database with the application-test.yaml we just made
  @Test
  void testDb() {
    int numrows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "customers");
    System.out.println("num=" + numrows);
  }
  */

  
  

