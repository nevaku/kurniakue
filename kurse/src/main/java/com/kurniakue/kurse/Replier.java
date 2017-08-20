/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.kurse;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Harun Al Rasyid
 */
public class Replier {

    private static final Replier instance = new Replier();
    private final StringBuilder reply = new StringBuilder();
    private final ThreadLocal<HttpServletResponse> response = new ThreadLocal<>();

    public static Replier get() {
        return instance;
    }

    public void init(HttpServletResponse response) {
        this.response.set(response);
    }

    public void close() {
        this.response.set(null);
    }

    public HttpServletResponse getResponse() {
        return response.get();
    }

    public Replier add(String text) {
        reply.append(text);
        return this;
    }

    public void send() {
        getResponse().setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = getResponse().getWriter()) {
            if (reply.length() == 0) {
                out.println("NA");
            } else {
                out.println(reply.toString());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            reply.delete(0, reply.length());
        }
    }
}
