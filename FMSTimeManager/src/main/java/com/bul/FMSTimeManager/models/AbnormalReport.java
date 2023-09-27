package com.bul.FMSTimeManager.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbnormalReport {
    private int adnormal_id;
    private Settings partial_day;
    private Settings abnormal_type;
    private Report report;

}
