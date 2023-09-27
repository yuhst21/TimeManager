package com.bul.FMSTimeManager.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Settings {

    private int setting_id;
    private int type_id;
    private String setting_title;
}
