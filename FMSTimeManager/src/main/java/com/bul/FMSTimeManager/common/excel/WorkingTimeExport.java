package com.bul.FMSTimeManager.common.excel;

import com.bul.FMSTimeManager.models.WorkingTimeReport;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class WorkingTimeExport {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<WorkingTimeReport> workingTimeReportList;

    public WorkingTimeExport(List<WorkingTimeReport> workingTimeReportList) {
        this.workingTimeReportList = workingTimeReportList;
        workbook = new XSSFWorkbook();
    }

    public void writeHeaderLine() {
        sheet = workbook.createSheet("WorkingTime");

        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Date", style);
        createCell(row, 1, "First Entry", style);
        createCell(row, 2, "Last Exit", style);
        createCell(row, 3, "In Office Time(hours)", style);
        createCell(row, 4, "In Office Time Within Working Time Frame(hours)", style);
        createCell(row, 5, "In Working Area(hours)", style);
        createCell(row, 6, "Number of Entry", style);
        createCell(row, 7, "Number of Exit", style);
        createCell(row, 8, "Request Type", style);
        createCell(row, 9, "Request Status", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        //check value input
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        //loop from db to excel
        for (WorkingTimeReport req : workingTimeReportList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, req.getReport().getDate().toString(), style);
            createCell(row, columnCount++, req.getReport().getFirst_entry().toString(), style);
            createCell(row, columnCount++, req.getReport().getLast_exit().toString(), style);
            createCell(row, columnCount++, req.getReport().getIn_office_time().toString(), style);
            createCell(row, columnCount++, req.getReport().getIn_office_working_time_frame().toString(), style);
            createCell(row, columnCount++, req.getReport().getIn_working_area().toString(), style);
            createCell(row, columnCount++, req.getNumber_of_entry(), style);
            createCell(row, columnCount++, req.getNumber_of_exit(), style);
            createCell(row, columnCount++, req.getReport().getRequest_type().getSetting_id(), style);
            createCell(row, columnCount++, req.getRequest_status(), style);

        }
    }

    public void exportToExcel(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }
}
