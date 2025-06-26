package src.rules;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class TRule {

    private final String rule;
    private final Pattern regex;
    private final TRulePriority priority;
    private final boolean isCapturable;

    public TRule(String rule) {
        this(rule, TRulePriority.NATURAL, true);
    }
    public TRule(String rule, TRulePriority priority) {
        this(rule, priority, true);
    }
    public TRule(String rule, boolean capture) {
        this(rule, TRulePriority.NATURAL, capture);
    }
    public TRule(String rule, TRulePriority priority, boolean capture) {
        Objects.requireNonNull(rule);
        Objects.requireNonNull(priority);

        this.rule = rule;
        this.priority = priority;
        this.isCapturable = capture;

        try {
            this.regex = Pattern.compile(rule);
        } catch (PatternSyntaxException e) {
            String errMsg = String.format("ERR: Rule could not be compiled to regex! Stack trace:\n%s\n", e.getStackTrace().toString());
            throw new IllegalArgumentException(errMsg);
        }
    }


    public static TRule of(String rule) {
        return new TRule(rule);
    }
    public static TRule of(String rule, TRulePriority priority) {
        return new TRule(rule, priority);
    }
    public static TRule of(String rule, boolean capture) {
        return new TRule(rule, capture);
    }
    public static TRule of(String rule, TRulePriority priority, boolean capture) {
        return new TRule(rule, priority, capture);
    }


    // getters
    public String rule() { return rule; }
    public Pattern regex() { return regex; }
    public TRulePriority priority() { return priority; }
    public boolean isCapturable() { return isCapturable; }

    @Override
    public String toString() {
        return String.format("%s(%d)", rule, priority.PRIORITY_LEVEL);
    }

    public String fullToString() {
        return String.format("[%s(%d) %b]", rule, priority.PRIORITY_LEVEL, isCapturable);
    }

}


