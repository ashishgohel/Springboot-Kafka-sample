package com.daimler.ingestion.service.ingestionengine.util;

public class Util {

    public static String sanitizeString(String val){
//      return val.replaceAll("\\W", " ");
        return val.replace("+", " ").replace("-", " ").replaceAll(" ", "");
    }
}
