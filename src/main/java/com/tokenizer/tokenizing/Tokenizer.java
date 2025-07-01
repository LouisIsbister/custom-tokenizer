
package com.tokenizer.tokenizing;

import com.tokenizer.rules.TRule;
import com.tokenizer.rules.TRulePriority;
import com.tokenizer.tokenizing.TokenizerEngine.TEngineMatch;
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
 *  > addRule(1, W(3))
 * 1 | W(3) X(2) Y(2) Z(2)
 * 
 */

public class Tokenizer {

    public static enum Ordering {
        CURRENT_ORDER,
        NEXT_ORDER,
    }

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

    //
    //
    //

    public List<TEngineMatch> rawTokens(String input) throws TokenizerFailureException {
        return TokenizerEngine.tokenize(input, rules);
    }

    public List<String> stringTokens(String input) throws TokenizerFailureException {
        return stringTokens(rawTokens(input));
    }

    public List<String> stringTokens(List<TEngineMatch> matches) throws TokenizerFailureException {
        return matches.stream()
            .map(TEngineMatch::token)
            .toList();
    }

    public Tokenizer addMultipleRules(TRule... rules) {
        for (TRule rule : rules) {
            addRule(rule); // additionOrder
        }
        return this;
    }

    /**
     * Given a rule and an order, update the tokenizers state then add the rule to the
     * rules map.
     * 
     * @param order the order at which this rule will be processed relative to other rules
     * @param rule  the rule object itself
     * @param priority the prioirty of this rule with respect to other rules of the same order!
     */
    public Tokenizer addRule(TRule rule) {
        // updateTokenizer(additionOrder);
        insertRuleByPriority(rules.get(this.order), rule);
        return this;
    }

    // public Tokenizer addRule(TRule rule) { 
    //     return addRule(Ordering.CURRENT_ORDER, rule);
    // }


    /**
     * Insert a new rule into a list of rules based upon its priority!
     * For example, if rule X has priority 3 (IMMEDIATE) and we have a list of rules
     * [A(3), B(2), C(2), D(1)] we want that to become [A(3), X(3), B(2), C(2), D(1)] 
     * 
     * @param rules the list of rules
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

    // private void updateTokenizer(Ordering addingOrder) {
    //     if (addingOrder == Ordering.NEXT_ORDER) {
    //         this.order++;
    //     }

    //     // if the order is not a key in the rules map then add it
    //     if (!rules.containsKey(order)) {
    //         rules.put(order, new ArrayList<>());
    //     }
    // }

    @Override
    public String toString() {
        return rules.toString();
    }

}


