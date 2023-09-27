package com.bul.FMSTimeManager.daos.RowMapper;

import com.bul.FMSTimeManager.models.Report;
import com.bul.FMSTimeManager.models.RequestTimeReport;
import com.bul.FMSTimeManager.models.Settings;
import com.bul.FMSTimeManager.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyEarlyLeaveAndLateComingRowMapper implements RowMapper<RequestTimeReport> {
    @Override
    public RequestTimeReport mapRow(ResultSet rs, int rowNum) throws SQLException {
        RequestTimeReport rtr = new RequestTimeReport();
        Report r = new Report();
        User emp = new User();
        Settings reqType = new Settings();
        //set value
        rtr.setRequest_time_report_id(rs.getInt("request_time_report_id"));
        rtr.setIs_coming_late(rs.getInt("is_coming_late"));
        rtr.setIs_early_leave(rs.getInt("is_early_leave"));
        rtr.setLate_time(rs.getTime("late_time"));
        rtr.setEarly_leave(rs.getTime("early_leave"));
        //
        r.setDate(rs.getDate("date"));
        r.setFirst_entry(rs.getTime("first_entry"));
        r.setLast_exit(rs.getTime("last_exit"));
        r.setIn_office_time(rs.getTime("in_office_time"));
        r.setIn_office_working_time_frame(rs.getTime("in_office_within_working_time_frame"));
        r.setIn_working_area(rs.getTime("in_working_area"));
        reqType.setSetting_id(rs.getInt("request_type"));
        r.setRequest_type(reqType);
        emp.setUser_id(rs.getInt("employee_id"));
        r.setEmployee(emp);
        rtr.setReport(r);
        rtr.setRequest_status(rs.getInt("request_status"));
        return rtr;
    }
}
