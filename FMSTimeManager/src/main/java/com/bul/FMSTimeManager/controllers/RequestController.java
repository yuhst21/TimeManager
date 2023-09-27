package com.bul.FMSTimeManager.controllers;

//import com.bul.FMSTimeManager.common.email.SendEmail;
import com.bul.FMSTimeManager.daos.RequestRepository;
import com.bul.FMSTimeManager.daos.SettingRepository;
import com.bul.FMSTimeManager.daos.UserRepository;
import com.bul.FMSTimeManager.models.Request;
import com.bul.FMSTimeManager.models.Settings;
import com.bul.FMSTimeManager.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.YearMonth;
import java.util.List;

@Controller
public class RequestController {
    @Autowired
    RequestRepository requestRespository;
    @Autowired
    SettingRepository settingRepository;
    @Autowired
    UserRepository userRepsitory;
//    @Autowired
//    SendEmail sendEmail;


    @GetMapping("/my_request")
    public String listRequestByEmployee(
            @RequestParam(value = "date_get_start", required = false) String raw_start_date
            , @RequestParam(value = "date_get_end", required = false) String raw_end_date
            , @RequestParam(value = "typeReq", required = false) String raw_request_type
            , @RequestParam(value = "status_get", required = false) String raw_status
            , @RequestParam(value = "page", required = false) String raw_page
            , @RequestParam(value = "size_of_page", required = false) String raw_size_page
            , Model model, HttpSession httpSession)  throws Exception{

        User u = (User) httpSession.getAttribute("user_object");

        //validate value
        Date start_date = (raw_start_date != null && raw_start_date.length() > 0) ? Date.valueOf(raw_start_date) : null;
        Date end_date = (raw_end_date != null && raw_end_date.length() > 0) ? Date.valueOf(raw_end_date) : null;
        int request_type = (raw_request_type != null && !raw_request_type.equals("-1")) ? Integer.parseInt(raw_request_type) : -1;
        int status = (raw_status != null && !raw_status.equals("-1")) ? Integer.parseInt(raw_status) : -1;
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
        //paging and count total
        int count = requestRespository.count(u.getUser_id(), start_date, end_date, request_type, status);
        int total_page = (count % page_size == 0) ? (count / page_size) : (count / page_size) + 1;
        List<Request> requestList = requestRespository.paggingAndfilter(page_size, page_index, u.getUser_id(), start_date, end_date, request_type, status);
        //add attribure to send value to web

        model.addAttribute("page_size", page_size);
        model.addAttribute("page_index", page_index);
        model.addAttribute("total_page", total_page);
        model.addAttribute("listRequest", requestList);
        model.addAttribute("list_request_type", settingRepository.listRequestType());
        model.addAttribute("date_start", startDateString);
        model.addAttribute("date_end", endDateString);
        model.addAttribute("request_type", request_type);
        model.addAttribute("status", status);
        return "Employee/my_request";
    }

    @GetMapping("/create_my_request")
    public String createRequest(Model model) {
        model.addAttribute("listPartialDay", settingRepository.listPartialDay());
        model.addAttribute("listRequest", settingRepository.listRequestType());
        model.addAttribute("listReason", settingRepository.listReason());
        model.addAttribute("listSuppervisor", userRepsitory.listUserByRole(42));
        model.addAttribute("listApprover", userRepsitory.listUserByRole(41));
        model.addAttribute("listEmailByRole", userRepsitory.listUserGmailByRole());
        return "Employee/create_my_request";
    }

    @GetMapping("/get_ticket_contain")
    public String getTicketRemain(
            @RequestParam(value = "month_check", required = false) String raw_month_check
            , Model model, HttpSession httpSession
    ) throws Exception {
        User u = (User) httpSession.getAttribute("user_object");
        //validate value
        YearMonth month_year = (raw_month_check != null && raw_month_check.length() > 0) ? YearMonth.parse(raw_month_check) : null;
        List<Request> requestList = requestRespository.findRequestRemainByMonth(u.getUser_id(), month_year);
        int count = 0;
        for (Request r : requestList
        ) {
            count++;
        }
        int maxTicket = 3;
        int totalTicketCanGet = 3 - count;
        // add session
        httpSession.setAttribute("year_with_month", month_year);
        httpSession.setAttribute("total_tic", totalTicketCanGet);
        httpSession.setAttribute("list_request_by_month", requestList);
        return "redirect:/create_my_request#modal-ticket";
    }

