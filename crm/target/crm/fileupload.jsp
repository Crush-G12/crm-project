<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" + request.getServerName()
        + ":" + request.getServerPort()
        + request.getContextPath() + "/"; %>
<html>
<head>
    <base href="<%= basePath %>">
    <title>测试文件上传</title>
</head>
<body>
<form action="/crm/workbench/activity/fileUpload" method="post" enctype="multipart/form-data">
    <input type="file" name="file"/><br>
    <input type="submit" value="提交"><br>
</form>
</body>
</html>
