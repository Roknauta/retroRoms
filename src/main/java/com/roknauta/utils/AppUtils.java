package com.roknauta.utils;

import java.util.Arrays;
import java.util.List;

public class AppUtils {

    public static List<String> stringToList(String str){
        return Arrays.stream(str.split(",")).toList();
    }

}
