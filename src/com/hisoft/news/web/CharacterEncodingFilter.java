package com.hisoft.news.web;

import javax.servlet.*;
import java.io.IOException;

public class CharacterEncodingFilter implements Filter {
    private String charset;
    public void destroy() {
        System.out.println("destroy.....................");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.setCharacterEncoding(charset);
//        resp.setContentType("text/html;charset="+charset);
        System.out.println("请求过来了。。。。。。。。。。。");
        //放行
        chain.doFilter(req, resp);
        System.out.println("响应又通过过滤器了。。。。。。。。。。");
    }

    public void init(FilterConfig config) throws ServletException {
        System.out.println("init......................");
        charset = config.getInitParameter("charset");
    }

}
