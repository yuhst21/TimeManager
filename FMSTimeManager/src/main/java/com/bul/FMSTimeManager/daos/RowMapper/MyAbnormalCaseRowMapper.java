package com.bul.FMSTimeManager.daos.RowMapper;

import com.bul.FMSTimeManager.models.AbnormalReport;
import com.bul.FMSTimeManager.models.Report;
import com.bul.FMSTimeManager.models.Settings;
import com.bul.FMSTimeManager.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyAbnormalCaseRowMapper implements RowMapper<AbnormalReport> {
    @Override
    public AbnormalReport mapRow(ResultSet rs, int rowNum) throws SQLException {
        AbnormalReport ar = new AbnormalReport();
        Report r = new Report();
        User emp = new User();
        Settings reqType = new Settings();
        Settings parType = new Settings();
        Settings request = new Settings();
        //
        //
        r.setDate(rs.getDate("date"));
        r.setFirst_entry(rs.getTime("first_entry"));
        r.setLast_exit(rs.getTime("last_exit"));
        r.setIn_office_time(rs.getTime("in_office_time"));
        r.setIn_office_working_time_frame(rs.getTime("in_office_within_working_time_frame"));
        r.setIn_working_area(rs.getTime("in_working_area"));
        emp.setUser_id(rs.getInt("employee_id"));
        r.setEmployee(emp);
        reqType.setSetting_id(rs.getInt("abnormal_type_set"));
        reqType.setSetting_title(rs.getString("abnormal_type_title"));
        ar.setAbnormal_type(reqType);
        parType.setSetting_id(rs.getInt("partial_day_set"));
        parType.setSetting_title(rs.getString("partial_day_title"));
        ar.setPartial_day(parType);
        request.setSetting_id(rs.getInt("request_type"));
        r.setRequest_type(request);
        ar.setReport(r);
        return ar;
    }
}
