package com.kurniakue.kurse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Harun Al Rasyid
 */
@WebServlet(urlPatterns = {"/kursepo"})
public class kursepo extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet kursepo</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet kursepo at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println(request.getContextPath());
        try {
            this.request.set(request);
            this.response.set(response);

            parseParameters();
            parseBody();
            if (!process()) {
                processRequest(getRequest(), getResponse());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final ThreadLocal<HttpServletRequest> request = new ThreadLocal<>();

    public HttpServletRequest getRequest() {
        return request.get();
    }

    private final ThreadLocal<HttpServletResponse> response = new ThreadLocal<>();

    public HttpServletResponse getResponse() {
        return response.get();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private final ThreadLocal<Map<String, String>> reqParams = new ThreadLocal<Map<String, String>>() {
        {
            set(new HashMap<String, String>());
        }
    };

    public Map<String, String> getReqParams() {
        return reqParams.get();
    }

    private void parseParameters() {
        for (Enumeration<String> iterator = getRequest().getParameterNames(); iterator.hasMoreElements();) {
            String paramName = iterator.nextElement();
            getReqParams().put(paramName, getRequest().getParameter(paramName));
            System.out.println(paramName + ": " + getRequest().getParameter(paramName));
        }
    }
    
    private final ThreadLocal<JsonRequest> jsonRequest = new ThreadLocal<>();

    public JsonRequest getJsonRequest() {
        return jsonRequest.get();
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

    private boolean process() throws Exception {
        String beanName = getJsonRequest().bean;
        String className = StringUtils.capitalize(beanName);
        String fullClassName = "com.kurniakue.kurse.bean." + className;
        Class clazz = Class.forName(fullClassName);
        Object bean = clazz.newInstance();
        Method method = clazz.getMethod(getJsonRequest().method);
        method.invoke(bean);
        return false;
    }

    private void loadBean(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        jsonRequest.set(mapper.readValue(jsonString, JsonRequest.class));
    }
}
