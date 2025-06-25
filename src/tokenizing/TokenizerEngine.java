package src.tokenizing;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import src.rules.Rule;
import src.tokenizing.Tokenizer.TMatchResult;

public class TokenizerEngine {

    /**
     * Iterate though the input string continously retrieving the 
     * next token and appending it to the result list.
     * 
     * @param input the string to be tokenized
     * @return list of generated tokens
     * @throws Exception
     */
    public static List<TMatchResult> tokenize(String input, NavigableMap<Integer, List<Rule>> rules) throws Exception {
        List<TMatchResult> ret = new ArrayList<>();

        int index = 0;
        while (index < input.length()) {
            String next = input.substring(index);

            Optional<TMatchResult> match = retrieveNextToken(next, rules);
            if (!match.isPresent()) {
                throw new Exception(matchFailureMsg(next));
            }
 
            TMatchResult matched = match.get();
            index += matched.token().length();
            if (matched.isCapturable()) {
                ret.add(matched);
            }
        }

        return ret;
    }


    /**
     * Iterate every rules until a match is found and return the result.
     * 
     * @param input the provided string to be tokenized
     * @return the match result
     */
    private static Optional<TMatchResult> retrieveNextToken(String input, NavigableMap<Integer, List<Rule>> rules) {
        for (int order : rules.keySet()) {
            for (Rule rule : rules.get(order)) {
                Optional<String> token = tryMatch(rule, input);
                if (!token.isPresent()) {
                    continue;
                }

                TMatchResult res = new TMatchResult(token.get(), rule);
                return Optional.of(res) ;
            }
        }
        return Optional.empty();
    }

    /**
     * Try and match a given rule to the input
     *
     * @param rule the rule to be applied
     * @param input again the string to be tokenized
     * @return
     */
    private static Optional<String> tryMatch(Rule rule, String input) {
        Pattern pat = rule.regex();
        
        Matcher matcher = pat.matcher(input);
        if (matcher.find() && matcher.start() == 0) {
            return Optional.of(matcher.group());
        }
        return Optional.empty();
    }


    private static final String matchFailureMsg(String input) {
        int end = input.length() < 10 ? input.length() : 10;
        return String.format("Given rules could not match the beginning of input: '%s'...\n", input.substring(0, end));
    }
}
