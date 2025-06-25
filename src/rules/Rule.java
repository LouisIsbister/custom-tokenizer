package src.rules;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class Rule {

    private final String rule;
    private final Pattern regex;
    private final RulePriority priority;
    private final boolean isCapturable;

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
        this.isCapturable = capture;

        try {
            this.regex = Pattern.compile(rule);
        } catch (PatternSyntaxException e) {
            String errMsg = String.format("ERR: Rule could not be compiled to regex! Stack trace:\n%s\n", e.getMessage());
            throw new IllegalArgumentException(errMsg);
        }
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
    public Pattern regex() { return regex; }
    public RulePriority priority() { return priority; }
    public boolean isCapturable() { return isCapturable; }

    @Override
    public String toString() {
        return String.format("%s(%d)", rule, priority.PRIORITY_LEVEL);
    }

    public String fullToString() {
        return String.format("[%s(%d) %b]", rule, priority.PRIORITY_LEVEL, isCapturable);
    }

}


