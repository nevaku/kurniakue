/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.kurse.bean;

import com.kurniakue.kurse.Replier;

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
}
