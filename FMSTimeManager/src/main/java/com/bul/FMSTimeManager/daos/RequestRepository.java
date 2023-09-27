package com.bul.FMSTimeManager.daos;

import com.bul.FMSTimeManager.daos.RowMapper.RequestRowMapper;
import com.bul.FMSTimeManager.models.Request;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RequestRepository extends ContextRepository<Request> {
    @Override
    public List<Request> list() {

        return (List<Request>) jdbcTemplate.query("SELECT * FROM request ",
                new RequestRowMapper());
    }

    @Override
    public List<Request> list(String identity) {

        return (List<Request>) jdbcTemplate.query("SELECT * FROM request where request_id = ?",
                new RequestRowMapper(), identity);

    }

    public List<Request> listRequestByEmployeeId(int id) {
        String sql = "SELECT r.*,uApprover.fullname as req_approver\n" +
                ",uSupervisor.fullname as req_suppervisor,\n" +
                "sRequest.setting_title as req_type,\n" +
                "sPatital.setting_title as req_partital, \n" +
                "sReason.setting_title as req_reason \n" +
                "FROM request r \n" +
                "inner join user uApprover on r.approver = uApprover.user_id\n" +
                "inner join user uSupervisor on r.supervisor = uSupervisor.user_id\n" +
                "inner join settings sRequest on sRequest.setting_id = r.request_type\n" +
                "inner join settings sPatital on r.partital_day = sPatital.setting_id\n" +
                "inner join settings sReason on r.reason = sReason.setting_id\n" +
                "\nwhere r.employee_id = ?";
        return jdbcTemplate.query(sql, new RequestRowMapper(), id);
    }

    public List<Request> paggingAndfilter(Integer page_size, Integer page_index, int emp_id, Date start_date, Date end_date, int request_type, int status) {
        String sql = "SELECT r.*,uApprover.fullname as req_approver\n" +
                "                ,uSupervisor.fullname as req_suppervisor,\n" +
                "                sRequest.setting_title as req_type,\n" +
                "                sPatital.setting_title as req_partital, \n" +
                "                sReason.setting_title as req_reason ,\n" +
                "                uEmployee.fullname as req_employee\n" +
                "                FROM request r \n" +
                "                inner join user uApprover on r.approver = uApprover.user_id\n" +
                "                inner join user uSupervisor on r.supervisor = uSupervisor.user_id\n" +
                "                inner join user uEmployee on r.employee_id = uEmployee.user_id\n" +
                "                inner join settings sRequest on sRequest.setting_id = r.request_type\n" +
                "                inner join settings sPatital on r.partital_day = sPatital.setting_id\n" +
                "                inner join settings sReason on r.reason = sReason.setting_id \n" +
                "where r.employee_id = ? ";
        Integer count = 1;
        HashMap<Integer, Object> params = new HashMap<>();
        if (start_date != null) {
            count++;
            sql += " and start_date >= ? \n";
            params.put(count, start_date);
        }
        if (end_date != null) {
            count++;
            sql += " and end_date <= ? \n";
            params.put(count, end_date);
        }
        if (request_type != -1) {
            count++;
            sql += " and r.request_type = ? \n";
            params.put(count, request_type);
        }
        if (status != -1) {
            count++;
            sql += " and r.status = ? \n";
            params.put(count, status);
        }
        sql += "order by request_id asc limit ?,?";
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
        }, new RequestRowMapper());
    }

    public int count(int emp_id, Date start_date, Date end_date, int request_type, int status) {
        String sql = "select count(*) as total from request where (1=1) and employee_id = ?\n";
        Integer count = 1;
        HashMap<Integer, Object> params = new HashMap<>();
        if (start_date != null) {
            count++;
            sql += " and start_date >= ? \n";
            params.put(count, start_date);
        }
        if (end_date != null) {
            count++;
            sql += " and end_date <= ? \n";
            params.put(count, end_date);
        }
        if (request_type != -1) {
            count++;
            sql += " and request_type = ? \n";
            params.put(count, request_type);
        }
        if (status != -1) {
            count++;
            sql += " and status = ? \n";
            params.put(count, status);
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

    public int countReq() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM request", Integer.class);
    }

    @Override
    public Request get(Request entity) {
        return null;
    }

    @Override
    public int insert(Request entity) {
        return 0;
    }


    public Boolean insertVal(Request entity) {
        String sql = "INSERT INTO `request`\n" +
                "VALUES\n" +
                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,2);";
        return jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, entity.getRequest_id());
                ps.setInt(2, entity.getRequest_type().getSetting_id());
                ps.setDate(3, entity.getStart_date());
                ps.setDate(4, entity.getEnd_date());
                ps.setInt(5, entity.getPartital_day().getSetting_id());
                ps.setInt(6, entity.getReason().getSetting_id());
                ps.setString(7, entity.getDetail_reason());
                ps.setDate(8, entity.getExpected_approve());
                ps.setInt(9, entity.getDuration());
                ps.setInt(10, entity.getApprover().getUser_id());
                ps.setInt(11, entity.getSupervisor().getUser_id());
                ps.setString(12, entity.getInform_to());
                ps.setString(13, entity.getTime());
                ps.setInt(14, entity.getEmployee().getUser_id());
                ps.execute();
                return true;
            }
        });
    }

    @Override
    public int delete(Request entity) {
        return 0;
    }

    @Override
    public int update(Request entity) {
        return 0;
    }

    public List<Request> paggingAndfilterForHighRole(Integer page_size, Integer page_index, int emp_id, Date start_date, Date end_date, int request_type, int status, String requester) {
        String sql = "SELECT r.*,uApprover.fullname as req_approver\n" +
                "                ,uSupervisor.fullname as req_suppervisor,\n" +
                "                sRequest.setting_title as req_type,\n" +
                "                sPatital.setting_title as req_partital, \n" +
                "                sReason.setting_title as req_reason ,\n" +
                "                uEmployee.fullname as req_employee\n" +
                "                FROM request r \n" +
                "                inner join user uApprover on r.approver = uApprover.user_id\n" +
                "                inner join user uSupervisor on r.supervisor = uSupervisor.user_id\n" +
                "                inner join user uEmployee on r.employee_id = uEmployee.user_id\n" +
                "                inner join settings sRequest on sRequest.setting_id = r.request_type\n" +
                "                inner join settings sPatital on r.partital_day = sPatital.setting_id\n" +
                "                inner join settings sReason on r.reason = sReason.setting_id\n" +
                "                where (1=1) and (r.supervisor = ? or r.approver = ?) ";
        Integer count = 2;
        HashMap<Integer, Object> params = new HashMap<>();
        if (start_date != null) {
            count++;
            sql += " and start_date >= ? \n";
            params.put(count, start_date);
        }
        if (end_date != null) {
            count++;
            sql += " and end_date <= ? \n";
            params.put(count, end_date);
        }
        if (request_type != -1) {
            count++;
            sql += " and r.request_type = ? \n";
            params.put(count, request_type);
        }
        if (status != -1) {
            count++;
            sql += " and r.status = ? \n";
            params.put(count, status);
        }
        if (!requester.equals("")) {
            count++;
            sql += "and uEmployee.username like ? \n";
            params.put(count, "%" + requester + "%");
        }
        sql += "order by request_id asc limit ?,?";
        count++;
        params.put(count, page_size * (page_index - 1));
        count++;
        params.put(count, page_size);
        return jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, emp_id);
                ps.setInt(2, emp_id);
                for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                    Integer key = entry.getKey();
                    Object val = entry.getValue();
                    ps.setObject(key, val);
                }
            }
        }, new RequestRowMapper());
    }

    public int countForHigerRole(int emp_id, Date start_date, Date end_date, int request_type, int status, String requester) {
        String sql = "select count(*) as total from request \n" +
                "inner join user on request.employee_id = user.user_id\n" +
                "where (1=1) and (supervisor = ? or approver = ?)\n";
        Integer count = 2;
        HashMap<Integer, Object> params = new HashMap<>();
        if (start_date != null) {
            count++;
            sql += " and start_date >= ? \n";
            params.put(count, start_date);
        }
        if (end_date != null) {
            count++;
            sql += " and end_date <= ? \n";
            params.put(count, end_date);
        }
        if (request_type != -1) {
            count++;
            sql += " and request_type = ? \n";
            params.put(count, request_type);
        }
        if (status != -1) {
            count++;
            sql += " and status = ? \n";
            params.put(count, status);
        }
        if (!requester.equals("")) {
            count++;
            sql += "and user.username like ? \n";
            params.put(count, "%" + requester + "%");
        }
        return jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, emp_id);
                ps.setInt(2, emp_id);
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

    public void changeStatus(int status, int request_id) {
        String sql = "UPDATE `request`\n" +
                "SET\n" +
                "`status` = ?\n" +
                "WHERE `request_id` = ?;\n";
        this.jdbcTemplate.update(sql, status, request_id);
    }

    public List<Request> findRequestRemainByMonth(int emp_id, YearMonth yearMonth) {
        String sql = "SELECT r.*,uApprover.fullname as req_approver\n" +
                "                               ,uSupervisor.fullname as req_suppervisor,\n" +
                "                               sRequest.setting_title as req_type,\n" +
                "                               sPatital.setting_title as req_partital,\n" +
                "                               sReason.setting_title as req_reason ,\n" +
                "                               uEmployee.fullname as req_employee\n" +
                "                               FROM request r \n" +
                "                               inner join user uApprover on r.approver = uApprover.user_id\n" +
                "                               inner join user uSupervisor on r.supervisor = uSupervisor.user_id\n" +
                "                             inner join user uEmployee on r.employee_id = uEmployee.user_id\n" +
                "                              inner join settings sRequest on sRequest.setting_id = r.request_type\n" +
                "                                inner join settings sPatital on r.partital_day = sPatital.setting_id\n" +
                "                               inner join settings sReason on r.reason = sReason.setting_id\n" +
                "                               where DATE_FORMAT(r.start_date, '%Y-%m') = ? and employee_id = ? ";

        return this.jdbcTemplate.query(sql, new RequestRowMapper(), new Object[]{yearMonth.toString(), emp_id});
    }
}
