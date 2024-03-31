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
    private Date begin;
    private Date end;
    private final long value;
    final private DateFormat DATEFORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public Price(String productCode, int number, int depart, String begin, String end, long value) {
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        try {
            this.begin = DATEFORMAT.parse(begin);
            this.end = DATEFORMAT.parse(end);
        } catch (java.text.ParseException e) {
            System.out.println("Illegal Date format. Date is must dd.MM.yyyy HH:mm:ss");
        }

        this.value = value;
    }




}




