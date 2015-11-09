package ru.vdovin.jsonParser;

import org.junit.Test;
import ru.nojs.json.JSONCreator;
import ru.nojs.json.JSONElement;
import ru.nojs.json.JSONField;
import ru.vdovin.Currency;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class ReflectionMapperTest {

    private static final ReflectionMapper mapper = new ReflectionMapper();

    @Test
    public void testFieldNameOf() throws Exception {
        String nothingToDo = "noop";
        assertEquals("nothing to do", nothingToDo, mapper.fieldNameOf(nothingToDo));

        String idempotent = "alreadyCamelCased";
        assertEquals("shouldn't change nothing", idempotent, mapper.fieldNameOf(idempotent));
        assertEquals("get prefixed", "getAlreadyCamelCased", mapper.getterNameOf(idempotent));

        // Bonus level
        // In reality no one(i think) but spring handles this kind of nonsense
        String snakeCased = "i_am_snake_cased";
        assertEquals("We can handle snakes", "iAmSnakeCased", mapper.fieldNameOf(snakeCased));
        assertEquals("get is prefixed", "getIAmSnakeCased", mapper.getterNameOf(snakeCased));
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
        assertEquals("ids matches", 1254817343741L, pi.getId());
        assertEquals("to account matches", "R12334", pi.getToAccount());
        assertEquals("from account matches", "R33212", pi.getFromAccount());
        assertEquals("currency set correctly", Currency.RUB, pi.getCurrency());
        assertEquals("from amount matches", new BigDecimal(100.0), pi.getFromAmount());
        assertEquals("to amount matches", new BigDecimal(95.0), pi.getToAmount());
        assertEquals("date parsed correctly", 1446361200000L, pi.getDate());
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
        assertEquals("ids matches", 1254817343741L, pi.getId());
        assertEquals("to account matches", "R12334", pi.getToAccount());
        assertEquals("from account matches", "R33212", pi.getFromAccount());
        assertEquals("currency set correctly", Currency.RUB, pi.getCurrency());
        assertEquals("from amount matches", new BigDecimal(100.0), pi.getFromAmount());
        assertEquals("to amount matches", new BigDecimal(95.0), pi.getToAmount());
        assertEquals("date parsed correctly", 1446361200000L, pi.getDate());
        assertEquals("extra 'a' parsed", "b", pi.getExtras().get("a"));
        assertEquals("extra 'b' parsed", "d", pi.getExtras().get("c"));
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

    @Test
    public void testMappingByCtor() {
        String json = "{\"R01\" : \"NIC\", \"X07\": \"+1 555 CALLME\", \"PRI\": -1}";
        JSONElement je = new ImplementedJsonParser().parse(new StringReader(json));
        ProprietaryPOJO info = mapper.createObject(je, ProprietaryPOJO.class);
        assertEquals("Contractor Id mapped right", "NIC", info.getContractorId());
        assertEquals("Extra info mapped too", "+1 555 CALLME", info.getExtraInfo());
        assertEquals("Obligatory field found", -1, info.getPriority().intValue());
    }

    @Test
    public void testMappingByCtorMissingObligatoryField() {
        String json = "{\"R01\" : \"NIC\", \"X07\": \"+1 555 CALLME\"}";
        JSONElement je = new ImplementedJsonParser().parse(new StringReader(json));
        ProprietaryPOJO info = mapper.createObject(je, ProprietaryPOJO.class);
        assertEquals("Contractor Id mapped right", "NIC", info.getContractorId());
        assertEquals("Extra info mapped too", "+1 555 CALLME", info.getExtraInfo());
        assertNull("Field must be empty", info.getPriority());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMappingByCtorMissingRequiredField() {
        String json = "{\"X07\": \"+1 555 CALLME\"}";
        JSONElement je = new ImplementedJsonParser().parse(new StringReader(json));
        ProprietaryPOJO info = mapper.createObject(je, ProprietaryPOJO.class);
        fail("Should've throw excpetion by now, missing required field!");
    }

    static class ProprietaryPOJO {
        private final String contractorId;
        private final String extraInfo;
        private final Integer priority;
        @JSONCreator
        public ProprietaryPOJO(
                @JSONField(name = "R01") String contractorId,
                @JSONField(name = "X07") String extraInfo,
                @JSONField(name = "PRI", required = false)  int priority
        ) {
            this.contractorId = contractorId;
            this.extraInfo = extraInfo;
            this.priority = priority;
        }

        public String getContractorId() {
            return contractorId;
        }

        public String getExtraInfo() {
            return extraInfo;
        }

        public Integer getPriority() {
            return priority;
        }
    }
}