package org.example;

import org.example.Utils.DataBaseManager;
import org.junit.jupiter.api.Test;

public class Тest {
    @Test
    void name() {
        DataBaseManager a = new DataBaseManager();
        a.updateCollectionFromDataBase();
        System.out.println(a.getMapColletion());
    }
}
