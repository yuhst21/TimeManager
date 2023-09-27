package com.bul.FMSTimeManager.daos.RowMapper;

import com.bul.FMSTimeManager.models.Settings;
import com.bul.FMSTimeManager.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User u = new User();
        Settings s = new Settings();
        s.setSetting_id(rs.getInt("setting_id"));
        s.setSetting_title(rs.getString("setting_title"));
        u.setUser_id(rs.getInt("user_id"));
        u.setFull_name(rs.getString("fullname"));
        u.setUser_name(rs.getString("username"));
        u.setRole(s);
        return u;
    }
}
