package com.bul.FMSTimeManager.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTimeReport{
    private int request_time_report_id;
    private int is_coming_late;
    private int is_early_leave;
    private Time late_time;
    private Time early_leave;
    private int request_status;

    private Report report;

}
