package com.welflex.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

public class HourMinuteValidator implements ConstraintValidator<HourMinute, String>{

  @Override
  public void initialize(HourMinute constraintAnnotation) {
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return false;
    }
    
    String[] data = StringUtils.split(value, ':');
    
    if (data.length == 0) {
      return false;
    }
    
    int[] dataArr = new int[2];
    
    try {
      dataArr[0] = Integer.parseInt(data[0]);
      dataArr[1] = Integer.parseInt(data[1]);
  
      if (dataArr[0] > 24 || dataArr[0] < 0) {
        return false;
      }
      
      if (dataArr[1] < 0 || dataArr[1] > 59) {
        return false;
      }
      
      return true;
      
    } catch (NumberFormatException e) {
      return false;
    }
  }

}
