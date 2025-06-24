package src;

public enum RulePriority {
    IMMEDIATE(3),
    NATURAL(2),
    END(1);

    public final int PRIORITY_LEVEL;

    RulePriority(int priority) {
        PRIORITY_LEVEL = priority;
    }

    public static boolean isHigherPriority(RulePriority first, RulePriority other) {
        return first.PRIORITY_LEVEL > other.PRIORITY_LEVEL;
    }

    @Override
    public String toString() {
        return Integer.toString(PRIORITY_LEVEL);
    }
}