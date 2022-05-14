package com.xie.crm.poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;

public class ParseExcel {
    public static void main(String[] args) throws Exception{

        //将磁盘中的文件加载到程序中，创建HSSFWorkbook对象，Excel的所有信息都封装在里面
        InputStream is = new FileInputStream("C:\\Users\\Admin\\Desktop\\Java\\SSM项目实战\\ServerDir\\upload.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(is);
        //逐一获取，先获取HSSFSheet对象,通常根据页的下标获取(下标从0开始)
        HSSFSheet sheet = workbook.getSheetAt(0);
        //再获取行
        HSSFRow row = null;
        HSSFCell cell = null;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            //获取每列的信息
            for (int j = 0; j < row.getLastCellNum(); j++) {
                cell = row.getCell(j);
                String str = getCellValueToStr(cell);
                System.out.println(str);
            }
        }

    }

    //工具方法

    /**
     * 获取HSSF列的值
     * @return
     */
    public static String getCellValueToStr(HSSFCell cell){
        String result = "";
        //获取数据前，要根据对应的数据类型调用对应的方法，否则会报错
        if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
            //如果是String类型的数据，则调用String类型的方法
            String stringCellValue = cell.getStringCellValue();
            result = stringCellValue;
        }else if(cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN){
            boolean booleanCellValue = cell.getBooleanCellValue();
            result = booleanCellValue + "";
        }else if(cell.getCellType() == HSSFCell.CELL_TYPE_ERROR){
            byte errorCellValue = cell.getErrorCellValue();
            result = errorCellValue + "";
        }else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
            double numericCellValue = cell.getNumericCellValue();
            result = numericCellValue + "";
        }else if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA){
            String cellFormula = cell.getCellFormula();
            result = cellFormula;
        }

        return result;
    }
}
