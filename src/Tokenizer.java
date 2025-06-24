package src;


import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;



public class Tokenizer {

    private final int startOrder;
    private int topOrder;

    private Map<Integer, List<Rule>> rules;

    // public Tokenizer() {
    //     this(0);
    // }

    public Tokenizer(int startOrder) {
        rules = new HashMap<>();

        this.startOrder = startOrder;
        this.topOrder = startOrder;
    }


    public void addRule(Rule rule) { 
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
    public void addRule(int order, Rule rule) {
        updateTokenizer(order);

        List<Rule> rulesOfOrder = rules.get(order);
        insertRuleByPriority(rulesOfOrder, rule);
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
    private boolean insertRuleByPriority(List<Rule> rules, Rule insertRule) {
        if (rules.isEmpty()) {
            rules.add(insertRule);
            return true;
        }

        RulePriority insertPr = insertRule.priority();
        RulePriority lastRulePr = rules.get(rules.size() - 1).priority();
        if (lastRulePr.PRIORITY_LEVEL >= insertPr.PRIORITY_LEVEL) {
            rules.add(insertRule);
            return true;
        }

        for (int i = 0; i < rules.size(); i++) {
            RulePriority currentPr = rules.get(i).priority();

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


