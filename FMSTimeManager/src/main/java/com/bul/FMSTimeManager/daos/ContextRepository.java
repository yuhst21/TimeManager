package com.bul.FMSTimeManager.daos;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;


import java.util.List;

public abstract class ContextRepository<T> {
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    public abstract List<T> list();

    public abstract List<T> list(String identity);

    public abstract T get(T entity);

    public abstract int insert(T entity);

    public abstract int delete(T entity);

    public abstract int update(T entity);
}
