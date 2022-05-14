<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" + request.getServerName()
        + ":" + request.getServerPort()
        + request.getContextPath() + "/"; %>
<html>
<head>
    <base href="<%= basePath %>">
    <title>文件下载</title>
    <%--jQuery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>

    <script type="text/javascript">
        $(function (){
            //所有文件下载的请求，只能发送同步请求
            $("#download_btn").click(function (){
                window.location.href="/crm/workbench/activity/fileDownload";
            });
        });
    </script>
</head>
<body>
<input type="button" value="下载文件" id="download_btn"/>
</body>
</html>
