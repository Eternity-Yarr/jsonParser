package ru.dkom.jsonParser;

import java.lang.reflect.Type;
import java.util.Objects;

public class PrimitiveHelper {
    private Integer i;
    private Float f;
    private String s;

    public PrimitiveHelper(String s) {
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            this.s = s;
        }
        ;
    }

    public Class getType() {
        if (i != null) {
            return new Integer(i).getClass();
        }
        return new String(s).getClass();
    }

    ;

    public Object getValue() {
        if (i != null) {
            return i;
        }
        return s;
    }
}
