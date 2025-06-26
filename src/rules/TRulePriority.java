package src.rules;

public enum TRulePriority {
    IMMEDIATE(3),
    NATURAL(2),
    END(1);

    public final int PRIORITY_LEVEL;

    TRulePriority(int priority) {
        PRIORITY_LEVEL = priority;
    }

    public static boolean isHigherPriority(TRulePriority first, TRulePriority other) {
        return first.PRIORITY_LEVEL > other.PRIORITY_LEVEL;
    }

    @Override
    public String toString() {
        return Integer.toString(PRIORITY_LEVEL);
    }
}