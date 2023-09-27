package com.bul.FMSTimeManager.daos;

import com.bul.FMSTimeManager.daos.RowMapper.MyAbnormalCaseRowMapper;
import com.bul.FMSTimeManager.daos.RowMapper.MyEarlyLeaveAndLateComingRowMapper;
import com.bul.FMSTimeManager.daos.RowMapper.MyWorkingTimeRowMapper;
import com.bul.FMSTimeManager.daos.RowMapper.RequestRowMapper;
import com.bul.FMSTimeManager.models.AbnormalReport;
import com.bul.FMSTimeManager.models.RequestTimeReport;
import com.bul.FMSTimeManager.models.WorkingTimeReport;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReportRepository extends ContextRepository<ReportRepository> {
    @Override
    public List<ReportRepository> list() {
        return null;
    }

    @Override
    public List<ReportRepository> list(String identity) {
        return null;
    }

    @Override
    public ReportRepository get(ReportRepository entity) {
        return null;
    }

    public int count(int emp_id, Date start_date, Date end_date) {
        String sql = "select count(*) as total from report where (1=1) and employee_id = ?";
        Integer count = 1;
        HashMap<Integer, Object> params = new HashMap<>();
        if (start_date != null) {
            count++;
            sql += " and date >= ? \n";
            params.put(count, start_date);
        }
        if (end_date != null) {
            count++;
            sql += " and date <= ? \n";
            params.put(count, end_date);
        }
        return jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, emp_id);
                for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                    Integer key = entry.getKey();
                    Object val = entry.getValue();
                    ps.setObject(key, val);
                }
            }
        }, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getInt("total");
                }
                return null;

            }
        });

    }

    public List<RequestTimeReport> paggerAndfilter(Integer page_size, Integer page_index, int emp_id, Date start_date, Date end_date) {
        String sql = "SELECT r.*,rtr.* FROM report r inner join request_time_report rtr\n" +
                "on r.report_id = rtr.request_time_report_id\n" +
                "where r.employee_id = ? ";
        Integer count = 1;
        HashMap<Integer, Object> params = new HashMap<>();
        if (start_date != null) {
            count++;
            sql += " and r.date >= ? \n";
            params.put(count, start_date);
        }
        if (end_date != null) {
            count++;
            sql += " and r.date <= ? \n";
            params.put(count, end_date);
        }
        sql += "order by r.report_id asc limit ?,?";
        count++;
        params.put(count, page_size * (page_index - 1));
        count++;
        params.put(count, page_size);
        return jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, emp_id);
                for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                    Integer key = entry.getKey();
                    Object val = entry.getValue();
                    ps.setObject(key, val);
                }
            }
        }, new MyEarlyLeaveAndLateComingRowMapper());
    }

    public int countComingLate(int emp_id) {
        String sql = "Select count(is_coming_late) from request_time_report rtr\n" +
                "inner join report r on r.report_id = rtr.request_time_report_id\n" +
                "where r.employee_id = ? and rtr.is_coming_late = 1";
        return jdbcTemplate.queryForObject(sql, Integer.class, emp_id);
    }

    public int counEarlyLeave(int emp_id) {
        String sql = "Select count(is_early_leave) from request_time_report rtr\n" +
                "inner join report r on r.report_id = rtr.request_time_report_id\n" +
                "where r.employee_id = ? and rtr.is_early_leave = 1";
        return jdbcTemplate.queryForObject(sql, Integer.class, emp_id);
    }

    public List<RequestTimeReport> listByEmp(int emp_id) {
        String sql = "SELECT r.*,rtr.* FROM report r inner join request_time_report rtr\n" +
                "on r.report_id = rtr.request_time_report_id\n" +
                "where r.employee_id = ? ";
        return jdbcTemplate.query(sql, new MyEarlyLeaveAndLateComingRowMapper(), emp_id);
    }

    public List<WorkingTimeReport> paggerAndfilterWorkingTime(Integer page_size, Integer page_index, int emp_id, Date start_date, Date end_date) {
        String sql = "SELECT r.*,rtr.* FROM report r inner join working_time_report rtr\n" +
                "                on r.report_id = rtr.working_time_report_id\n" +
                "                where r.employee_id = ? ";
        Integer count = 1;
        HashMap<Integer, Object> params = new HashMap<>();
        if (start_date != null) {
            count++;
            sql += " and r.date >= ? \n";
            params.put(count, start_date);
        }
        if (end_date != null) {
            count++;
            sql += " and r.date <= ? \n";
            params.put(count, end_date);
        }
        sql += "order by r.report_id asc limit ?,?";
        count++;
        params.put(count, page_size * (page_index - 1));
        count++;
        params.put(count, page_size);
        return jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, emp_id);
                for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                    Integer key = entry.getKey();
                    Object val = entry.getValue();
                    ps.setObject(key, val);
                }
            }
        }, new MyWorkingTimeRowMapper());
    }

    public List<WorkingTimeReport> listByEmpWorking(int emp_id) {
        String sql = "SELECT r.*,rtr.* FROM report r inner join working_time_report rtr\n" +
                "                on r.report_id = rtr.working_time_report_id\n" +
                "                where r.employee_id = ?";
        return jdbcTemplate.query(sql, new MyWorkingTimeRowMapper(), emp_id);
    }

    public List<AbnormalReport> paggerAndfilterAbnormal(Integer page_size, Integer page_index, int emp_id, Date start_date, Date end_date, int leave_without_request, int working_on_approved) {
        String sql = "SELECT ar.*,r.*,par_day.setting_title as partial_day_title,ab_type.setting_title as abnormal_type_title,\n" +
                "par_day.setting_id as partial_day_set ,ab_type.setting_id as abnormal_type_set\n" +
                "FROM abnormal_report ar inner join\n" +
                "report r on ar.adnormal_id = r.report_id\n" +
                "inner join settings ab_type on ar.abnormal_type = ab_type.setting_id\n" +
                "inner join settings par_day on ar.partial_day = par_day.setting_id\n" +
                "where r.employee_id = ? ";
        Integer count = 1;
        HashMap<Integer, Object> params = new HashMap<>();
        if (start_date != null) {
            count++;
            sql += " and r.date >= ? \n";
            params.put(count, start_date);
        }
        if (end_date != null) {
            count++;
            sql += " and r.date <= ? \n";
            params.put(count, end_date);
        }
        if (leave_without_request != -1 && working_on_approved != -1) {
            count++;
            sql += " and ar.abnormal_type = ? \n";
            params.put(count, leave_without_request);
            count++;
            sql += " or ar.abnormal_type = ? \n";
            params.put(count, working_on_approved);
        } else if (leave_without_request != -1 && working_on_approved == -1) {
            count++;
            sql += " and ar.abnormal_type = ? \n";
            params.put(count, leave_without_request);
        } else if (working_on_approved != -1 && leave_without_request == -1) {
            count++;
            sql += " and ar.abnormal_type = ? \n";
            params.put(count, working_on_approved);
        }
        sql += "order by r.report_id asc limit ?,?";
        count++;
        params.put(count, page_size * (page_index - 1));
        count++;
        params.put(count, page_size);
        return jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, emp_id);
                for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                    Integer key = entry.getKey();
                    Object val = entry.getValue();
                    ps.setObject(key, val);
                }
            }
        }, new MyAbnormalCaseRowMapper());
    }

    public int countAbnormal(int emp_id, Date start_date, Date end_date, int leave_without_request, int working_on_approved) {
        String sql = "SELECT count(*) as total\n" +
                "FROM abnormal_report ar inner join\n" +
                "report r on ar.adnormal_id = r.report_id\n" +
                "inner join settings ab_type on ar.abnormal_type = ab_type.setting_id\n" +
                "inner join settings par_day on ar.partial_day = par_day.setting_id\n" +
                "where r.employee_id = ?";
        Integer count = 1;
        HashMap<Integer, Object> params = new HashMap<>();
        if (start_date != null) {
            count++;
            sql += " and date >= ? \n";
            params.put(count, start_date);
        }
        if (end_date != null) {
            count++;
            sql += " and date <= ? \n";
            params.put(count, end_date);
        }
        if (leave_without_request != -1 && working_on_approved != -1) {
            count++;
            sql += " and ar.abnormal_type = ? \n";
            params.put(count, leave_without_request);
            count++;
            sql += " or ar.abnormal_type = ? \n";
            params.put(count, working_on_approved);
        } else if (leave_without_request != -1 && working_on_approved == -1) {
            count++;
            sql += " and ar.abnormal_type = ? \n";
            params.put(count, leave_without_request);
        } else if (working_on_approved != -1 && leave_without_request == -1) {
            count++;
            sql += " and ar.abnormal_type = ? \n";
            params.put(count, working_on_approved);
        }
        return jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, emp_id);
                for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                    Integer key = entry.getKey();
                    Object val = entry.getValue();
                    ps.setObject(key, val);
                }
            }
        }, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getInt("total");
                }
                return null;

            }
        });

    }


    @Override
    public int insert(ReportRepository entity) {
        return 0;
    }


    @Override
    public int delete(ReportRepository entity) {
        return 0;
    }

    @Override
    public int update(ReportRepository entity) {
        return 0;
    }


}
