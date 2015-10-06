package ru.nojs.json;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface JSONElement {
    // convenience method to get this element as a BigDecimal.
    BigDecimal getAsBigDecimal();
    //convenience method to get this element as a BigInteger.
    BigInteger getAsBigInteger();
    // convenience method to get this element as a boolean value.
    boolean	getAsBoolean();
    // convenience method to get this element as a primitive byte value.
    byte getAsByte();
    // convenience method to get this element as a primitive character value.
    char getAsCharacter();
    // convenience method to get this element as a primitive double value.
    double getAsDouble();
    // convenience method to get this element as a primitive float value.
    float getAsFloat();
    // convenience method to get this element as a primitive integer value.
    int	getAsInt();
    // convenience method to get this element as a JsonArray.
    JSONArray getAsJsonArray();
    // convenience method to get this element as a JsonNull.
    JSONNull getAsJsonNull();
    // convenience method to get this element as a JsonObject.
    JSONObject getAsJsonObject();
    // convenience method to get this element as a JsonPrimitive.
    JSONPrimitive getAsJsonPrimitive();
    // convenience method to get this element as a primitive long value.
    long getAsLong();
    // convenience method to get this element as a Number.
    Number getAsNumber();
    // convenience method to get this element as a primitive short value.
    short getAsShort();
    // convenience method to get this element as a string value.
    String getAsString();
    // provides check for verifying if this element is an array or not.
    boolean	isJsonArray();
    // provides check for verifying if this element represents a null value or not.
    boolean	isJsonNull();
    // provides check for verifying if this element is a Json object or not.
    boolean	isJsonObject();
    // provides check for verifying if this element is a primitive or not.
    boolean	isJsonPrimitive();
}
