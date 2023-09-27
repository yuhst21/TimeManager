package com.bul.FMSTimeManager.controllers;
//
//import com.bul.FMSTimeManager.common.excel.EarlyAndLateTimeExportExcel;
//import com.bul.FMSTimeManager.common.excel.WorkingTimeExport;
import com.bul.FMSTimeManager.daos.ReportRepository;
import com.bul.FMSTimeManager.daos.SettingRepository;
import com.bul.FMSTimeManager.daos.UserRepository;
import com.bul.FMSTimeManager.models.AbnormalReport;
import com.bul.FMSTimeManager.models.RequestTimeReport;
import com.bul.FMSTimeManager.models.User;
import com.bul.FMSTimeManager.models.WorkingTimeReport;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class ReportController {
    @Autowired
    SettingRepository settingRepository;
    @Autowired
    UserRepository userRepsitory;
    @Autowired
    ReportRepository reportRepository;

    @GetMapping("/my_early_leave_and_late_coming")
    public String listMyEarlyLeaveAndMyLateComing(
            @RequestParam(value = "date_get_start", required = false) String raw_start_date
            , @RequestParam(value = "date_get_end", required = false) String raw_end_date
            , @RequestParam(value = "page", required = false) String raw_page
            , @RequestParam(value = "size_of_page", required = false) String raw_size_page
            , Model model, HttpSession httpSession
            ) {
        User u = (User) httpSession.getAttribute("user_object");
        //validate value
        Date start_date = (raw_start_date != null && raw_start_date.length() > 0) ? Date.valueOf(raw_start_date) : null;
        Date end_date = (raw_end_date != null && raw_end_date.length() > 0) ? Date.valueOf(raw_end_date) : null;
        if (raw_page == null || raw_page.equals("")) {
            raw_page = "1";
        }
        if (raw_size_page == null || raw_size_page.equals("")) {
            raw_size_page = "5";
        }
        String startDateString = start_date + "";
        if (start_date == null) {
            startDateString = "";
        }
        String endDateString = end_date + "";
        if (end_date == null) {
            endDateString = "";
        }
        int page_index = Integer.parseInt(raw_page);
        int page_size = Integer.parseInt(raw_size_page);
        int count = reportRepository.count(u.getUser_id(), start_date, end_date);
        int total_page = (count % page_size == 0) ? (count / page_size) : (count / page_size) + 1;
        //pagger and filter
        List<RequestTimeReport> requestTimeReportList = reportRepository.paggerAndfilter(page_size, page_index, u.getUser_id(), start_date, end_date);

        // add attribute
        model.addAttribute("total_coming_late", reportRepository.countComingLate(u.getUser_id()));
        model.addAttribute("total_early_leave", reportRepository.counEarlyLeave(u.getUser_id()));
        model.addAttribute("page_size", page_size);
        model.addAttribute("page_index", page_index);
        model.addAttribute("total_page", total_page);
        model.addAttribute("time_report_list", requestTimeReportList);
        model.addAttribute("date_start", startDateString);
        model.addAttribute("date_end", endDateString);
        return "Employee/my_late_coming_and_early_leave";
    }

//    @GetMapping("/export_my_early_and_late_time")
//    public String exportTime(HttpServletResponse response,HttpSession httpSession) throws IOException {
//        User u = (User) httpSession.getAttribute("user_object");
//        response.setContentType("application/octet-stream");
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM");
//        String currentDateTime = dateFormatter.format(new java.util.Date());
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=my_early_and_late_time_" + currentDateTime + ".xlsx";
//        response.setHeader(headerKey, headerValue);
//        List<RequestTimeReport> requestTimeReportList = reportRepository.listByEmp(u.getUser_id());
//        EarlyAndLateTimeExportExcel earlyAndLateTimeExportExcel = new EarlyAndLateTimeExportExcel(requestTimeReportList);
//        earlyAndLateTimeExportExcel.exportToExcel(response);
//        return "Employee/my_late_coming_and_early_leave";
//    }

    @GetMapping("/my_working_time_report")
    public String workingTimeReport(@RequestParam(value = "date_get_start", required = false) String raw_start_date
            , @RequestParam(value = "date_get_end", required = false) String raw_end_date
            , @RequestParam(value = "page", required = false) String raw_page
            , @RequestParam(value = "size_of_page", required = false) String raw_size_page
            , Model model,HttpSession httpSession

    ) {
        User u = (User) httpSession.getAttribute("user_object");
        //validate value
        Date start_date = (raw_start_date != null && raw_start_date.length() > 0) ? Date.valueOf(raw_start_date) : null;
        Date end_date = (raw_end_date != null && raw_end_date.length() > 0) ? Date.valueOf(raw_end_date) : null;
        if (raw_page == null || raw_page.equals("")) {
            raw_page = "1";
        }
        if (raw_size_page == null || raw_size_page.equals("")) {
            raw_size_page = "5";
        }
        String startDateString = start_date + "";
        if (start_date == null) {
            startDateString = "";
        }
        String endDateString = end_date + "";
        if (end_date == null) {
            endDateString = "";
        }
        int page_index = Integer.parseInt(raw_page);
        int page_size = Integer.parseInt(raw_size_page);
        //count total page
        int count = reportRepository.count(u.getUser_id(), start_date, end_date);
        int total_page = (count % page_size == 0) ? (count / page_size) : (count / page_size) + 1;
        //pagger and filter
        List<WorkingTimeReport> workingTimeReportList = reportRepository.paggerAndfilterWorkingTime(page_size, page_index, u.getUser_id(), start_date, end_date);
        //add attribute
        model.addAttribute("page_size", page_size);
        model.addAttribute("page_index", page_index);
        model.addAttribute("total_page", total_page);
        model.addAttribute("workingTimeReportList", workingTimeReportList);
        model.addAttribute("date_start", startDateString);
        model.addAttribute("date_end", endDateString);
        return "Employee/my_working_time_report";
    }


//    @GetMapping("/export_my_working_time")
//    public String exportTimeWorking(HttpServletResponse response,HttpSession httpSession) throws IOException {
//        User u = (User) httpSession.getAttribute("user_object");
//        response.setContentType("application/octet-stream");
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM");
//        String currentDateTime = dateFormatter.format(new java.util.Date());
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=my_working_time" + currentDateTime + ".xlsx";
//        response.setHeader(headerKey, headerValue);
//        List<WorkingTimeReport> workingTimeReportList = reportRepository.listByEmpWorking(u.getUser_id());
//        WorkingTimeExport workingTimeExport = new WorkingTimeExport(workingTimeReportList);
//        workingTimeExport.exportToExcel(response);
//        return "Employee/my_working_time_report";
//    }

    @GetMapping("/my_abnormal_case")
    public String abnormalCase(@RequestParam(value = "date_get_start", required = false) String raw_start_date
            , @RequestParam(value = "date_get_end", required = false) String raw_end_date
            , @RequestParam(value = "page", required = false) String raw_page
            , @RequestParam(value = "size_of_page", required = false) String raw_size_page
            , @RequestParam(value = "leave_no_request", required = false) String raw_leave_without_request
            , @RequestParam(value = "work_with_approved", required = false) String raw_working_on_approved_leave
            , Model model,HttpSession httpSession

    ) {
        User u = (User) httpSession.getAttribute("user_object");
        //validate value
        Date start_date = (raw_start_date != null && raw_start_date.length() > 0) ? Date.valueOf(raw_start_date) : null;
        Date end_date = (raw_end_date != null && raw_end_date.length() > 0) ? Date.valueOf(raw_end_date) : null;
        int leave_without_request = (raw_leave_without_request != null && !raw_leave_without_request.equals("-1")) ? Integer.parseInt(raw_leave_without_request) : -1;
        int working_on_approved_leave = (raw_working_on_approved_leave != null && !raw_working_on_approved_leave.equals("-1")) ? Integer.parseInt(raw_working_on_approved_leave) : -1;
        if (raw_page == null || raw_page.equals("")) {
            raw_page = "1";
        }
        if (raw_size_page == null || raw_size_page.equals("")) {
            raw_size_page = "5";
        }
        String startDateString = start_date + "";
        if (start_date == null) {
            startDateString = "";
        }
        String endDateString = end_date + "";
        if (end_date == null) {
            endDateString = "";
        }
        int page_index = Integer.parseInt(raw_page);
        int page_size = Integer.parseInt(raw_size_page);
        //count total page
        int count = reportRepository.countAbnormal(u.getUser_id(), start_date, end_date,leave_without_request,working_on_approved_leave);
        int total_page = (count % page_size == 0) ? (count / page_size) : (count / page_size) + 1;
        //pagger and filter
        List<AbnormalReport> abnormalReportList = reportRepository.paggerAndfilterAbnormal(page_size, page_index, u.getUser_id(), start_date, end_date,leave_without_request,working_on_approved_leave);

        // add attribute
        model.addAttribute("page_size", page_size);
        model.addAttribute("page_index", page_index);
        model.addAttribute("total_page", total_page);
        model.addAttribute("abnormalReportList", abnormalReportList);
        model.addAttribute("date_start", startDateString);
        model.addAttribute("date_end", endDateString);
        model.addAttribute("leave_without_request", leave_without_request);
        model.addAttribute("work_on_approved_leave", working_on_approved_leave);

        return "Employee/my_abnormal_report";
    }
}
