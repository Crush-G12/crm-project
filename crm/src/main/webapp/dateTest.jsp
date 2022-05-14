<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" + request.getServerName()
        + ":" + request.getServerPort()
        + request.getContextPath() + "/"; %>
<html>
<head>
    <base href="<%= basePath %>">
    <%--jQuery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <%--bootStrap框架--%>
    <link rel="stylesheet" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <%--bootStrap日期插件--%>
    <link rel="stylesheet" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" />
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <title>测试日历插件</title>

    <script type="text/javascript">
        <%--入口函数——页面加载完成后执行--%>
        $(function (){
            $("#date").datetimepicker({
                //通过参数控制日历的显示效果
                language:'zh-CN',
                //生成的日期格式
                format:'yyyy-mm-dd',
                //显示到‘日’即可
                minView:'month',
                //初始化日期
                initialDate:new Date(),
                //自动关闭日历
                autoclose:true,
                //选择当天的日期
                todayBtn:true,
                //显示清空按钮
                clearBtn:true
            });
        });
    </script>

</head>
<body>
<input type="text" id="date" readonly>

</body>
</html>
