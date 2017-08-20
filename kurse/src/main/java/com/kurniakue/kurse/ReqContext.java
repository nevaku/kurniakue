/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.kurse;

import com.fasterxml.jackson.databind.ObjectMapper;
import static com.kurniakue.kurse.kursepo.getContext;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Harun Al Rasyid
 */
public class ReqContext {

    private static final ThreadLocal<ReqContext> context = new ThreadLocal<>();

    public static ReqContext get() {
        return context.get();
    }

    public static ReqContext init(HttpServletRequest request, HttpServletResponse response) throws Exception {
        context.set(new ReqContext(request, response));
        get().parseParameters();
        get().parseBody();
        Replier.get().init(response);
        return get();
    }
    final ThreadLocal<JsonRequest> jsonRequest = new ThreadLocal<>();

    private ReqContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public static ReqContext close() {
        Replier.get().close();
        ReqContext prev = context.get();
        context.set(null);
        return prev;
    }

    public void setCurrentCalendar(Calendar calendar) {
        getSession().setAttribute(Sst.Calendar + "", calendar.clone());
    }

    public Calendar getCurrentCalendar() {
        Calendar calendar = (Calendar) getSession().getAttribute(Sst.Calendar + "");
        if (calendar == null) {
            calendar = Calendar.getInstance();
            getSession().setAttribute(Sst.Calendar + "", calendar);
        }
        return calendar;
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

    public JsonRequest getJsonRequest() {
        return jsonRequest.get();
    }

    private final ThreadLocal<Map<String, String>> reqParams = new ThreadLocal<Map<String, String>>() {
        {
            set(new HashMap<String, String>());
        }
    };

    public Map<String, String> getReqParams() {
        return reqParams.get();
    }

    public void parseParameters() {
        reqParams.get().clear();
        for (Enumeration<String> iterator = getRequest().getParameterNames(); iterator.hasMoreElements();) {
            String paramName = iterator.nextElement();
            reqParams.get().put(paramName, getRequest().getParameter(paramName));
            System.out.println(paramName + ": " + getRequest().getParameter(paramName));
        }
    }

    private void parseBody() throws Exception {
        String data;
        try (InputStream is = getRequest().getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int read;
            while (true) {
                read = is.read(buffer);
                if (read > 0) {
                    baos.write(buffer, 0, read);
                }
                if (read < buffer.length) {
                    break;
                }
            }
            data = baos.toString();
            System.out.println(data);
            loadBean(data);
        }
    }

    private void loadBean(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        jsonRequest.set(mapper.readValue(jsonString, JsonRequest.class));
    }

    public boolean process() throws Exception {
        String beanName = getJsonRequest().bean;
        String className = StringUtils.capitalize(beanName);
        String fullClassName = "com.kurniakue.kurse.bean." + className;
        Class clazz;
        try {
            clazz = Class.forName(fullClassName);
            Object bean = clazz.newInstance();
            Method method = clazz.getMethod(getJsonRequest().method);
            method.invoke(bean);
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
        
        return true;
    }
}
