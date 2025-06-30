package test;

import src.rules.TRule;
import src.rules.TRulePriority;
import src.tokenizing.Tokenizer;
import src.tokenizing.TokenizerEngine.TEngineMatch;

public class TestUtil {

    public static TEngineMatch T(String s, TRule r) {
        return new TEngineMatch(s, r);
    }

    public static Tokenizer generateTestTokenizerA() {
        TRule r1 = TRule.of("r1", TRulePriority.IMMEDIATE, true);
        TRule r2 = TRule.of("r2", TRulePriority.END,       true);
        TRule r3 = TRule.of("r3", TRulePriority.NATURAL,   true);
        TRule r4 = TRule.of("r4", TRulePriority.IMMEDIATE, true);
        TRule r5 = TRule.of("r5", TRulePriority.END,       true);
        TRule r6 = TRule.of("r6", TRulePriority.IMMEDIATE, true);

        Tokenizer ts = new Tokenizer();
        ts.addRulesOfOrder(0, r1, r2, r3, r4, r5, r6);

        return ts;
    }

    public static Tokenizer generateTestTokenizerB() {
        TRule r1 = TRule.of("r1", TRulePriority.IMMEDIATE, true);
        TRule r2 = TRule.of("r2", TRulePriority.END,       true);
        TRule r3 = TRule.of("r3", TRulePriority.NATURAL,   true);
        TRule r4 = TRule.of("r4", TRulePriority.IMMEDIATE, false);
        TRule r5 = TRule.of("r5", TRulePriority.END,       false);
        TRule r6 = TRule.of("r6", TRulePriority.IMMEDIATE, false);

        Tokenizer ts = new Tokenizer(0);
        ts.addRulesOfOrder(0, r1, r3);
        ts.addRulesOfOrder(1, r2, r4, r5);
        ts.addRulesOfOrder(2, r6);

        return ts;
    }   


}
