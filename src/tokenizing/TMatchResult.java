package src.tokenizing;

import src.rules.TRule;

public record TMatchResult(String token, TRule rule) {
    public boolean isCapturable() { return rule.isCapturable(); }
}