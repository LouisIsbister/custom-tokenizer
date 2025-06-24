package src;

import java.util.Objects;

public class Rule {

    private final String rule;
    private final RulePriority priority;
    private final boolean capture;

    public Rule(String rule) {
        this(rule, RulePriority.NATURAL, true);
    }
    public Rule(String rule, RulePriority priority) {
        this(rule, priority, true);
    }
    public Rule(String rule, boolean capture) {
        this(rule, RulePriority.NATURAL, capture);
    }
    public Rule(String rule, RulePriority priority, boolean capture) {
        Objects.requireNonNull(rule);
        Objects.requireNonNull(priority);

        this.rule = rule;
        this.priority = priority;
        this.capture = capture;
    }


    public static Rule of(String rule) {
        return new Rule(rule);
    }
    public static Rule of(String rule, RulePriority priority) {
        return new Rule(rule, priority);
    }
    public static Rule of(String rule, boolean capture) {
        return new Rule(rule, capture);
    }
    public static Rule of(String rule, RulePriority priority, boolean capture) {
        return new Rule(rule, priority, capture);
    }


    // getters
    public String rule() { return rule; }
    public RulePriority priority() { return priority; }
    public boolean capture() { return capture; }

    @Override
    public String toString() {
        return String.format("%s(%d)", rule, priority.PRIORITY_LEVEL);
    }

    public String fullToString() {
        return String.format("[%s(%d) %b]", rule, priority.PRIORITY_LEVEL, capture);
    }

}


