package com.bul.FMSTimeManager.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    private int report_id;
    private Date date;
    private Time first_entry;
    private Time last_exit;
    private Time in_office_time;
    private Time in_office_working_time_frame;
    private Time in_working_area;
    private Settings request_type;

    private User employee;
}
