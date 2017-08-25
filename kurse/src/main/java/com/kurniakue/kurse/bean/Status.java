/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.kurse.bean;

import com.kurniakue.common.Tool;
import com.kurniakue.data.DateInfo;
import com.kurniakue.kurse.Replier;
import java.util.Date;

/**
 *
 * @author Harun Al Rasyid
 */
public class Status {
    public void refresh()
    {
        System.out.println("Status.refresh");
        Replier.get().send();
    }
    
    public void hello()
    {
        Date date = new Date();
        System.out.println("Status.hello");
        
        Replier.get()
            .add("[" + Tool.formatDate(date, "HH:mm:ss") + "] Hello, how are you")
            .send();
    }
}