    @PostMapping("/create_my_request")
    public String addRequest(@RequestParam(value = "req_type", required = false) int request_type
            , @RequestParam(value = "req_start_date", required = false) Date start_date
            , @RequestParam(value = "req_partial", required = false) int partial_day
            , @RequestParam(value = "req_reason", required = false) int reason
            , @RequestParam(value = "req_detail_reason", required = false) String detail_reason
            , @RequestParam(value = "req_expected_approve", required = false) Date expectd_approve
            , @RequestParam(value = "req_end_date", required = false) Date end_date
            , @RequestParam(value = "req_duration", required = false) int duration
            , @RequestParam(value = "req_appover", required = false) int approver
            , @RequestParam(value = "req_supervisor", required = false) int suppervisor
            , @RequestParam(value = "req_inform_to", required = false) String inform_to
            , @RequestParam(value = "req_time", required = false) String time
            , Model model, HttpSession httpSession
    ) throws Exception {
        User u = (User) httpSession.getAttribute("user_object");
        int count = requestRespository.countReq();

        Request req = new Request();
        //new Setting
        Settings setReq = new Settings();
        Settings setParital = new Settings();
        Settings setReason = new Settings();
        //new User
        User uApprover = new User();
        User uSupervisor = new User();
        User uEmployee = new User();
        //set value
        setReq.setSetting_id(request_type);
        req.setRequest_id(count + 1);
        req.setRequest_type(setReq);
        req.setStart_date(start_date);
        setParital.setSetting_id(partial_day);
        req.setPartital_day(setParital);
        setReason.setSetting_id(reason);
        req.setReason(setReason);
        req.setDetail_reason(detail_reason);
        req.setExpected_approve(expectd_approve);
        req.setEnd_date(end_date);
        req.setDuration(duration);
        uApprover.setUser_id(approver);
        req.setApprover(uApprover);
        uSupervisor.setUser_id(suppervisor);
        req.setSupervisor(uSupervisor);
        req.setTime(time);
        uEmployee.setUser_id(u.getUser_id());
        req.setEmployee(uEmployee);
        //insert to db
        boolean check = requestRespository.insertVal(req);
        // check insert to sent email to manager
//        if (check) {
//            sendEmail.sendEmailToUser(inform_to, u);
//        }
        //add attribute
        model.addAttribute("send", "Send Success !!");
        model.addAttribute("listPartialDay", settingRepository.listPartialDay());
        model.addAttribute("listRequest", settingRepository.listRequestType());
        model.addAttribute("listReason", settingRepository.listReason());
        model.addAttribute("listSuppervisor", userRepsitory.listUserByRole(42));
        model.addAttribute("listApprover", userRepsitory.listUserByRole(41));
        model.addAttribute("listEmailByRole", userRepsitory.listUserGmailByRole());
        return "Employee/create_my_request";
    }

    @GetMapping("/receive_request_list")
    public String reviceRequestList(@RequestParam(value = "date_get_start", required = false) String raw_start_date
            , @RequestParam(value = "date_get_end", required = false) String raw_end_date
            , @RequestParam(value = "typeReq", required = false) String raw_request_type
            , @RequestParam(value = "status_get", required = false) String raw_status
            , @RequestParam(value = "page", required = false) String raw_page
            , @RequestParam(value = "size_of_page", required = false) String raw_size_page
            , @RequestParam(value = "requester", required = false) String raw_requester
            , @RequestParam(value = "status_change", required = false) String status_get
            , Model model, HttpSession httpSession) throws Exception{
        User u = (User) httpSession.getAttribute("user_object");
        //validate value
        Date start_date = (raw_start_date != null && raw_start_date.length() > 0) ? Date.valueOf(raw_start_date) : null;
        Date end_date = (raw_end_date != null && raw_end_date.length() > 0) ? Date.valueOf(raw_end_date) : null;
        int request_type = (raw_request_type != null && !raw_request_type.equals("-1")) ? Integer.parseInt(raw_request_type) : -1;
        int status = (raw_status != null && !raw_status.equals("-1")) ? Integer.parseInt(raw_status) : -1;
        String requester = (raw_requester != null && !raw_requester.equals("-1")) ? raw_requester : "";
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

        ///
        if (status_get != null) {
            String[] arr = status_get.split("_");
            String status_ch = arr[0];
            String request_id = arr[1];
            requestRespository.changeStatus(Integer.parseInt(status_ch), Integer.parseInt(request_id));
        }
        //paging and count total
        int count = requestRespository.countForHigerRole(u.getUser_id(), start_date, end_date, request_type, status, requester);
        int total_page = (count % page_size == 0) ? (count / page_size) : (count / page_size) + 1;
        List<Request> requestList = requestRespository.paggingAndfilterForHighRole(page_size, page_index, u.getUser_id(), start_date, end_date, request_type, status, requester);
        //add attribure to send value to web
        model.addAttribute("page_size", page_size);
        model.addAttribute("page_index", page_index);
        model.addAttribute("total_page", total_page);
        model.addAttribute("listRequest", requestList);
        model.addAttribute("list_request_type", settingRepository.listRequestType());
        model.addAttribute("requester_r", requester);
        model.addAttribute("date_start", startDateString);
        model.addAttribute("date_end", endDateString);
        model.addAttribute("request_type", request_type);
        model.addAttribute("status", status);
        model.addAttribute("listUserName", userRepsitory.listUserNameByRequest(u.getUser_id()));


        return "Employee/received_request_list";
    }

    @GetMapping("/send_email_for_all_user")
    public String sentEmailWhenHoliday() {
        return "Employee/send_email_for_all_user";
    }

    @PostMapping("/send_email_for_all_user")
    public String sendEmailForAll() {
        return "Employee/send_email_for_all_user";
    }

    @GetMapping("/auto_sent")
    public String autoSent(@RequestParam(value = "type_sent", required = false) int type) {
        switch (type) {
            case 1:
            case 2:
            case 3:
        }
        return "redirect:/send_email_for_all_user#modal-ticket";
    }
}
