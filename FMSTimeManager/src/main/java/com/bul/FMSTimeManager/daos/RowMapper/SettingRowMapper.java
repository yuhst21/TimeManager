package com.bul.FMSTimeManager.daos.RowMapper;

import com.bul.FMSTimeManager.models.Settings;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingRowMapper implements RowMapper<Settings> {
    @Override
    public Settings mapRow(ResultSet rs, int rowNum) throws SQLException {
        Settings settings = new Settings();
        settings.setSetting_id(rs.getInt("setting_id"));
        settings.setType_id(rs.getInt("type_id"));
        settings.setSetting_title(rs.getString("setting_title"));
        return settings;
    }
}
