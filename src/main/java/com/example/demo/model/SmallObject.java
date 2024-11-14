package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmallObject {

    private String ticker;
    private int gender;
    private int weight;
    private double bmi;
    private double col;
    private Date date;
}
