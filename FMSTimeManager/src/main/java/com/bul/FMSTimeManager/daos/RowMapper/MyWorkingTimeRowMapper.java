package com.bul.FMSTimeManager.daos.RowMapper;

import com.bul.FMSTimeManager.models.Report;
import com.bul.FMSTimeManager.models.Settings;
import com.bul.FMSTimeManager.models.User;
import com.bul.FMSTimeManager.models.WorkingTimeReport;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyWorkingTimeRowMapper implements RowMapper<WorkingTimeReport> {
    @Override
    public WorkingTimeReport mapRow(ResultSet rs, int rowNum) throws SQLException {
        WorkingTimeReport wtr = new WorkingTimeReport();

        Report r = new Report();
        User emp = new User();
        Settings reqType = new Settings();

        wtr.setNumber_of_entry(rs.getInt("number_of_entry"));
        wtr.setNumber_of_exit(rs.getInt("number_of_exit"));
        wtr.setRequest_status(rs.getInt("request_status"));
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
        wtr.setReport(r);
        return wtr;
    }
}
