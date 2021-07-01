<%--
  Created by IntelliJ IDEA.
  User: wuligang
  Date: 2021/6/16
  Time: 8:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>用户注册</title>
</head>
<body>

<span style="color: red">${error}</span>
<form action="user.do" method="post" enctype="application/x-www-form-urlencoded">
    用户名：<input type="text" name="uname" required/><br/>
    密码：<input type="password" name="upwd" required/><br>
    再次输入密码：<input type="password" name="reupwd" required/><br>
    <input type="hidden" name="action" value="doregister" /><br>
    <input type="submit" value="注册"/>
</form>
</body>
</html>
