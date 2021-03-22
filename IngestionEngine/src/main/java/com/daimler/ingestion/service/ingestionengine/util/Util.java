package com.daimler.ingestion.service.ingestionengine.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

public class Util {

    public static String sanitizeString(String val){
//      return val.replaceAll("\\W", " ");
        return val.replace("+", " ").replace("-", " ").replaceAll(" ", "");
    }

    public static Double randomNumber(){
        Random random = new Random();
        Double val = (random.nextDouble() * (101 - 85) + 85) ;
        return roundUp(val);
    }

    public static Double roundUp(Double val){
        BigDecimal bigDecimal = new BigDecimal(val);
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static Long calculateSecondsElapsed(Date startDate){
        return (new Date().getTime()-startDate.getTime())/1000;
    }
}
