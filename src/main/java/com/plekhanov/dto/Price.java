package com.plekhanov.dto;

import lombok.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Price {

    private long id;
    private final String productCode;
    private final int number;
    private final int depart;
    private final Date begin;
    private final Date end;
    private final long value;


    public Price(String productCode, int number, int depart, Date begin, Date end, long value) {
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }
}




