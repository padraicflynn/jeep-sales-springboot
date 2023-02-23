package com.promineotech.jeep.entity;

 
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
 
@Data
@Builder
 @NoArgsConstructor
 @AllArgsConstructor

public class Jeep {

  private Long modelPK;
  
  private JeepModel modelId;
  
  private String trimLevel;
  
  private int numDoors;
  
  private int wheelSize;
  
  private BigDecimal basePrice;
  
  
  //work around for changing version dependencies 
  @JsonIgnore
  public Long getModelPK() {
    return modelPK;
  }
  
}


