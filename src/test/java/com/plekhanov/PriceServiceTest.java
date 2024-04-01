package com.plekhanov;


import com.plekhanov.dto.Price;
import com.plekhanov.service.PriceService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PriceServiceTest {

    final private DateFormat DATEFORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    Date parseDate(String date){
        Date pDate = null;
        try {
           pDate  = DATEFORMAT.parse(date);
        } catch (ParseException e) {
            System.out.println("Illegal Date format. Date is must dd.MM.yyyy HH:mm:ss");
        }
        return pDate;
    }

    @Test
    void newPriceIsIncludedOldTest() {
        List<Price> prices = new ArrayList<>();
        prices.add(new Price("122856", 1, 1, parseDate("01.01.2013 00:00:00"), parseDate("31.01.2013 23:59:59"), 50));
        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, parseDate("20.01.2013 00:00:00"), parseDate("23.01.2013 23:59:59"), 60));
        List<Price> resultPrices = new ArrayList<>();
        resultPrices.add(new Price("122856", 1, 1, parseDate("01.01.2013 00:00:00"), parseDate("20.01.2013 00:00:00"), 50));
        resultPrices.add(new Price("122856", 1, 1, parseDate("20.01.2013 00:00:00"), parseDate("23.01.2013 23:59:59"), 60));
        resultPrices.add(new Price("122856", 1, 1, parseDate("23.01.2013 23:59:59"), parseDate("31.01.2013 23:59:59"), 50));

        Assertions.assertThat(resultPrices).containsExactlyInAnyOrderElementsOf(PriceService.joinPrice(prices, newPrices));
    }
    @Test
    void newPriceIsCrossOldTest() {
        List<Price> prices = new ArrayList<>();
        prices.add(new Price("122856", 1, 1, parseDate("01.01.2013 00:00:00"), parseDate("31.01.2013 23:59:59"), 100));
        prices.add(new Price("122856", 1, 1, parseDate("31.01.2013 23:59:59"), parseDate("20.02.2013 23:59:59"), 120));
        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, parseDate("20.01.2013 00:00:00"), parseDate("13.02.2013 23:59:59"), 110));
        List<Price> resultPrices = new ArrayList<>();
        resultPrices.add(new Price("122856", 1, 1, parseDate("01.01.2013 00:00:00"), parseDate("20.01.2013 00:00:00"), 100));
        resultPrices.add(new Price("122856", 1, 1, parseDate("20.01.2013 00:00:00"), parseDate("13.02.2013 23:59:59"), 110));
        resultPrices.add(new Price("122856", 1, 1, parseDate("13.02.2013 23:59:59"), parseDate("20.02.2013 23:59:59"), 120));

        Assertions.assertThat(resultPrices).containsExactlyInAnyOrderElementsOf(PriceService.joinPrice(prices, newPrices));
    }
    @Test
    void newPriceIsClosesOldTest() {
        List<Price> prices = new ArrayList<>();
        prices.add(new Price("122856", 1, 1, parseDate("01.01.2013 00:00:00"), parseDate("10.01.2013 23:59:59"), 80));
        prices.add(new Price("122856", 1, 1, parseDate("10.01.2013 23:59:59"), parseDate("30.01.2013 23:59:59"), 87));
        prices.add(new Price("122856", 1, 1, parseDate("30.01.2013 23:59:59"), parseDate("20.02.2013 23:59:59"), 90));
        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, parseDate("9.01.2013 00:00:00"), parseDate("20.01.2013 23:59:59"), 80));
        newPrices.add(new Price("122856", 1, 1, parseDate("20.01.2013 00:00:00"), parseDate("10.02.2013 23:59:59"), 85));
        List<Price> resultPrices = new ArrayList<>();
        resultPrices.add(new Price("122856", 1, 1, parseDate("01.01.2013 00:00:00"), parseDate("20.01.2013 00:00:00"), 80));
        resultPrices.add(new Price("122856", 1, 1, parseDate("20.01.2013 00:00:00"), parseDate("10.02.2013 23:59:59"), 85));
        resultPrices.add(new Price("122856", 1, 1, parseDate("10.02.2013 23:59:59"), parseDate("20.02.2013 23:59:59"), 90));

        Assertions.assertThat(resultPrices).containsExactlyInAnyOrderElementsOf(PriceService.joinPrice(prices, newPrices));
    }
    @Test
    void newPriceHasDuplicateTest() {
        List<Price> prices = new ArrayList<>();
        prices.add(new Price("122856", 1, 1, parseDate("01.01.2013 00:00:00"), parseDate("10.01.2013 23:59:59"), 80));
        prices.add(new Price("122856", 1, 1, parseDate("10.01.2013 23:59:59"), parseDate("30.01.2013 23:59:59"), 87));
        prices.add(new Price("122856", 1, 1, parseDate("30.01.2013 23:59:59"), parseDate("20.02.2013 23:59:59"), 90));
        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, parseDate("9.01.2013 00:00:00"), parseDate("20.01.2013 23:59:59"), 85));
        newPrices.add(new Price("122856", 1, 1, parseDate("9.01.2013 00:00:00"), parseDate("20.01.2013 23:59:59"), 85));
        List<Price> resultPrices = new ArrayList<>();
        resultPrices.add(new Price("122856", 1, 1, parseDate("01.01.2013 00:00:00"), parseDate("9.01.2013 00:00:00"), 80));
        resultPrices.add(new Price("122856", 1, 1, parseDate("9.01.2013 00:00:00"), parseDate("20.01.2013 23:59:59"), 85));
        resultPrices.add(new Price("122856", 1, 1, parseDate("20.01.2013 23:59:59"), parseDate("30.01.2013 23:59:59"), 87));
        resultPrices.add(new Price("122856", 1, 1, parseDate("30.01.2013 23:59:59"), parseDate("20.02.2013 23:59:59"), 90));

        Assertions.assertThat(resultPrices).containsExactlyInAnyOrderElementsOf(PriceService.joinPrice(prices, newPrices));
    }

    @Test
    void newPricesIsSuffleTest() {
        List<Price> prices = new ArrayList<>();
        prices.add(new Price("122856", 1, 1, parseDate("01.01.2013 00:00:00"), parseDate("31.01.2013 23:59:59"), 110000));
        prices.add(new Price("122856", 2, 1, parseDate("10.01.2013 00:00:00"), parseDate("20.01.2013 23:59:59"), 99000));
        prices.add(new Price("6654", 1, 2, parseDate("01.01.2013 00:00:00"), parseDate("31.01.2013 00:00:00"), 5000));
        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("6654", 1, 2, parseDate("12.01.2013 00:00:00"), parseDate("13.01.2013 00:00:00"), 4000));
        newPrices.add(new Price("122856", 2, 1, parseDate("15.01.2013 00:00:00"), parseDate("25.01.2013 23:59:59"), 92000));
        newPrices.add(new Price("122856", 1, 1, parseDate("20.01.2013 00:00:00"), parseDate("20.02.2013 23:59:59"), 110000));
        List<Price> resultPrices = new ArrayList<>();
        resultPrices.add(new Price("122856", 1, 1, parseDate("01.01.2013 00:00:00"), parseDate("20.02.2013 23:59:59"), 110000));
        resultPrices.add(new Price("122856", 2, 1, parseDate("10.01.2013 00:00:00"), parseDate("15.01.2013 00:00:00"), 99000));
        resultPrices.add(new Price("122856", 2, 1, parseDate("15.01.2013 00:00:00"), parseDate("25.01.2013 23:59:59"), 92000));
        resultPrices.add(new Price("6654", 1, 2, parseDate("01.01.2013 00:00:00"), parseDate("12.01.2013 00:00:00"), 5000));
        resultPrices.add(new Price("6654", 1, 2, parseDate("12.01.2013 00:00:00"), parseDate("13.01.2013 00:00:00"), 4000));
        resultPrices.add(new Price("6654", 1, 2, parseDate("13.01.2013 00:00:00"), parseDate("31.01.2013 00:00:00"), 5000));

        Assertions.assertThat(resultPrices).containsExactlyInAnyOrderElementsOf(PriceService.joinPrice(prices, newPrices));
    }
    @Test
    void newPriceIsNotIncludedTest() {
        List<Price> prices = new ArrayList<>();
        prices.add(new Price("122856", 1, 1, parseDate("01.01.2013 00:00:00"), parseDate("31.01.2013 23:59:59"), 100));
        prices.add(new Price("122856", 1, 1, parseDate("31.01.2013 23:59:59"), parseDate("20.02.2013 23:59:59"), 120));
        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1, parseDate("22.02.2013 00:00:00"), parseDate("28.02.2013 23:59:59"), 110));
        List<Price> resultPrices = new ArrayList<>();
        resultPrices.add(new Price("122856", 1, 1, parseDate("01.01.2013 00:00:00"),parseDate( "31.01.2013 23:59:59"), 100));
        resultPrices.add(new Price("122856", 1, 1, parseDate("31.01.2013 23:59:59"),parseDate( "20.02.2013 23:59:59"), 120));
        resultPrices.add(new Price("122856", 1, 1, parseDate("22.02.2013 00:00:00"), parseDate("28.02.2013 23:59:59"), 110));

        Assertions.assertThat(resultPrices).containsExactlyInAnyOrderElementsOf(PriceService.joinPrice(prices, newPrices));
    }
    @Test
    void newPriceIsEmptyTest() {
        List<Price> prices = new ArrayList<>();
        prices.add(new Price("122856", 1, 1, parseDate("01.01.2013 00:00:00"), parseDate("31.01.2013 23:59:59"), 100));
        prices.add(new Price("122856", 1, 1, parseDate("31.01.2013 23:59:59"), parseDate("20.02.2013 23:59:59"), 120));
        List<Price> newPrices = new ArrayList<>();
        List<Price> resultPrices = new ArrayList<>();
        resultPrices.add(new Price("122856", 1, 1, parseDate("01.01.2013 00:00:00"), parseDate("31.01.2013 23:59:59"), 100));
        resultPrices.add(new Price("122856", 1, 1, parseDate("31.01.2013 23:59:59"), parseDate("20.02.2013 23:59:59"), 120));

        Assertions.assertThat(resultPrices).containsExactlyInAnyOrderElementsOf(PriceService.joinPrice(prices, newPrices));
    }
}
