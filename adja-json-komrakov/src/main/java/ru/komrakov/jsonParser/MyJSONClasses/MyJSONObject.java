package ru.komrakov.jsonParser.MyJSONClasses;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ru.komrakov.jsonParser.JSONObjectClass;

/**
 * Created by User on 08.11.2015.
 */
public class MyJSONObject extends JSONObjectClass{

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }


}
