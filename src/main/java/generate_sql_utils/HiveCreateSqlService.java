package generate_sql_utils;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;



public class HiveCreateSqlService {
    public static void main(String[] args){
        ExcelToHiveDDL("E:\\附件6：投后_数据模型设计V1.0(1).xlsx");
    }


    public static void ExcelToHiveDDL(String excel_file_str) {
        //sheet所在页数
        int sheet_num=8;

        try {
            FileInputStream file = new FileInputStream(new File(excel_file_str));

            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(sheet_num); // Assuming 8th sheet, index starts from 0
            DataFormatter dataFormatter = new DataFormatter();

            StringBuilder hiveDDL = new StringBuilder();
            hiveDDL.append("CREATE TABLE IF NOT EXISTS ");

            Row tableNameRow = sheet.getRow(2);
            Cell tableNameCell = tableNameRow.getCell(2); // Assuming table name is in column C (index 2)
            String tableName = dataFormatter.formatCellValue(tableNameCell);
            hiveDDL.append(tableName).append(" (");

            for (Row row : sheet) {
                if (row.getRowNum() > 0) {
                    Cell fieldNameCell = row.getCell(5); // Assuming field names are in column F (index 5)
                    Cell fieldTypeCell = row.getCell(6); // Assuming field types are in column G (index 6)

                    String fieldName = dataFormatter.formatCellValue(fieldNameCell);
                    String fieldType = dataFormatter.formatCellValue(fieldTypeCell);

                    hiveDDL.append(fieldName).append(" ").append(fieldType).append(", ").append("\n");
                }
            }

            hiveDDL.delete(hiveDDL.length() - 2, hiveDDL.length()); // Remove trailing comma and space
            hiveDDL.append(");");

            System.out.println("Hive DDL for table " + tableName + ":\n" + hiveDDL.toString());

            workbook.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
