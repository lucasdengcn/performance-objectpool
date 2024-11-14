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
public class LargeObject {

    private String string1;
    private String string2;
    private String string3;

    private int int1;
    private int int2;
    private int int3;
    //
    private double double1;
    private double double2;
    private double double3;
    //
    private Date date1;
    private Date date2;
    //
    private boolean valid;
}
