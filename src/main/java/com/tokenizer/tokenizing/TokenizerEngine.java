package com.tokenizer.tokenizing;

import com.tokenizer.rules.TRule;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenizerEngine {

    public static record TEngineMatch(
        String token, 
        TRule rule
    ) {}


    /**
     * Iterate though the input string continously retrieving the 
     * next token and appending it to the result list.
     * 
     * @param input the string to be tokenized
     * @return list of generated tokens
     * @throws TokenizerFailureException
     */
    public static List<TEngineMatch> tokenize(String input, NavigableMap<Integer, List<TRule>> rules) throws TokenizerFailureException {
        List<TEngineMatch> ret = new ArrayList<>();

        final int length = input.length();
        int index = 0;
        while (index < length) {
            TEngineMatch engineMatch = retrieveNextToken(input, index, rules);
            index += engineMatch.token().length();

            if (engineMatch.rule().isCapturable()) {
                ret.add(engineMatch);
            }
        }

        return ret;
    }


    /**
     * Iterate every rules until a match is found and return the result.
     * 
     * @param input the provided string to be tokenized
     * @param index the start index to search in the input string
     * @param rules the tokenizers rules
     * @return the match result
     * @throws TokenizerFailureException 
     */
    private static TEngineMatch retrieveNextToken(String input, int index, NavigableMap<Integer, List<TRule>> rules) throws TokenizerFailureException {
        final int inputLength = input.length();
        
        for (Map.Entry<Integer, List<TRule>> entry : rules.entrySet()) {
            for (TRule rule : entry.getValue()) {
                Pattern pat = rule.regex();
                Matcher matcher = pat.matcher(input);
                matcher.region(index, inputLength);

                if (matcher.find() && matcher.start() == index) {
                    return new TEngineMatch(matcher.group(), rule); 
                }
            }
        }

        String inputWindow = errorInputWindow(input);
        String errMsg = String.format("Failed to tokenize the beginning of sequence: '%s'\nPlease verify your rule set.", inputWindow);
        throw new TokenizerFailureException(errMsg);
    }

    private static final int INPUT_WINDOW_SIZE = 25;
    private static final String errorInputWindow(String input) {
        return input.length() < INPUT_WINDOW_SIZE ? 
                input :
                input.substring(0, INPUT_WINDOW_SIZE);
    }

}
