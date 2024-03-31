package com.plekhanov;

import java.text.SimpleDateFormat;

import java.util.*;
import java.util.stream.Collectors;


public class Price {
    private long id;
    private String productCode;
    private int number;
    private int depart;
    private Date begin;
    private Date end;
    private long value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;

        if (number != price.number) return false;
        if (depart != price.depart) return false;
        if (value != price.value) return false;
        if (!productCode.equals(price.productCode)) return false;
        if (!begin.equals(price.begin)) return false;
        return end.equals(price.end);
    }

    @Override
    public int hashCode() {
        int result = productCode.hashCode();
        result = 31 * result + number;
        result = 31 * result + depart;
        result = 31 * result + begin.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + (int) (value ^ (value >>> 32));
        return result;
    }

    public Price(String productCode, int number, int depart, String begin, String end, long value) {
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        try {
            this.begin = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(begin);
            this.end = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(end);
        } catch (java.text.ParseException e) {
            System.out.println("Illegal Date format. Date is must dd.MM.yyyy HH:mm:ss");
        }

        this.value = value;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDepart() {
        return depart;
    }

    public void setDepart(int depart) {
        this.depart = depart;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Price{" +
                "productCode='" + productCode + '\'' +
                ", number=" + number +
                ", depart=" + depart +
                ", begin=" + begin +
                ", end=" + end +
                ", value=" + value +
                '}';
    }

    public boolean isSuitablePrice(Price price){
        return this.productCode == price.productCode &&
                this.number == price.number && this.depart == price.depart;
    }
    public boolean isDateCross(Price price){
        return (this.getEnd().after(price.getBegin()) && this.getEnd().before(price.getEnd())) ||
                (this.getBegin().after(price.getBegin()) && this.getBegin().before(price.getEnd())) ||
                (price.getBegin().after(this.getBegin()) && price.getEnd().before(this.getEnd()));
    }
    public static Date minDate(Date date1, Date date2) {
        return (date1.before(date2)) ? date1 : date2;
    }
    public static Date maxDate(Date date1, Date date2) {
        return (date1.after(date2)) ? date1 : date2;
    }

    public static List<Price> joinPrice(List<Price> availablePrices, List<Price> newPrices) {
        Iterator<Price> newIter = newPrices.iterator();

        while (newIter.hasNext()) {
            Price newPrice = newIter.next();
            Iterator<Price> availableIter = availablePrices.iterator();
            int count = 0;
            while (availableIter.hasNext()) {
                Price availPrice = availableIter.next();
                Price nextPrice = null;
                if (newPrice.isSuitablePrice(availPrice)) {
                    if (newPrice.isDateCross(availPrice)) { //если периоды пересекаются
                        if (newPrice.getValue() == availPrice.getValue()) { //если новая цена равна текущей
                            availPrice.setBegin(minDate(availPrice.getBegin(), newPrice.getBegin()));
                            availPrice.setEnd(maxDate(availPrice.getEnd(), newPrice.getEnd()));
                            availablePrices = availablePrices.stream()
                                    .sorted(Comparator.comparing(Price::getProductCode).thenComparing(Price::getDepart)
                                            .thenComparing(Price::getNumber)).collect(Collectors.toList());
                            while (availableIter.hasNext() && availPrice.isSuitablePrice(nextPrice = availableIter.next())){
                                if (availPrice.isDateCross(nextPrice)) {
                                    nextPrice.setBegin(availPrice.getEnd());
                                }
                            }
                            count++;
                            break;
                        } else { //если цены отличается
                            if (availPrice.getEnd().after(newPrice.getEnd())) { //если новая цена не выходит за конец текущей
                                if (availPrice.getBegin().before(newPrice.getBegin())) {
                                    availablePrices.add(newPrice);
                                    Price price = new Price(availPrice.getProductCode(), availPrice.getNumber(), availPrice.getDepart()
                                            ,"01.01.2013 00:00:00" ,"01.01.2013 00:00:00" , availPrice.getValue());
                                    price.setBegin(newPrice.getEnd());
                                    price.setEnd(availPrice.getEnd());
                                    availablePrices.add(price);
                                    availPrice.setEnd(newPrice.getBegin());
                                    count++;
                                    break;
                                } else {
                                    availablePrices.add(newPrice);
                                    availPrice.setBegin(newPrice.getEnd());
                                    count++;
                                    break;
                                }
                            } else {
                                availPrice.setEnd(newPrice.getBegin());
                            }
                        }
                    }
                }


            }
            if(count == 0) {
                availablePrices.add(newPrice);
            }
            availablePrices = availablePrices.stream().filter(price -> (price.getEnd().after(price.getBegin())))
                    .sorted(Comparator.comparing(Price::getProductCode).thenComparing(Price::getDepart).thenComparing(Price::getNumber))
                    .distinct()
                    .collect(Collectors.toList());
        }

        return availablePrices;
    }


}




