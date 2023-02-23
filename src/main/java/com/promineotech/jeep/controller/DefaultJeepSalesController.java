package com.promineotech.jeep.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.service.JeepSalesService;
import lombok.extern.slf4j.Slf4j;

//week 14 instructions 
//http://localhost:8080/swagger-ui.html
//http://localhost:8080/jeeps?model=WRANGLER&trim=Sport
//put in browser and show in log that it works
// go to web api, try it out, get a model name and sport for trim and execute, show log line, show curl and request url, show 200 code response
// go to junit test and run test and show green bar to show it works 
// the above is notes for week 14 homework video to show case work


@RestController
@Slf4j

public class DefaultJeepSalesController implements JeepSalesController {

 @Autowired
 private JeepSalesService jeepSalesService;
 
  
  
  @Override
  public List<Jeep> fetchJeeps(JeepModel model, String trim) {
    
    //use log.debug to see at the debug level, spring logs at the info level 
    
   log.info("model={}, trim={}", model, trim);
    return jeepSalesService.fetchJeeps(model, trim);
  }

}
