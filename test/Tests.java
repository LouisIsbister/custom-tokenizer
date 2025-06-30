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


    //
    //
    //

    @Test
    public void tokenizerToString() {
        TRule r1 = TRule.of("float", TRulePriority.IMMEDIATE, true);
        TRule r2 = TRule.of("hello", TRulePriority.NATURAL, false);

        Tokenizer ts = new Tokenizer(0);
        ts.addRule(0, r1);
        ts.addRule(2, r2);
        Assertions.assertEquals(
            "{0=[float(3)], 2=[hello(2)]}",
            ts.toString()
        );
    }
    

    //
    //
    //

    @Test
    public void testRulePriorityA() {
        Tokenizer ts = TestUtil.generateTestTokenizerA();
        Assertions.assertEquals(
            "{0=[r1(3), r4(3), r6(3), r3(2), r2(1), r5(1)]}",
            ts.toString()
        );
    }

    @Test
    public void testRulePriorityB() {
        Tokenizer ts = TestUtil.generateTestTokenizerB();
        Assertions.assertEquals(
            "{0=[r1(3), r3(2)], 1=[r4(3), r2(1), r5(1)], 2=[r6(3)]}",
            ts.toString()
        );
    }


    //
    //
    //

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

        Tokenizer ts1 = new Tokenizer(0, true);
        Tokenizer ts2 = new Tokenizer(0, false);
        ts1.addRulesOfOrder(0, r1, r2, r3);
        ts2.addRulesOfOrder(0, r1, r2, r3);

        Assertions.assertEquals(
            List.of("r1", "r3", "r1"),
            ts1.stringTokens(input)
        );

        Assertions.assertThrows(
            TokenizerFailureException.class,
            () -> ts2.stringTokens(input)
        );

    }


    //
    //
    //

    @Test
    public void testUsageA() throws Exception {
        String test = "int a_b_c = 1; float xyz = 6.56; string str = \"hello world\";";

        Tokenizer ts = new Tokenizer(0, true);
        
        TRule typeRule =    TRule.of("(float|int|string)");
        TRule syntaxRule =  TRule.of(";|\\=");
        TRule newlineRule = TRule.of("\\r\\n\\t", false);
        ts.addRulesOfOrder(0, typeRule, syntaxRule, newlineRule);

        TRule stringValRule = TRule.of("\".*?\"");
        TRule floatValRule =  TRule.of("-?\\d+\\.\\d+");
        TRule intValRule =    TRule.of("-?\\d+");
        ts.addRulesOfOrder(1, stringValRule, floatValRule, intValRule);

        TRule varRule = TRule.of("[a-zA-Z][a-zA-Z_0-9]+");
        ts.addRulesOfOrder(2, varRule);

        List<TEngineMatch> tokens = ts.rawTokens(test);
        Assertions.assertEquals(
            List.of(
                T("int", typeRule), T("a_b_c", varRule), T("=", syntaxRule), T("1", intValRule), T(";", syntaxRule),
                T("float", typeRule), T("xyz", varRule), T("=", syntaxRule), T("6.56", floatValRule),T(";", syntaxRule),
                T("string", typeRule), T("str", varRule), T("=", syntaxRule), T("\"hello world\"", stringValRule),T(";", syntaxRule)
            ),
            tokens
        );
    }

}

