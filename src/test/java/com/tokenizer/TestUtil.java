package com.tokenizer;

import com.tokenizer.rules.*;
import com.tokenizer.tokenizing.Tokenizer;
import com.tokenizer.tokenizing.TokenizerEngine.TEngineMatch;

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

        Tokenizer ts = new Tokenizer()
            .addMultipleRules(r1, r2, r3, r4, r5, r6);

        return ts;
    }

    public static Tokenizer generateTestTokenizerB() {
        TRule r1 = TRule.of("r1", TRulePriority.IMMEDIATE, true);
        TRule r2 = TRule.of("r2", TRulePriority.END,       true);
        TRule r3 = TRule.of("r3", TRulePriority.NATURAL,   true);
        TRule r4 = TRule.of("r4", TRulePriority.IMMEDIATE, false);
        TRule r5 = TRule.of("r5", TRulePriority.END,       false);
        TRule r6 = TRule.of("r6", TRulePriority.IMMEDIATE, false);

        Tokenizer ts = new Tokenizer()
            .addMultipleRules(r1, r3)
            .startNextOrder()
            .addMultipleRules(r2, r4, r5)
            .startNextOrder()
            .addMultipleRules(r6);

        return ts;
    }   


}
