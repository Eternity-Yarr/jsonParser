package ru.vdovin.jsonParser;

import ru.nojs.json.JSONNull;

public final class MyJSONNull extends MyJSONElement implements JSONNull{

    public static final MyJSONNull INSTANCE = new MyJSONNull();

}
