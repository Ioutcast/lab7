package org.example.Worker;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public enum Status implements Comparable<Status>, Serializable {
    FIRED("Уволен", 4),
    HIRED("Принят на работу", 3),
    RECOMMENDED_FOR_PROMOTION("Рекомендован для повышения", 2),
    REGULAR("Штатный сотрудник", 1);
    @Getter
    @Setter
    private String position;
    @Getter
    @Setter
    private int type;

   Status(String position, int type) {
      this.position = position;
      this.type = type;
   }


}
