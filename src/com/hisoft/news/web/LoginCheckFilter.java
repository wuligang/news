package com.hisoft.news.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "LoginCheckFilter",urlPatterns = {"/newspages/*","/upload/*"})
public class LoginCheckFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;
        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute("user") != null){
            chain.doFilter(req, resp);//可以访问资源
        }else{
            response.sendRedirect("../index.jsp");
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
