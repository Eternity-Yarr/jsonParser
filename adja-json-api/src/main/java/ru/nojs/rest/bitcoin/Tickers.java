package ru.nojs.rest.bitcoin;

import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonRawValue;
import org.codehaus.jackson.annotate.JsonUnwrapped;

import java.util.HashMap;
import java.util.Map;

public class Tickers {
    Map<String, String> tickers = new HashMap<>();

    @JsonAnySetter public void add(String key, String value) {
        tickers.put(key, value);
    }
    public Map<String, String> tickers() {
        return tickers;
    }
}
