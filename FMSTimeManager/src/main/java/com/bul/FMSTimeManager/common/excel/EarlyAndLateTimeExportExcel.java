package com.bul.FMSTimeManager.common.excel;

import com.bul.FMSTimeManager.models.RequestTimeReport;
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

public class EarlyAndLateTimeExportExcel {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<RequestTimeReport> requestTimeReportList;

    public EarlyAndLateTimeExportExcel(List<RequestTimeReport> requestTimeReportList) {
        this.requestTimeReportList = requestTimeReportList;
        workbook = new XSSFWorkbook();
    }

    public void writeHeaderLine() {
        sheet = workbook.createSheet("Users");

        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Date", style);
        createCell(row, 1, "In Coming Late", style);
        createCell(row, 2, "In Early Leave", style);
        createCell(row, 3, "First Entry", style);
        createCell(row, 4, "Last Exit", style);
        createCell(row, 5, "Late Time", style);
        createCell(row, 6, "Early Leave", style);
        createCell(row, 7, "In Office Time(hours)", style);
        createCell(row, 8, "In Office Time Within Working Time Frame(hours)", style);
        createCell(row, 9, "In Working Area(hours)", style);
        createCell(row, 10, "Request Type", style);
        createCell(row, 11, "Request Status", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        //check input value to export excel
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
        for (RequestTimeReport req : requestTimeReportList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, req.getReport().getDate().toString(), style);
            createCell(row, columnCount++, req.getIs_coming_late(), style);
            createCell(row, columnCount++, req.getIs_early_leave(), style);
            createCell(row, columnCount++, req.getReport().getFirst_entry().toString(), style);
            createCell(row, columnCount++, req.getReport().getLast_exit().toString(), style);
            createCell(row, columnCount++, req.getLate_time().toString(), style);
            createCell(row, columnCount++, req.getEarly_leave().toString(), style);
            createCell(row, columnCount++, req.getReport().getIn_office_time().toString(), style);
            createCell(row, columnCount++, req.getReport().getIn_office_working_time_frame().toString(), style);
            createCell(row, columnCount++, req.getReport().getIn_working_area().toString(), style);
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