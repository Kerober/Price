package com.plekhanov.service;

import com.plekhanov.dto.Price;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PriceService {
    public static boolean isSuitablePrice(Price availPrice, Price newPrice ) {
        return newPrice.getProductCode() == availPrice.getProductCode() &&
                newPrice.getNumber() == availPrice.getNumber() &&
                newPrice.getDepart() == availPrice.getDepart();
    }

    public static boolean isDateCross(Price availPrice, Price newPrice) {
        return (newPrice.getEnd().after(availPrice.getBegin()) && newPrice.getEnd().before(availPrice.getEnd())) ||
                (newPrice.getBegin().after(availPrice.getBegin()) && newPrice.getBegin().before(availPrice.getEnd())) ||
                (availPrice.getBegin().after(newPrice.getBegin()) && availPrice.getEnd().before(newPrice.getEnd()));
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
                if (PriceService.isSuitablePrice(availPrice, newPrice)) {
                    if (PriceService.isDateCross(availPrice, newPrice)) { //если периоды пересекаются
                        if (newPrice.getValue() == availPrice.getValue()) { //если новая цена равна текущей
                            availPrice.setBegin(minDate(availPrice.getBegin(), newPrice.getBegin()));
                            availPrice.setEnd(maxDate(availPrice.getEnd(), newPrice.getEnd()));
                            availablePrices = availablePrices.stream()
                                    .sorted(Comparator.comparing(Price::getProductCode).thenComparing(Price::getDepart)
                                            .thenComparing(Price::getNumber)).collect(Collectors.toList());
                            while (availableIter.hasNext() && PriceService.isSuitablePrice(nextPrice = availableIter.next(), availPrice)) {
                                if (PriceService.isDateCross(nextPrice, availPrice)) {
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
                                            , newPrice.getEnd(), availPrice.getEnd(), availPrice.getValue());
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
            if (count == 0) {
                availablePrices.add(newPrice);
            }
            availablePrices = availablePrices.stream()
                    .filter(price -> (price.getEnd().after(price.getBegin())))
                    .sorted(Comparator.comparing(Price::getProductCode).thenComparing(Price::getDepart).thenComparing(Price::getNumber))
                    .distinct()
                    .collect(Collectors.toList());
        }

        return availablePrices;
    }
}
