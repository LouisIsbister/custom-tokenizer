# Tokenizer
Throughout my time at university I have written far too many regex-based tokenizers in Java for various coding assignments, with each assignment that requires a new one becoming more and more frustrating! As a result, the goal of this project is to create a simple to use, regex-based tokenizer that can be utilised for small-scale projects.  

Example usage of the tokenizer:  
```java
// The string we want to pass into tokens!
String testExample = """
    int a_b_c = -1;
    float xyz = 6.56;
    string str = \"hello world\";
    """;

// define the tokenizer and its rule set
Tokenizer ts = new Tokenizer(true)  // true to skip whitespaces by default 
    .addMultipleRules(
        TRule.of("(float|int|string)"), // different types
        TRule.of(";|\\="),              // general syntax tokens
        TRule.of("\\r\\n\\t", false)    // false to not capture these tokens
    )
    .startNextOrder()
    .addMultipleRules(
        TRule.of("\".*?\""),        // string values
        TRule.of("-?\\d+\\.\\d+"),  // float values
        TRule.of("-?\\d+")          // int values
    )
    .startNextOrder()
    .addRule(TRule.of("[a-zA-Z][a-zA-Z_0-9]+")); // var names!

// Generate the tokens as strings
List<String> tokens = ts.stringTokens(testExample);

// Verify the tokens are as expected!
Assertions.assertEquals(
    tokens,
    List.of(
        "int", "a_b_c", "=", "-1", ";",
        "float", "xyz", "=", "6.56", ";",
        "string", "str", "=", "\"hello world\"", ";"
    )
);
```