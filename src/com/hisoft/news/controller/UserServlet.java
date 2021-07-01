package com.hisoft.news.controller;

import com.hisoft.news.javabean.NewsUser;
import com.hisoft.news.service.NewsUserService;
import com.hisoft.news.service.impl.NewsUserServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: news
 * @description:
 * @author: wlg
 * @create: 2021-06-22 09:49:10
 **/
@WebServlet("/user.do")
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        if ("dologin".equals(action)) {

//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("login")){
//                response.sendRedirect("newspages/admin.jsp");
//                return;
//            }
//        }

            String uname = request.getParameter("uname");
            String upwd = request.getParameter("upwd");
            if (uname == null || "".equals(uname)) {
                request.setAttribute("error", "用户名不能为空");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } else if (upwd == null || "".equals(upwd)) {
                request.setAttribute("error", "密码不能为空");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } else {
                NewsUserService userService = new NewsUserServiceImpl();
                NewsUser user = userService.login(uname, upwd);

                if (user == null) {
                    request.setAttribute("error", "用户名或密码错误，请重新登录");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {
//                Cookie c = new Cookie("login","login");
//                c.setMaxAge(5*60);
//                response.addCookie(c);

                    request.getSession().setMaxInactiveInterval(20 * 60);
                    request.getSession().setAttribute("user", uname);
                    request.getRequestDispatcher("newspages/admin.jsp").forward(request, response);
                }
            }
        }else if("doregister".equals(action)){
            String uname = request.getParameter("uname");
            String upwd = request.getParameter("upwd");
            String reupwd = request.getParameter("reupwd");
            if (uname == null ||"".equals(uname)){
                request.setAttribute("error","用户名不能为空");
                request.getRequestDispatcher("register.jsp").forward(request,response);
            }else if(upwd == null || "".equals(upwd)){
                request.setAttribute("error","密码不能为空");
                request.getRequestDispatcher("register.jsp").forward(request,response);
            }else if(reupwd == null || "".equals(reupwd)){
                request.setAttribute("error","重复密码不能为空");
                request.getRequestDispatcher("register.jsp").forward(request,response);
            }else if(!upwd.equals(reupwd)){
                request.setAttribute("error","两次输入的密码不一致");
                request.getRequestDispatcher("register.jsp").forward(request,response);
            }else{
                NewsUserService userService = new NewsUserServiceImpl();
                int count = userService.saveUser(uname,upwd);
                if (count > 0){
                    response.sendRedirect("index.jsp");
                }else{
                    request.setAttribute("error","注册失败，请联系管理员");
                    request.getRequestDispatcher("register.jsp").forward(request,response);
                }
            }
        }else if("logout".equals(action)){
            request.getSession().invalidate();
            response.sendRedirect("index.jsp");
        }

    }
}
