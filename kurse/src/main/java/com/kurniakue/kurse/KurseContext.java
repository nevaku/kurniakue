/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.kurse;

import java.util.Calendar;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Harun Al Rasyid
 */
public class KurseContext {

    private static final ThreadLocal<KurseContext> context = new ThreadLocal<>();

    public static KurseContext getContext() {
        return context.get();
    }

    public static KurseContext init(HttpServletRequest request, HttpServletResponse response) {
        context.set(new KurseContext(request, response));
        return getContext();
    }

    private KurseContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        replier = new Replier(response);
    }

    public static KurseContext close() {
        KurseContext prev = context.get();
        context.set(null);
        return prev;
    }

    public void setCurrentCalendar(Calendar calendar) {
        getSession().setAttribute(Sst.Calendar + "", calendar.clone());
    }

    public Calendar getCurrentCalendar() {
        return (Calendar) getSession().getAttribute(Sst.Calendar + "");
    }

    private final Replier replier;

    public Replier getReplier() {
        return replier;
    }

    private final HttpServletRequest request;

    public HttpServletRequest getRequest() {
        return request;
    }

    private final HttpServletResponse response;

    public HttpServletResponse getResponse() {
        return response;
    }

    public HttpSession getSession() {
        return request.getSession();
    }

    public ServletContext getApplication() {
        return request.getServletContext();
    }
}
