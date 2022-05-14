package com.xie.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * 操作Excel文件的工具类
 */
public class HSSFUtils {

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
