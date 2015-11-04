package ru.nojs.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface JSONObject extends JSONElement {
    // Adds a member, which is a name-value pair, to self.
    void add(String property, JSONElement value);
    // Convenience method to add a boolean member.
    void addProperty(String property, Boolean value);
    // Convenience method to add a char member.
    void addProperty(String property, Number value);
    // Convenience method to add a primitive member.
    void addProperty(String property, String value);
    // Returns a set of members of this object.
    Set<Map.Entry<String,JSONElement>> entrySet();
    // Returns the member with the specified name.
    JSONElement get(String memberName);
    // Convenience method to get the specified member as a JsonArray.
    JSONArray getAsJsonArray(String memberName);
    // Convenience method to get the specified member as a JsonObject.
    JSONObject getAsJsonObject(String memberName);
    // Convenience method to get the specified member as a JsonPrimitive element.
    JSONPrimitive getAsJsonPrimitive(String memberName);
    // Convenience method to check if a member with the specified name is present in this object.
    boolean has(String memberName);
    // Removes the property from this JsonObject.
    JSONElement remove(String property);
}
