package test;

import src.rules.TRule;
import src.rules.TRulePriority;
import src.tokenizing.Tokenizer;
import src.tokenizing.TMatchResult;

public class TestUtil {

    public static TMatchResult T(String s, TRule r) {
        return new TMatchResult(s, r);
    }

    public static Tokenizer generateTestTokenizerA() {
        TRule r1 = TRule.of("r1", TRulePriority.IMMEDIATE, true);
        TRule r2 = TRule.of("r2", TRulePriority.END,       true);
        TRule r3 = TRule.of("r3", TRulePriority.NATURAL,   true);
        TRule r4 = TRule.of("r4", TRulePriority.IMMEDIATE, true);
        TRule r5 = TRule.of("r5", TRulePriority.END, true);
        TRule r6 = TRule.of("r6", TRulePriority.IMMEDIATE, true);

        Tokenizer ts = new Tokenizer(0);
        ts.addRule(r1); ts.addRule(r2);
        ts.addRule(r3); ts.addRule(r4);
        ts.addRule(r5); ts.addRule(r6);

        return ts;
    }

    public static Tokenizer generateTestTokenizerB() {
        TRule r1 = TRule.of("r1", TRulePriority.IMMEDIATE, true);
        TRule r2 = TRule.of("r2", TRulePriority.END,       true);
        TRule r3 = TRule.of("r3", TRulePriority.NATURAL,   true);
        TRule r4 = TRule.of("r4", TRulePriority.IMMEDIATE, false);
        TRule r5 = TRule.of("r5", TRulePriority.END, false);
        TRule r6 = TRule.of("r6", TRulePriority.IMMEDIATE, false);

        Tokenizer ts = new Tokenizer(0);
        ts.addRule(0, r1); ts.addRule(1, r2);
        ts.addRule(0, r3); ts.addRule(1, r4);
        ts.addRule(1, r5); ts.addRule(2, r6);

        return ts;
    }   


}
