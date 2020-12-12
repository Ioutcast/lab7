package org.example.Worker;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.io.Serializable;

/**
 * класс локации - элемент адреса
 */
@ToString
public class Location extends Worker implements Serializable {
    @Getter
    private Integer y;
    @Getter
    private Double x; //Поле не может быть null
    @Getter
    private String name; //Поле может быть null

    /**
     * конструктор
     *
     * @param x
     * @param y
     * @param name
     */
    public Location(Double x, int y, String name) {
        assert x > 0;
        assert y > 0;
        setX(x);
        setY(y);
        setName(name);
    }

    /**
     * сеттер x
     *
     * @param x
     */
    public void setX(@NonNull Double x) {
        this.x = x;
    }

    /**
     * сеттер y
     *
     * @param y
     */
    public void setY(int y) {

        this.y = y;
    }

    /**
     * сеттер имени локации
     *
     * @param name
     */
    public void setName(@NonNull String name) {
        if (name.trim().equals("")) {
            throw new NullPointerException("zipCode is marked NotNull but is null");
        }
        this.name = name;
    }

}
