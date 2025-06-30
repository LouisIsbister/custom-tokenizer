
package src.tokenizing;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

import src.rules.TRule;
import src.rules.TRulePriority;
import src.tokenizing.TokenizerEngine.TEngineMatch;



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
 * Order | Rule(RulePriority) ..
 * ------------------------------
 *    0  | A(3) B(2) C(1) D(1)
 *    1  | X(2) Y(2) Z(2)
 *    2  | a(3) b(1) c(1)
 * 
 *  > addRule(1, W(3))
 * 1 | W(3) X(2) Y(2) Z(2)
 * 
 */

public class Tokenizer {

    public static enum Order {
        CURRENT,
        START_NEW,
    }

    private final int startOrder;
    private int topOrder;

    /* map of order -> list of rules */
    private NavigableMap<Integer, List<TRule>> rules;


    /**
     * Fully parametised constructor
     * 
     * @param startOrder     
     * @param skipWhitespace whether leading whitespace sequences should be ignored by default
     */
    public Tokenizer(int startOrder, boolean skipWhitespace) {
        rules = new TreeMap<>();

        this.startOrder = startOrder;
        this.topOrder = startOrder;

        if (skipWhitespace) {
            addRule(TRule.of("\\s+", TRulePriority.IMMEDIATE, false));
        }
    }
    public Tokenizer() {
        this(0, false);
    }
    public Tokenizer(boolean skipWhitespace) {
        this(0, skipWhitespace);
    }
    public Tokenizer(int startOrder) {
        this(startOrder, false);
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

    public void addRulesOfOrder(int order, TRule... rules) {
        for (TRule rule : rules) {
            addRule(order, rule);
        }
    }

    public void addRule(TRule rule) { 
        addRule(topOrder, rule);
    }

    /**
     * Given a rule and an order, update the tokenizers state then add the rule to the
     * rules map.
     * 
     * @param order the order at which this rule will be processed relative to other rules
     * @param rule  the rule object itself
     * @param priority the prioirty of this rule with respect to other rules of the same order!
     */
    public void addRule(int order, TRule rule) {
        updateTokenizer(order);
        insertRuleByPriority(rules.get(order), rule);
    }


    /**
     * Insert a new rule into a list of rules based upon its priority!
     * For example, if rule X has priority 3 (IMMEDIATE) and we have a list of rules
     * [A(3), B(2), C(2), D(1)] we want that to become [A(3), X(3), B(2), C(2), D(1)] 
     * 
     * @param rules the list of rules
     * @param insertRule the rule to be added
     * @return
     */
    private boolean insertRuleByPriority(List<TRule> rules, TRule insertRule) {
        if (rules.isEmpty()) {
            rules.add(insertRule);
            return true;
        }

        TRulePriority insertPr = insertRule.priority();
        TRulePriority lastRulePr = rules.get(rules.size() - 1).priority();
        if (lastRulePr.PRIORITY_LEVEL >= insertPr.PRIORITY_LEVEL) {
            rules.add(insertRule);
            return true;
        }

        for (int i = 0; i < rules.size(); i++) {
            TRulePriority currentPr = rules.get(i).priority();

            if (insertPr.PRIORITY_LEVEL > currentPr.PRIORITY_LEVEL) {
                rules.add(i, insertRule);
                return true;
            }
        }
        return false;
    }

    
    private void updateTokenizer(int order) {
        if (order < startOrder) {
            String errMsg = String.format("Order: %s is less than minimum order: %d\n", order, startOrder);
            throw new IllegalArgumentException(errMsg);
        }

        // ensure the topOrder is consistent
        if (order > topOrder) {
            topOrder = order;
        }

        // if the order is not a key in the rules map then add it
        if (!rules.containsKey(order)) {
            rules.put(order, new ArrayList<>());
        }
    }

    @Override
    public String toString() {
        return rules.toString();
    }

}


