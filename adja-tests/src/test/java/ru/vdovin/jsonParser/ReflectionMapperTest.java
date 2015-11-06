package ru.vdovin.jsonParser;

import org.junit.Assert;
import org.junit.Test;
import ru.nojs.json.JSONElement;
import ru.vdovin.Currency;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Map;

public class ReflectionMapperTest {

    private static final ReflectionMapper mapper = new ReflectionMapper();

    @Test
    public void testFieldNameOf() throws Exception {
        String nothingToDo = "noop";
        Assert.assertEquals("nothing to do", nothingToDo, mapper.fieldNameOf(nothingToDo));

        String idempotent = "alreadyCamelCased";
        Assert.assertEquals("shouldn't change nothing", idempotent, mapper.fieldNameOf(idempotent));
        Assert.assertEquals("get prefixed", "getAlreadyCamelCased", mapper.getterNameOf(idempotent));

        // Bonus level
        // In reality no one(i think) but spring handles this kind of nonsense
        String snakeCased = "i_am_snake_cased";
        Assert.assertEquals("We can handle snakes", "iAmSnakeCased", mapper.fieldNameOf(snakeCased));
        Assert.assertEquals("get is prefixed", "getIAmSnakeCased", mapper.getterNameOf(snakeCased));
    }

    @Test
    public void testMapper() {
        String simpleJson =
                "{\n" +
                "  \"a\": \"a\",\n" +
                "  \"b\": 2\n" +
                "}";
        JSONElement je = new ImplementedJsonParser().parse(new StringReader(simpleJson));
        SimplePOJO sp = mapper.createObject(je, SimplePOJO.class);
    }


    @Test(expected = NoSuchFieldException.class)
    public void testIllegalArgument() {
        String noSuchField =
                "{\n" +
                "  \"a\": \"a\",\n" +
                "  \"b\": 2,\n" +
                "  \"c\": \"c\"\n" +
                "}";
        JSONElement je = new ImplementedJsonParser().parse(new StringReader(noSuchField));
        SimplePOJO sp = mapper.createObject(je, SimplePOJO.class);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testTest() {
        try {
            throw new NoSuchFieldException("test");
        }
        catch (Exception e) {
            throw new RuntimeException("test", e);
        }

    }

    static class SimplePOJO {
        String a;
        int b;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

    }

    @Test //Bonus level: hard
    public void testBigPOJOMapping() throws Exception {
        String paymentInfo =
                "{\n" +
                "  \"id\": 1254817343741,\n" +
                "  \"toAccount\" : \"R12334\",\n" +
                "  \"fromAccount\": \"R33212\",\n" +
                "  \"currency\": \"RUB\",\n" +
                "  \"fromAmount\": 100.0,\n" +
                "  \"toAmount\": 95.0,\n" +
                "  \"date\": 1446361200000\n" +
                "}";
        JSONElement je = new ImplementedJsonParser().parse(new StringReader(paymentInfo));
        PaymentInfo pi = mapper.createObject(je, PaymentInfo.class);
        Assert.assertEquals("ids matches", 1254817343741L, pi.getId());
        Assert.assertEquals("to account matches", "R12334", pi.getToAccount());
        Assert.assertEquals("from account matches", "R33212", pi.getFromAccount());
        Assert.assertEquals("currency set correctly", Currency.RUB, pi.getCurrency());
        Assert.assertEquals("from amount matches", new BigDecimal(100.0), pi.getFromAmount());
        Assert.assertEquals("to amount matches", new BigDecimal(95.0), pi.getToAmount());
        Assert.assertEquals("date parsed correctly", 1446361200000L, pi.getDate());
    }

    @Test //Bonus level: hardcore
    public void testBigPOJOMapping2() throws Exception {
        String paymentInfo =
                "{\n" +
                "  \"id\": 1254817343741,\n" +
                "  \"toAccount\" : \"R12334\",\n" +
                "  \"fromAccount\": \"R33212\",\n" +
                "  \"currency\": \"RUB\",\n" +
                "  \"fromAmount\": 100.0,\n" +
                "  \"toAmount\": 95.0,\n" +
                "  \"date\": 1446361200000,\n" +
                "  \"extras\": {\n" +
                "    \"a\": \"b\",\n" +
                "    \"c\": \"d\"\n" +
                "  }\n" +
                "}";
        JSONElement je = new ImplementedJsonParser().parse(new StringReader(paymentInfo));
        PaymentInfo pi = mapper.createObject(je, PaymentInfo.class);
        Assert.assertEquals("ids matches", 1254817343741L, pi.getId());
        Assert.assertEquals("to account matches", "R12334", pi.getToAccount());
        Assert.assertEquals("from account matches", "R33212", pi.getFromAccount());
        Assert.assertEquals("currency set correctly", Currency.RUB, pi.getCurrency());
        Assert.assertEquals("from amount matches", new BigDecimal(100.0), pi.getFromAmount());
        Assert.assertEquals("to amount matches", new BigDecimal(95.0), pi.getToAmount());
        Assert.assertEquals("date parsed correctly", 1446361200000L, pi.getDate());
        Assert.assertEquals("extra 'a' parsed", "b", pi.getExtras().get("a"));
        Assert.assertEquals("extra 'b' parsed", "d", pi.getExtras().get("c"));
    }

    static class PaymentInfo {
        long id;
        String toAccount;
        String fromAccount;
        Currency currency;
        BigDecimal fromAmount;
        BigDecimal toAmount;
        long date;
        Map<String, String> extras;

        public String getToAccount() {
            return toAccount;
        }

        public void setToAccount(String toAccount) {
            this.toAccount = toAccount;
        }

        public String getFromAccount() {
            return fromAccount;
        }

        public void setFromAccount(String fromAccount) {
            this.fromAccount = fromAccount;
        }

        public Currency getCurrency() {
            return currency;
        }

        public void setCurrency(Currency currency) {
            this.currency = currency;
        }

        public BigDecimal getFromAmount() {
            return fromAmount;
        }

        public void setFromAmount(BigDecimal fromAmount) {
            this.fromAmount = fromAmount;
        }

        public BigDecimal getToAmount() {
            return toAmount;
        }

        public void setToAmount(BigDecimal toAmount) {
            this.toAmount = toAmount;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Map<String, String> getExtras() {
            return extras;
        }

        public void setExtras(Map<String, String> extras) {
            this.extras = extras;
        }
    }

}