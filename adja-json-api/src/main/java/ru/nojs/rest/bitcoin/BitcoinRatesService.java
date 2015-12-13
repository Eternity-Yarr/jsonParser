package ru.nojs.rest.bitcoin;

//https://api.bitcoinaverage.com/ticker/global/
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


@Produces("application/json")
public interface BitcoinRatesService {

    @GET
    @Path("/ticker/global/")
    Tickers listAll();

    @GET
    @Path("/ticker/global/{tickerName}/")
    Ticker get(@PathParam("tickerName") String tickerName);
}
