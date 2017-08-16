/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.kurse;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Harun Al Rasyid
 */
public class Replier {
    
    private final StringBuilder reply = new StringBuilder();
    private final HttpServletResponse response;

    public Replier(HttpServletResponse response) {
        this.response = response;
    }

    public Replier add(String text) {
        reply.append(text);
        return this;
    }

    public void send() {
        
    }
    
}
