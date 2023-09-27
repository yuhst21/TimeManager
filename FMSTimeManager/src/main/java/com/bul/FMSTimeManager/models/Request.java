package com.bul.FMSTimeManager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Request {
    private int request_id;

    private Settings request_type;
    private java.sql.Date start_date;
    private java.sql.Date end_date;
    private Settings partital_day;
    private Settings reason;
    private String detail_reason;
    private Date expected_approve;
    private int duration;
    private User approver;
    private User supervisor;
    private String inform_to;
    private String time;
    private int status;

    private User employee;

}
