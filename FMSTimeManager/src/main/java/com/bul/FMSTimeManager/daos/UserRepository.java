package com.bul.FMSTimeManager.daos;

import com.bul.FMSTimeManager.daos.RowMapper.UserRowMapper;
import com.bul.FMSTimeManager.models.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
public class UserRepository extends ContextRepository<User>{
    @Override
    public List<User> list() {
        return null;
    }

    @Override
    public List<User> list(String identity) {
        return null;
    }

    @Override
    public User get(User entity) {
        return null;
    }

    @Override
    public int insert(User entity) {
        return 0;
    }

    @Override
    public int delete(User entity) {
        return 0;
    }

    @Override
    public int update(User entity) {
        return 0;
    }

    public List<User> listUserByRole(int role){
        String sql = "SELECT u.*,s.setting_title,s.setting_id FROM user u inner join user_role ur\n" +
                "on u.user_id = ur.user_id\n" +
                "inner join settings s \n" +
                "on s.setting_id = ur.role_id \n" +
                "where ur.role_id = ?";
        return jdbcTemplate.query(sql,new UserRowMapper(),role);
    }
    public List<String> listUserNameByRequest(int user_id){
        List<String> userList = new ArrayList<>();
        String sql = "select distinct u.username from request r inner join user u \n" +
                "on r.employee_id = u.user_id\n" +
                "where r.approver = ? or r.supervisor = ?";
        return jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1,user_id);
                ps.setInt(2,user_id);
            }
        }, new ResultSetExtractor<List<String>>() {
            @Override
            public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    userList.add(rs.getString("username"));
                }
                return userList;
            }
        });
    }
    public List<String> listUserGmailByRole(){
        List<String> listEmail = new ArrayList<>();
        String sql = "SELECT u.email FROM user u inner join user_role ur\n" +
                "                on u.user_id = ur.user_id\n" +
                "                inner join settings s \n" +
                "                on s.setting_id = ur.role_id \n" +
                "                where ur.role_id = 41 or ur.role_id = 42";
        return jdbcTemplate.query(sql, new ResultSetExtractor<List<String>>() {
            @Override
            public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                     listEmail.add(  rs.getString("email"));
                }
                return listEmail;
            }
        });
    }
    public User check(String user_name,String pass){
        String sql = "SELECT u.*,s.setting_title,s.setting_id FROM user u inner join user_role ur\n" +
                "                on u.user_id = ur.user_id\n" +
                "                inner join settings s \n" +
                "                on s.setting_id = ur.role_id\n" +
                "                where (u.username = ?  or u.email = ? ) and u.password = ? \n" +
                "              ";
        return jdbcTemplate.queryForObject(sql,new UserRowMapper(),new Object[]{user_name,user_name,pass});

    }
    public User findUserByUserName(String user_name){
        String sql = "SELECT u.*,s.setting_title,s.setting_id FROM user u inner join user_role ur\n" +
                "                                on u.user_id = ur.user_id\n" +
                "                                inner join settings s \n" +
                "                               on s.setting_id = ur.role_id\n" +
                "                                where (u.username = ?   or u.email = ? )";
        return jdbcTemplate.queryForObject(sql,new UserRowMapper(),new Object[]{user_name,user_name});
    }


  /*  public List<String> getRole(){
        List<String> roleName = new ArrayList<>();
        String sql = "select distinct s.setting_title from user u inner join user_role ur \n" +
                "on u.user_id = ur.user_id inner join settings s \n" +
                "on ur.role_id = s.setting_id;";

        return jdbcTemplate.query(sql, new ResultSetExtractor<List<String>>() {
            @Override
            public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()){
                    roleName.add( rs.getString("setting_title"));
                }
                return roleName;
            }
        });
    }
    public boolean hasRole(String role){
        List<String> checkRole = getRole();
        for (String s: checkRole
             ) {
            if (s.equals(role)) return true;
        }
        return false;
    }*/

}
