package com.bul.FMSTimeManager.daos.RowMapper;

import com.bul.FMSTimeManager.models.Request;
import com.bul.FMSTimeManager.models.Settings;
import com.bul.FMSTimeManager.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestRowMapper implements RowMapper<Request> {
    @Override
    public Request mapRow(ResultSet rs, int rowNum) throws SQLException {
        Request req = new Request();
        //new Setting
        Settings setReq = new Settings();
        Settings setParital = new Settings();
        Settings setReason = new Settings();
        //new User
        User uApprover = new User();
        User uSupervisor = new User();
        User uEmployee = new User();

        req.setRequest_id(rs.getInt("request_id"));
        //setReq
        setReq.setSetting_id(rs.getInt("request_type"));
        setReq.setSetting_title(rs.getString("req_type"));
        req.setRequest_type(setReq);
        req.setStart_date(rs.getDate("start_date"));
        req.setEnd_date(rs.getDate("end_date"));
        //setPar
        setParital.setSetting_id(rs.getInt("partital_day"));
        setParital.setSetting_title(rs.getString("req_partital"));
        req.setPartital_day(setParital);
        //setReason
        setReason.setSetting_id(rs.getInt("reason"));
        setReason.setSetting_title(rs.getString("req_reason"));
        req.setReason(setReason);
        req.setDetail_reason(rs.getString("detail_reason"));
        req.setExpected_approve(rs.getDate("expected_approve"));
        req.setDuration(rs.getInt("duration"));
        //set uApprover
        uApprover.setUser_id(rs.getInt("approver"));
        uApprover.setFull_name(rs.getString("req_approver"));
        req.setApprover(uApprover);
        //set uSupervisor
        uSupervisor.setUser_id(rs.getInt("supervisor"));
        uSupervisor.setFull_name(rs.getString("req_suppervisor"));
        req.setSupervisor(uSupervisor);
        //set uEmployee
        uEmployee.setUser_id(rs.getInt("employee_id"));
        uEmployee.setFull_name(rs.getString("req_employee"));
        req.setEmployee(uEmployee);
        req.setInform_to(rs.getString("inform_to"));
        req.setTime(rs.getString("time"));
        req.setStatus(rs.getInt("status"));
        return req;
    }
}
