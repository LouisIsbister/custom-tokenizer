
package com.tokenizer.tokenizing;

import com.tokenizer.rules.TRule;
import com.tokenizer.rules.TRulePriority;
import com.tokenizer.tokenizing.TokenizerEngine.TEngineToken;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;


/**
 * A tokenizer contains rules, rules are strings that get applied 
 * as regular expressions to tokenize a text. Rules are given an
 * 'order' which specifies when they should be processed, i.e.
 * rules of higher order try to match a token earlier than those
 * of lower order. Rules of the same order are then sorted by
 * their 'RulePriority'. Rules of higher priority are processed
 * before those of lower priority with the same order.
 * 
 * E.g.
 * 
 *  Order | Rule(RulePriority) ..
 * ------------------------------
 *    1   | A(3) B(2) C(1) D(1)
 *    2   | X(2) Y(2) Z(2)
 *    3   | a(3) b(1) c(1)
 * 
 *  > addRule -> W(3)
 * 3 | a(3) W(3) b(1) c(1)
 */

public class Tokenizer {

    /* the current `order` count of rules */
    private int order;

    /* map of order -> list of rules */
    private NavigableMap<Integer, List<TRule>> rules;


    /**
     * Fully parametised constructor
     * @param skipWhitespace whether leading whitespace sequences should be ignored by default
     */
    public Tokenizer(boolean skipWhitespace) {
        rules = new TreeMap<>();
        order = 0;
        startNextOrder();

        if (skipWhitespace) {
            addRule(TRule.of("\\s+", TRulePriority.IMMEDIATE, false));
        }
    }

    public Tokenizer() {
        this(false);
    }


    /**
     * Tokenize the input string and return the list of TEngineToken objects
     * @param input to be tokenized
     * @return      the list of generated tokens
     * @throws TokenizerFailureException
     */
    public List<TEngineToken> rawTokens(String input) throws TokenizerFailureException {
        return TokenizerEngine.tokenize(input, rules);
    }

    /**
     * Tokenize the input string and return the list of strign tokens 
     * @param input
     * @return
     * @throws TokenizerFailureException
     */
    public List<String> stringTokens(String input) throws TokenizerFailureException {
        return rawTokensToStrings(rawTokens(input));
    }

    /**
     * Converts a list of TEngineToken objects to their strings
     * @param matches   the generated tokens
     * @return          the tokens in sting form
     * @throws TokenizerFailureException
     */
    public List<String> rawTokensToStrings(List<TEngineToken> matches) throws TokenizerFailureException {
        return matches.stream()
            .map(TEngineToken::token)
            .toList();
    }


    /**
     * Add N number of rules to the tokenizer
     * @param rules the rules to be added
     * @return      the current tokenizer object
     */
    public Tokenizer addMultipleRules(TRule... rules) {
        for (TRule rule : rules) {
            addRule(rule);
        }
        return this;
    }

    /**
     * @param rule the rule object to be added to the tokenizer
     */
    public Tokenizer addRule(TRule rule) {
        insertRuleByPriority(rules.get(this.order), rule);
        return this;
    }


    /**
     * Insert a new rule into a list of rules based upon its priority!
     * For example, if rule X has priority 3 (IMMEDIATE) and we have a list of rules
     * [A(3), B(2), C(2), D(1)] we want that to become [A(3), X(3), B(2), C(2), D(1)] 
     * 
     * @param rules      the list of rules
     * @param insertRule the rule to be added
     */
    private void insertRuleByPriority(List<TRule> rules, TRule insertRule) {
        if (rules.isEmpty()) {
            rules.add(insertRule);
            return;
        }

        TRulePriority insertPr = insertRule.priority();
        TRulePriority lastRulePr = rules.get(rules.size() - 1).priority();
        if (lastRulePr.PRIORITY_LEVEL >= insertPr.PRIORITY_LEVEL) {
            rules.add(insertRule);
            return;
        }

        for (int i = 0; i < rules.size(); i++) {
            TRulePriority currentPr = rules.get(i).priority();

            if (insertPr.PRIORITY_LEVEL > currentPr.PRIORITY_LEVEL) {
                rules.add(i, insertRule);
                return;
            }
        }
    }

    public Tokenizer startNextOrder() {
        this.order++;

        // if the order is not a key in the rules map then add it
        if (!rules.containsKey(order)) {
            rules.put(order, new ArrayList<>());
        }
        return this;
    }

    @Override
    public String toString() {
        return rules.toString();
    }

}


