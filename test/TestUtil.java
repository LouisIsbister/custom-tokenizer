package test;

import src.rules.Rule;
import src.rules.RulePriority;
import src.tokenizing.Tokenizer;
import src.tokenizing.Tokenizer.TMatchResult;

public class TestUtil {

    public static TMatchResult T(String s, Rule r) {
        return new TMatchResult(s, r);
    }

    public static Tokenizer generateTestTokenizerA() {
        Rule r1 = Rule.of("r1", RulePriority.IMMEDIATE, true);
        Rule r2 = Rule.of("r2", RulePriority.END,       true);
        Rule r3 = Rule.of("r3", RulePriority.NATURAL,   true);
        Rule r4 = Rule.of("r4", RulePriority.IMMEDIATE, true);
        Rule r5 = Rule.of("r5", RulePriority.END, true);
        Rule r6 = Rule.of("r6", RulePriority.IMMEDIATE, true);

        Tokenizer ts = new Tokenizer(0);
        ts.addRule(r1); ts.addRule(r2);
        ts.addRule(r3); ts.addRule(r4);
        ts.addRule(r5); ts.addRule(r6);

        return ts;
    }

    public static Tokenizer generateTestTokenizerB() {
        Rule r1 = Rule.of("r1", RulePriority.IMMEDIATE, true);
        Rule r2 = Rule.of("r2", RulePriority.END,       true);
        Rule r3 = Rule.of("r3", RulePriority.NATURAL,   true);
        Rule r4 = Rule.of("r4", RulePriority.IMMEDIATE, false);
        Rule r5 = Rule.of("r5", RulePriority.END, false);
        Rule r6 = Rule.of("r6", RulePriority.IMMEDIATE, false);

        Tokenizer ts = new Tokenizer(0);
        ts.addRule(0, r1); ts.addRule(1, r2);
        ts.addRule(0, r3); ts.addRule(1, r4);
        ts.addRule(1, r5); ts.addRule(2, r6);

        return ts;
    }   


}
