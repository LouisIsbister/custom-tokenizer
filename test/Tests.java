package test;

import src.rules.*;
import src.tokenizing.*;
import src.tokenizing.TokenizerEngine.TEngineMatch;

import static test.TestUtil.T;

import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;


public class Tests {

    @Test
    public void ruleToString() {
        TRule r1 = TRule.of("r1", TRulePriority.IMMEDIATE, true);
        TRule r2 = TRule.of("r2", TRulePriority.END,       true);
        TRule r3 = TRule.of("r3", TRulePriority.NATURAL,   false);

        Assertions.assertEquals("r1(3)", r1.toString());
        Assertions.assertEquals("r2(1)", r2.toString());
        Assertions.assertEquals("r3(2)", r3.toString());

        Assertions.assertEquals("[r1(3) true]", r1.fullToString());
        Assertions.assertEquals("[r2(1) true]", r2.fullToString());
        Assertions.assertEquals("[r3(2) false]", r3.fullToString());
    }

    @Test
    public void tokenizerToString() {
        TRule r1 = TRule.of("float", TRulePriority.IMMEDIATE, true);
        TRule r2 = TRule.of("hello", TRulePriority.NATURAL, false);

        Tokenizer ts = new Tokenizer()
            .addRule(r1)
            .startNextOrder()
            .addRule(r2);

        Assertions.assertEquals(
            "{1=[float(3)], 2=[hello(2)]}",
            ts.toString()
        );
    }



    @Test
    public void testRulePriorityA() {
        Tokenizer ts = TestUtil.generateTestTokenizerA();
        Assertions.assertEquals(
            "{1=[r1(3), r4(3), r6(3), r3(2), r2(1), r5(1)]}",
            ts.toString()
        );
    }

    @Test
    public void testRulePriorityB() {
        Tokenizer ts = TestUtil.generateTestTokenizerB();
        Assertions.assertEquals(
            "{1=[r1(3), r3(2)], 2=[r4(3), r2(1), r5(1)], 3=[r6(3)]}",
            ts.toString()
        );
    }



    @Test
    public void testTokenizationA() throws Exception {
        Tokenizer ts = TestUtil.generateTestTokenizerA();

        List<String> tokens = ts.stringTokens("r1r2r4r5r6r1r1r4r3");
        Assertions.assertEquals(
            List.of("r1", "r2", "r4", "r5", "r6", "r1", "r1", "r4", "r3"),
            tokens
        );
    }

    @Test
    public void testTokenizationB() throws Exception {
        Tokenizer ts = TestUtil.generateTestTokenizerB();
        List<String> tokens = ts.stringTokens("r1r2r4r5r6r1r1r4r3");
        Assertions.assertEquals(
            List.of("r1", "r2", "r1", "r1","r3"),
            tokens
        );
    }

    @Test
    public void testTokenizationC() throws Exception {
        String input = "  r1 r2   r3   r1 ";

        TRule r1 = TRule.of("r1", TRulePriority.NATURAL, true); 
        TRule r2 = TRule.of("r2", TRulePriority.NATURAL, false);
        TRule r3 = TRule.of("r3", TRulePriority.NATURAL, true);

        Tokenizer ts1 = new Tokenizer(true)
            .addMultipleRules(r1, r2, r3);
        Assertions.assertEquals(
            List.of("r1", "r3", "r1"),
            ts1.stringTokens(input)
        );

        Tokenizer ts2 = new Tokenizer(false)
            .addMultipleRules(r1, r2, r3);
        Assertions.assertThrows(
            TokenizerFailureException.class,
            () -> ts2.stringTokens(input)
        );

    }



    @Test
    public void testUsageVerification() throws Exception {
        String __test__ = """
            int a_b_c = -1;
            float xyz = 6.56;
            string str = \"hello world\";
            """;

        TRule[] baseRules = {
            TRule.of("(float|int|string)"),
            TRule.of(";|\\="),
            TRule.of("\\r\\n\\t", false)
        };
        TRule[] valueRules = {
            TRule.of("\".*?\""),
            TRule.of("-?\\d+\\.\\d+"),
            TRule.of("-?\\d+")
        };
        TRule varRule = TRule.of("[a-zA-Z][a-zA-Z_0-9]+");

        Tokenizer ts = new Tokenizer(true)
            .addMultipleRules(baseRules)
            .startNextOrder()
            .addMultipleRules(valueRules)
            .startNextOrder()
            .addRule(varRule);

        List<TEngineMatch> tokens = ts.rawTokens(__test__);
        Assertions.assertEquals(
            List.of(
                T("int", baseRules[0]), T("a_b_c", varRule), T("=", baseRules[1]), T("-1", valueRules[2]), T(";", baseRules[1]),
                T("float", baseRules[0]), T("xyz", varRule), T("=", baseRules[1]), T("6.56", valueRules[1]),T(";", baseRules[1]),
                T("string", baseRules[0]), T("str", varRule), T("=", baseRules[1]), T("\"hello world\"", valueRules[0]),T(";", baseRules[1])
            ),
            tokens
        );
    }

    /**
     * A proper demonstration of how the tokenizer can be utilised!
     * 
     * @throws Exception
     */
    @Test
    public void testUsageExample() throws Exception {
        String testExample = """
            int a_b_c = -1;
            float xyz = 6.56;
            string str = \"hello world\";
            """;

        Tokenizer ts = new Tokenizer(true)
            .addMultipleRules(
                TRule.of("(float|int|string)"),
                TRule.of(";|\\="),
                TRule.of("\\r\\n\\t", false)
            )
            .startNextOrder()
            .addMultipleRules(
                TRule.of("\".*?\""),
                TRule.of("-?\\d+\\.\\d+"),
                TRule.of("-?\\d+")
            )
            .startNextOrder()
            .addRule(TRule.of("[a-zA-Z][a-zA-Z_0-9]+"));


        // verify the tokens generated
        List<String> tokens = ts.stringTokens(testExample);
        Assertions.assertEquals(
            tokens,
            List.of(
                "int", "a_b_c", "=", "-1",";",
                "float", "xyz", "=", "6.56", ";",
                "string", "str", "=", "\"hello world\"",";"
            )
        );
    }

}

