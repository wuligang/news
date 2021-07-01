package com.hisoft.news.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hisoft.news.javabean.News;
import com.hisoft.news.service.NewsService;
import com.hisoft.news.service.TopicService;
import com.hisoft.news.service.impl.NewsServiceImpl;
import com.hisoft.news.service.impl.TopicServiceImpl;
import com.hisoft.news.util.Page;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
@WebServlet("/news.do")
public class NewsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        NewsService newsService = new NewsServiceImpl();
        TopicService topicService = new TopicServiceImpl();
        if ("index".equals(action)) {//进入首页
            String tidStr = request.getParameter("tid");
            Integer tid = tidStr == null ? null : Integer.parseInt(request.getParameter("tid"));

//            List<Topic> topics = topicService.findAllTopics();
            List<News> newsList = newsService.findNewsByTid(tid);

//            Map<String, List> map = new HashMap<>();
//            map.put("topics", topics);
//            map.put("newsList", newsList);
//            request.setAttribute("map", map);
//            request.getRequestDispatcher("index.jsp").forward(request, response);
//            out.print(newsList);//要返回json格式的字符串[{"nid":1,"ntitle":"新闻标题","ncreateDate":"时间字符串"},{},{},,{}]
            /*StringBuilder sb = new StringBuilder("[");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (News news : newsList) {
                sb.append("{\"nid\":"+news.getNid()+",\"ntitle\":\""+news.getNtitle().replace("\"","&quot;")+"\",\"ncreateDate\":\""+df.format(news.getNcreateDate())+"\"},");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append("]");
            out.print(sb.toString());*/

            String jsonStr = JSON.toJSONStringWithDateFormat(newsList,"yyyy-MM-dd hh:mm:ss", SerializerFeature.WriteMapNullValue);
            out.print(jsonStr);
        } else if ("readnews".equals(action)) {//查看单条新闻详细信息
            int nid = Integer.parseInt(request.getParameter("nid"));
            //读取单条新：标题，时间，作者，主体内容，评论列表
            News news = newsService.readNews(nid);
            request.setAttribute("news", news);
            request.getRequestDispatcher("news_read.jsp").forward(request, response);
        } else if ("backnews".equals(action)) {//管理员界面的新闻列表
            String currPageNo = request.getParameter("currPageNo");
            int pageIndex = currPageNo == null ? 1 : Integer.parseInt(currPageNo);
            Page newsPage = newsService.queryPageNews(pageIndex);
            request.setAttribute("newsPage", newsPage);
            request.getRequestDispatcher("newspages/admin.jsp").forward(request, response);
        } else if ("del".equals(action)) {//删除新闻
            int nid = Integer.parseInt(request.getParameter("nid"));
            int result = newsService.delNews(nid);
            if (result > 0) {//删除成功
                out.print("<script type=\"text/javaScript\">" +
                        "alert(\"删除成功！\");" +
                        "location.href = \"news.do?action=backnews\";" +
                        "</script>");
            } else {
                out.print("<script type=\"text/javaScript\">" +
                        "alert(\"删除失败！请联系管理员\");" +
                        "location.href = \"news.do?action=backnews\";" +
                        "</script>");
            }
        } else if ("addnews".equals(action)) {//添加新闻
            News news = new News();
            String fieldName = "";  //表单字段元素的name属性值
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            //上传文件的存储路径（服务器文件系统上的绝对文件路径）
            String uploadFilePath = request.getServletContext().getRealPath("upload");
            if (isMultipart) {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setSizeMax(1024 * 1024 * 5);
                //解析form表单中所有文件
                try {
                    List<FileItem> items = upload.parseRequest(request);
                    for (FileItem item : items) {
                        if (item.isFormField()) {  //普通表单字段
                            fieldName = item.getFieldName();  //表单字段的name属性值
                            if (fieldName.equals("ntid")) {   //主题
                                news.setNtid(Integer.parseInt(item.getString("UTF-8")));
                            } else if (fieldName.equals("ntitle")) { //标题
                                news.setNtitle(item.getString("UTF-8"));
                            } else if (fieldName.equals("nauthor")) {//作者
                                news.setNauthor(item.getString("UTF-8"));
                            } else if (fieldName.equals("nsummary")) { //摘要
                                news.setNsummary(item.getString("UTF-8"));
                            } else if (fieldName.equals("ncontent")) { //内容
                                news.setNcontent(item.getString("UTF-8"));
                            }
                        } else {   //文件表单字段
                            String fileName = item.getName();
                            List<String> filType = Arrays.asList("gif", "jpg", "jpeg", "png", "bmp");
                            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
                            if (fileName != null && !fileName.equals("") && !filType.contains(ext)) {  //判断文件类型是否在允许范围内
                                out.print("<script type=\"text/javascript\">" +
                                        "alert(\"上传失败，文件类型只能是gif、jpg、jpeg\");" +
                                        "location.href = \"newspages/news_add.jsp\";" +
                                        "</script>");
                            } else if (fileName != null && !fileName.equals("")) {
                                File saveFile = new File(uploadFilePath, item.getName());
                                item.write(saveFile);
                                news.setNpicPath(uploadFilePath + "\\" + item.getName());
                            }
                        }
                    }
                } catch (FileUploadBase.SizeLimitExceededException ex) {
                    out.print("<script type=\"text/javascript\">" +
                            "alert(\"上传失败，文件太大，单个文件的最大限制是：5MB\");" +
                            "location.href = \"newspages/news_add.jsp\";" +
                            "</script>");
                } catch (FileUploadException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (newsService.addNews(news)) {
                out.print("<script type=\"text/javascript\">" +
                        "alert(\"上传成功！\");" +
                        "location.href = \"newspages/admin.jsp\";" +
                        "</script>");
            } else {
                out.print("<script type=\"text/javascript\">" +
                        "alert(\"上传失败，请联系管理员！\");" +
                        "location.href = \"newspages/news_add.jsp\";" +
                        "</script>");
            }
        }
    }
}
