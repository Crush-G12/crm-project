package com.xie.crm.workbench.web.controller;

import com.xie.crm.commons.contants.Contants;
import com.xie.crm.commons.domain.ReturnObject;
import com.xie.crm.commons.utils.DateUtils;
import com.xie.crm.commons.utils.UUIDUtils;
import com.xie.crm.settings.domain.User;
import com.xie.crm.settings.service.UserService;
import com.xie.crm.workbench.domain.Activity;
import com.xie.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    UserService userService;

    @Autowired
    ActivityService activityService;

    @RequestMapping(value = "/workbench/activity/index")
    public String index(HttpServletRequest request){
        //查询所有用户
        List<User> userList = userService.queryAllUsers();
        //保存到作用域中
        request.setAttribute("userList",userList);
        return "workbench/activity/index";
    }

    @RequestMapping(value = "/workbench/activity/createActivity")
    @ResponseBody
    public Object createActivity(Activity activity, HttpSession session){
        //用UUID生成id
        activity.setId(UUIDUtils.getUUID());
        //生成创建时间
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        //获取作者的id
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setCreateBy(user.getId());
        //保存数据
        ReturnObject returnObject = new ReturnObject();
        try {
            int result = activityService.saveActivityService(activity);
            //如果成功
            if(result > 0){
                returnObject.setCode(Contants.CODE_SUCCESS);
            }else {
                //如果失败
                returnObject.setCode(Contants.CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后再试....");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //如果失败
            returnObject.setCode(Contants.CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后再试....");
        }
        return returnObject;
    }

    @RequestMapping(value = "/workbench/activity/queryActivityByConditionForPage")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,String owner,String startDate,
                                                  String endDate,Integer pageNo,Integer pageSize){
        //封装到Map里
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        Integer beginNo;
        if(pageNo != null && pageSize != null){
            beginNo = (pageNo-1)*pageSize;
        }else {
            beginNo = null;
        }
        map.put("beginNo",beginNo);
        map.put("pageSize",pageSize);
        //查询数据
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int totalCount = activityService.queryCountByConditionForPage(map);
        //生成json字符串
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("activityList",activityList);
        resultMap.put("totalCount",totalCount);

        return resultMap;
    }

    @RequestMapping(value = "/workbench/activity/deleteActivityByIds")
    @ResponseBody
    public Object deleteActivityByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        int result = 0;
        try {
            result = activityService.deleteActivityByIds(id);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.CODE_FAIL);
            returnObject.setMessage("系统忙碌，请稍后再试...");
        }
        if(result > 0){
            //删除成功
            returnObject.setCode(Contants.CODE_SUCCESS);
            returnObject.setMessage("删除成功");
        }else {
            //删除失败
            returnObject.setCode(Contants.CODE_FAIL);
            returnObject.setMessage("系统忙碌，请稍后再试...");
        }
        return returnObject;
    }

    @RequestMapping(value = "/workbench/activity/queryActivityById")
    @ResponseBody
    public Object queryActivityById(String id){
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }


    @RequestMapping(value = "/workbench/activity/saveEditActivity")
    @ResponseBody
    public Object saveEditActivity(Activity activity,HttpSession session) {
        //获取参数，封装参数
        //生成创建时间
        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        //获取作者的id
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setEditBy(user.getId());
        //修改市场活动，生成响应信息
        ReturnObject returnObject = new ReturnObject();
        int result = 0;
        try {
            result = activityService.saveEditActivity(activity);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后再试...");
        }

        if (result > 0) {
            //修改成功
            returnObject.setCode(Contants.CODE_SUCCESS);
            returnObject.setMessage("修改成功");
        }else {
            //修改失败
            returnObject.setCode(Contants.CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后再试...");
        }

        return returnObject;
    }

    @RequestMapping(value = "/workbench/activity/fileDownload")
    public void fileDownload(HttpServletResponse response){
        //返回的是一个 应用程序/二进制文件（不清楚下载类型的时候）
        response.setContentType("application/octet-stream;charset=UTF-8");
        try {
            //获取输出流
            OutputStream outputStream = response.getOutputStream();

            //设置响应头，默认激活下载窗口
            //attachment:以附件形式打开,filename:设置默认的文件名
            response.addHeader("Content-Disposition","attachment;filename=student.xls");

            //磁盘与程序建立了一个“管道”
            InputStream is = new FileInputStream("C:\\Users\\Admin\\Desktop\\Java\\SSM项目实战\\ServerDir\\student.xls");
            //程序可以通过这个“管道”把数据读进来
            //为了提高读取的效率，建立一个缓冲区，每次读一段数据
            byte[] buffer = new byte[256];
            //len表示每次读取的字节数，若为-1，表示已经读完了
            int len = 0;
            //每次把数据读到256字节的缓冲区里
            while ((len = is.read(buffer)) != -1){
                //把缓冲区的数据通过输出流往外写
                //读取多少，就输出多少(从第0个开始，往外输出len个字节的数据)
                outputStream.write(buffer,0,len);
            }

            //关闭输入流，刷新输出流
            is.close();
            //原则：在Java中，资源由谁开启，最后就由谁关闭
            //OutputStream是Tomcat生成的（从response获取），最后应该由Tomcat关闭
            //把数据刷新一下，就能全部写到浏览器了
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/workbench/activity/exportAllActivities")
    public void exportAllActivities(HttpServletResponse response) throws Exception{
        //查询所有市场活动
        List<Activity> activityList = activityService.queryAllActivities();
        //创建Excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        //创建表头
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("编辑时间");
        cell = row.createCell(10);
        cell.setCellValue("编辑者");
        //编辑数据行
        //在循环外面创建引用，效率更高（不用每次循环都创建一个引用）
        Activity activity = null;
        for(int i=0; i<activityList.size(); i++){
            activity = activityList.get(i);
            row = sheet.createRow(i+1);
            //设置每一列的数据
            cell = row.createCell(0);
            cell.setCellValue(activity.getId());
            cell = row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell = row.createCell(2);
            cell.setCellValue(activity.getName());
            cell = row.createCell(3);
            cell.setCellValue(activity.getStartDate());
            cell = row.createCell(4);
            cell.setCellValue(activity.getEndDate());
            cell = row.createCell(5);
            cell.setCellValue(activity.getCost());
            cell = row.createCell(6);
            cell.setCellValue(activity.getDescription());
            cell = row.createCell(7);
            cell.setCellValue(activity.getCreateTime());
            cell = row.createCell(8);
            cell.setCellValue(activity.getCreateBy());
            cell = row.createCell(9);
            cell.setCellValue(activity.getEditTime());
            cell = row.createCell(10);
            cell.setCellValue(activity.getEditTime());
        }

        OutputStream outputStream = new FileOutputStream("C:\\Users\\Admin\\Desktop\\Java\\SSM项目实战\\ServerDir\\activityList.xls");
        //workbook.write(outputStream);
        //关闭资源
        outputStream.close();

        //将生成的文件输出到客户端，供用户下载
        response.setContentType("application/octet-stream;charset=UTF-8");
        //设置响应头信息
        response.setHeader("Content-Disposition","attachment;filename=activityList.xls");
        //获取输出流
        OutputStream os = response.getOutputStream();
        //读取文件，先获取文件输入流
        /*InputStream is = new FileInputStream("C:\\Users\\Admin\\Desktop\\Java\\SSM项目实战\\ServerDir\\activityList.xls");
        byte[] buffer = new byte[256];
        int i = 0;
        while ((i=is.read(buffer))!=-1){
            os.write(buffer,0,i);
        }*/

        //直接向内存写数据
        workbook.write(os);

        //关闭、刷新
        os.flush();
        workbook.close();

    }
}
