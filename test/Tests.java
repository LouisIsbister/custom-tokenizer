package test;

import src.*;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;


public class Tests {

    @Test
    public void ruleToString() {
        Rule r1 = Rule.of("r1", RulePriority.IMMEDIATE, true);
        Rule r2 = Rule.of("r2", RulePriority.END);
        Rule r3 = Rule.of("r3", RulePriority.NATURAL, false);

        Assertions.assertEquals("r1(3)", r1.toString());
        Assertions.assertEquals("r2(1)", r2.toString());
        Assertions.assertEquals("r3(2)", r3.toString());

        Assertions.assertEquals("[r1(3) true]", r1.fullToString());
        Assertions.assertEquals("[r2(1) true]", r2.fullToString());
        Assertions.assertEquals("[r3(2) false]", r3.fullToString());
    }

    @Test
    public void tokenizerToString() {
        Rule r1 = Rule.of("float", RulePriority.IMMEDIATE, true);
        Rule r2 = Rule.of("hello", RulePriority.NATURAL, false);

        Tokenizer ts = new Tokenizer(0);
        ts.addRule(0, r1);
        ts.addRule(2, r2);
        Assertions.assertEquals(
            "{0=[float(3)], 2=[hello(2)]}",
            ts.toString()
        );
    }

    @Test
    public void testRulePriority() {
        Rule r1 = Rule.of("r1", RulePriority.IMMEDIATE, true);
        Rule r2 = Rule.of("r2", RulePriority.END,       true);
        Rule r3 = Rule.of("r3", RulePriority.NATURAL,   true);
        Rule r4 = Rule.of("r4", RulePriority.IMMEDIATE, true);

        Tokenizer ts = new Tokenizer(0);
        ts.addRule(0, r1); ts.addRule(0, r2);
        ts.addRule(0, r3); ts.addRule(0, r4);

        Assertions.assertEquals(
            "{0=[r1(3), r4(3), r3(2), r2(1)]}",
            ts.toString()
        );
    }

}

