/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.common;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author harun1
 */
public class Tool {
    public static boolean isBlank(String string) {
        return string == null || "".equals(string) || "null".equals(string) ;
    }
    
    public static boolean isExists(String string) {
        return string != null && !"".equals(string);
    }

    public static <T> T neo(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    public static MemberFinder getMember(String memberName) {
        return new MemberFinder(memberName);
    }

    public static String[] parseArguments(String text) {
        String[] temp = text.split(" ");
        List<String> retlist = new ArrayList<>();
        
        String prev = "";
        for (String string : temp) {
            if (isBlank(prev) && !isBlank(string) 
                    && (!string.startsWith("\"") || string.endsWith("\""))) {
                retlist.add(string.replace("\"", ""));
            } else if (isBlank(prev) && !isBlank(string) && string.startsWith("\"")) {
                prev = string.replace("\"", "");
            } else if (!isBlank(prev) && !isBlank(string) && !string.endsWith("\"")) {
                prev += " " + string;
            } else if (!isBlank(prev) && !isBlank(string) && string.endsWith("\"")) {
                retlist.add(prev + " " + string.replace("\"", ""));
                prev = "";
            }
        }
        
        if (!isBlank(prev)) {
            retlist.add(prev);
        }
        
        return retlist.toArray(new String[0]);
    }
}
