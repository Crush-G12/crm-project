package com.xie.crm.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileOutputStream;
import java.io.IOException;

public class CreateExcel {
    public static void main(String[] args) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("student sheet");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("Stu_No");
        cell = row.createCell(1);
        cell.setCellValue("Stu_Name");
        cell = row.createCell(2);
        cell.setCellValue("Stu_Age");

        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        for(int i=1;i<=10;i++){
            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue("Stu_No" + i);
            cell = row.createCell(1);
            cell.setCellValue("Stu_Name" + i);
            cell = row.createCell(2);
            cell.setCellValue("Stu_Age" + i);
            cell.setCellStyle(cellStyle);
        }

        //根据文件生成输出流
        //往文件输出数据，用文件输出流
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Admin\\Desktop\\Java\\SSM项目实战\\ServerDir\\student.xls");
            workbook.write(fileOutputStream);

            fileOutputStream.close();
            workbook.close();

            System.out.println("creat successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
