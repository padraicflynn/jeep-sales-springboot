package com.promineotech.jeep.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class DefaultJeepSalesService implements JeepSalesService {

 @Override
 
 //##################################################################
 //for step 8 comment this back in the video 
 /* */
 
  public List<Jeep> fetchJeeps(JeepModel model, String trim) {
   log.info("!!!!!!!!THIS IS A LOG LINE LOOK HERE!!!!!!!");
   log.info("The fetchedJeeps method was called with model = {} and trim = {}");
    return null;
  }

}
