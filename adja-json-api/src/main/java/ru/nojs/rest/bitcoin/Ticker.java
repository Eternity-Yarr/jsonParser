package ru.nojs.rest.bitcoin;


import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import java.math.BigDecimal;

/* {
  "ask": 1623.45,
  "bid": 1622.34,
  "last": 1623.03,
  "timestamp": "Sun, 13 Dec 2015 19:55:59 -0000",
  "volume_btc": 0.0,
  "volume_percent": 0.0
} */

public class Ticker {
    BigDecimal ask;
    BigDecimal bid;
    BigDecimal last;
    BigDecimal avg;
    String timestamp;
    double volumeBtc;
    double volumePercent;

    @JsonCreator
    public Ticker(
            @JsonProperty("ask") BigDecimal ask,
            @JsonProperty("bid") BigDecimal bid,
            @JsonProperty("last") BigDecimal last,
            @JsonProperty("timestamp") String timestamp,
            @JsonProperty("volume_btc") double volumeBtc,
            @JsonProperty("volume_percent") double volumePercent,
            @JsonProperty("24h_avg") BigDecimal avg
    ) {
        this.ask = ask;
        this.bid = bid;
        this.last = last;
        this.avg = avg;
        this.timestamp = timestamp;
        this.volumeBtc = volumeBtc;
        this.volumePercent = volumePercent;
    }
}
