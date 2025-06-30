package src.rules;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class TRule {

    /* the pattern associated with this rule */
    private final Pattern regex;

    /* priority of this rule relative to other rules of the same order */
    private final TRulePriority priority;
    
    /* whether tokens captured by the regex should be captured or ignored */
    private final boolean isCapturable;

    /**
     * Fully parametised constructor
     * 
     * @param rule     the regex string associated with the rule
     * @param priority the priority of this rule
     * @param capture  whether to capture this rules tokens
     */
    public TRule(String rule, TRulePriority priority, boolean capture) {
        Objects.requireNonNull(rule);
        Objects.requireNonNull(priority);

        this.priority = priority;
        this.isCapturable = capture;

        try {
            this.regex = Pattern.compile(rule);
        } catch (PatternSyntaxException e) {
            String errMsg = String.format("ERR: Rule could not be compiled to regex! Stack trace:\n%s\n", e.getStackTrace().toString());
            throw new IllegalArgumentException(errMsg);
        }
    }
    /* optional constructor A */
    public TRule(String rule) {
        this(rule, TRulePriority.NATURAL, true);
    }
    /* optional constructor B */
    public TRule(String rule, boolean capture) {
        this(rule, TRulePriority.NATURAL, capture);
    }
    
    /* factory methods for creating rules */
    public static TRule of(String rule, TRulePriority priority, boolean capture) {
        return new TRule(rule, priority, capture);
    }
    public static TRule of(String rule) {
        return new TRule(rule);
    }
    public static TRule of(String rule, boolean capture) {
        return new TRule(rule, capture);
    }

    /* rule information getters */ 
    public String rule() { return regex.pattern(); }
    public Pattern regex() { return regex; }
    public TRulePriority priority() { return priority; }
    public boolean isCapturable() { return isCapturable; }

    @Override
    public String toString() {
        return String.format("%s(%d)", rule(), priority.PRIORITY_LEVEL);
    }

    public String fullToString() {
        return String.format("[%s(%d) %b]", rule(), priority.PRIORITY_LEVEL, isCapturable);
    }

}


