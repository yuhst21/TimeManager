package com.bul.FMSTimeManager.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingTimeReport{
    private int working_time_report_id;
    private int number_of_entry;
    private int number_of_exit;
    private int request_status;
    private Report report;

}
