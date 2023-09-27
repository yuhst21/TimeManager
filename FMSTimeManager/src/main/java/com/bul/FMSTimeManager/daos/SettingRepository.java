package com.bul.FMSTimeManager.daos;

import com.bul.FMSTimeManager.daos.RowMapper.SettingRowMapper;
import com.bul.FMSTimeManager.models.Settings;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class SettingRepository extends ContextRepository<Settings>{

    public List<Settings> listRequestType(){
        String sql = "SELECT s.* FROM settings s \n" +
                "where s.type_id = 1";
        return jdbcTemplate.query(sql,new SettingRowMapper());
    }
    public List<Settings> listPartialDay(){
        String sql = "SELECT s.* FROM settings s \n" +
                "where s.type_id = 4";
        return jdbcTemplate.query(sql,new SettingRowMapper());
    }
    public List<Settings> listReason(){
        String sql = "SELECT s.* FROM settings s \n" +
                "where s.type_id = 5";
        return jdbcTemplate.query(sql,new SettingRowMapper());
    }
    @Override
    public List<Settings> list() {
        return null;
    }

    @Override
    public List<Settings> list(String identity) {
        return null;
    }

    @Override
    public Settings get(Settings entity) {
        return null;
    }

    @Override
    public int insert(Settings entity) {
        return 0;
    }

    @Override
    public int delete(Settings entity) {
        return 0;
    }

    @Override
    public int update(Settings entity) {
        return 0;
    }


}
