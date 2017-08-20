package com.kurniakue.kurse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.kurniakue.data.KurniaKueDb;
import com.kurniakue.data.TheConfig;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
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
    
    private static TheConfig config;

    @Override
    public void init() throws ServletException {
        System.out.println("Starting servlet");
        config = KurniaKueDb.getConfig();
        String configPath = "c:/harun/cfg/telebot.conf";
        System.out.println("Config path: " + configPath);
        config.load(configPath);
    }

    @Override
    public void destroy() {
        KurniaKueDb.stopAll();
        super.destroy();
        System.out.println("Destroying servlet");
    }

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
        try{
            ReqContext.init(request, response);
            processRequest(request, response);
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
        finally{
            ReqContext.close();
        }
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
            ReqContext.init(request, response);
            if (!ReqContext.get().process()) {
                processRequest(getRequest(), getResponse());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            ReqContext.close();
        }
    }

    public static ReqContext getContext() {
        return ReqContext.get();
    }

    public HttpServletRequest getRequest() {
        return getContext().getRequest();
    }

    public HttpServletResponse getResponse() {
        return getContext().getResponse();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
