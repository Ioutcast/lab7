package org.example.Worker;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.io.Serializable;

/**
 * класс адреса - элемент рабочего
 */
@ToString
public class Address implements Comparable<Address>, Serializable {
    @Getter
    private String street; //Поле не может быть null
    @Getter
    private String zipCode; //Поле может быть null
    @Getter
    private Location town; //Поле не может быть null

    /**
     * конструктор
     *
     * @param zipCode
     * @param street
     * @param town
     */
    public Address(String zipCode, String street, Location town) {
        setZipCode(zipCode);
        setStreet(street);
        setTown(town);
    }

    /**
     * сеттер улицы
     *
     * @param street
     */
    public void setStreet(@NonNull String street) {
        if (street.trim().equals("")) {
            throw new NullPointerException("street is marked NotNull but is null");
        }
        this.street = street;
    }

    /**
     * сеттер zipCode
     *
     * @param zipCode
     */
    public void setZipCode(@NonNull String zipCode) {
        if (zipCode.trim().equals("")) {
            throw new NullPointerException("zipCode is marked NotNull but is null");
        }
        this.zipCode = zipCode;
    }

    /**
     * сеттер города
     *
     * @param town
     */
    public void setTown(@NonNull Location town) {
        this.town = town;
    }

    /**
     * сравнение
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Address o) {
        if (this.zipCode.length() > o.getZipCode().length()) {
            return 1;
        } else if (this.zipCode.length() < o.getZipCode().length()) {
            return -1;
        } else return 0;
    }
}
