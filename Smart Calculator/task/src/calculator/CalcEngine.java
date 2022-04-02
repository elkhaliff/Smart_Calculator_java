package calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcEngine {
    Pattern patternOperator = Pattern.compile("([-+*/^()])");
    Pattern patternBase = Pattern.compile("(-?\\w+)\\s?");
    Pattern patternValue = Pattern.compile("([-+*/]+)\\s*(\\w+)");
    Pattern patternVariables = Pattern.compile("([a-zA-Z]+)\\s*([=])\\s*(-?\\d+)\\b+");
    Pattern patternVariablesFor = Pattern.compile("([a-zA-Z]+)\\s*([=])\\s*([a-zA-Z]+)\\b+");
    final Map<String, Integer> variables = new HashMap<>();
    final Map<Integer, String> postfix = new HashMap<>();

    final private String unknownOperator = "Unknown operator";
    final private String invalidIdentifier = "Invalid identifier";
    final private String invalidAssignment = "Invalid assignment";
    final private String invalidExpression = "Invalid expression";

    private void println(String text) {
        System.out.println(text);
    }

    private void println(int text) {
        System.out.println(text);
    }

    private boolean isOperator(String input) { return patternOperator.matcher(input).find(); }
    private boolean isBase(String input) { return patternBase.matcher(input).find(); }
    private boolean isValue(String input) { return patternValue.matcher(input).find(); }
    private boolean isVariables(String input) { return patternVariables.matcher(input).matches(); }
    private boolean isVariablesFor(String input) { return patternVariablesFor.matcher(input).matches(); }

    private boolean testString(String input) {
        return (!input.contains("=")) ? isBase(input) :
                (isValue(input) || isVariables(input) || isVariablesFor(input));
    }

    private Integer getValue(String input) {
        try {
            return Integer.parseInt(input);
        } catch (Exception e) {
            return (variables.containsKey(input)) ? variables.get(input) : null;
        }
    }

    private int evaluatePostfix() {
        var stack = new Stack<Integer>();

        for (var element: postfix.entrySet()) {
            var value = element.getValue();
            if (elementLevel(value) < 0) {
                stack.push(getValue(value));
            } else {
                var val1 = stack.pop();
                var val2 = stack.pop();

                switch (value) {
                    case "+": { stack.push(val2 + val1); break; }
                    case "-": { stack.push(val2 - val1); break; }
                    case "*": { stack.push(val2 * val1); break; }
                    case "/": { stack.push(val2 / val1); break; }
                    case "^": { stack.push((int) Math.pow(val2, val1)); break; }
                    default: println(unknownOperator);
                }
            }
        }
        return stack.pop();
    }

    private void setVariables(String input) {
        if (isVariables(input)) {
            Matcher matcherVariables = patternVariables.matcher(input);
            while (matcherVariables.find()) {
                var variable = matcherVariables.group(1).trim();
                var value = Integer.parseInt(matcherVariables.group(3));
                variables.put(variable, value);
            }
        } else if (isVariablesFor(input)) {
            Matcher matcherVariablesFor = patternVariablesFor.matcher(input);
            while (matcherVariablesFor.find()) {
                var variable = matcherVariablesFor.group(1).trim();
                var value = matcherVariablesFor.group(3);
                String unknownVariable = "Unknown variable";
                if (variables.containsKey(value))
                    variables.put(variable, variables.get(value));
                else
                    println(unknownVariable);
            }
        } else
            println(invalidAssignment);
    }

    private int elementLevel(String str) {
        switch (str) {
            case "+": case "-": { return 1; }
            case "*": case "/": { return 2; }
            case "^": { return 3; }
            case "(": case ")": { return 0; }
            default: { return -1; }
        }
    }

    public void infixToPostfix(String input) {
        final Map<Integer, String> inputMap = new HashMap<>();
        var m = 0;
        var strings = input.toCharArray();
        for (var ch: strings) {
            String str = String.valueOf(ch);
            if (!isOperator(str)) {
                inputMap.putIfAbsent(m, "");
                inputMap.put(m, String.format("%s%s", inputMap.get(m), str));
            } else {
                inputMap.put(++m, str);
                m++;
            }
        }

        var stack = new Stack<String>();
        var i = 0;
        postfix.clear();
        for (var entry: inputMap.entrySet()) {
            var value = entry.getValue();
            if (elementLevel(value) < 0) { postfix.put(i++, value); }
            else if ("(".equals(value)) { stack.push(value); }
            else if (")".equals(value)) {
                while (!stack.isEmpty() && !"(".equals(stack.peek())) {
                    postfix.put(i++, stack.pop());
                }
                try {
                    stack.pop();
                } catch (Exception e) {
                    println(invalidExpression);
                    return;
                }
            } else {
                while (!stack.isEmpty() && elementLevel(value) <= elementLevel(stack.peek())) {
                    postfix.put(i++, stack.pop());
                }
                stack.push(value);
            }
        }
        while (!stack.isEmpty()) {
            if ("(".equals(stack.peek())) {
                println(invalidExpression);
                return;
            }
            postfix.put(i++, stack.pop());
        }
    }

    public void calc(String input) {
        if (input.contains("**") || input.contains("//") ) {
            println(invalidExpression);
        } else if (testString(input)) {
            if (input.contains("="))
                setVariables(input);
            else if (!isOperator(input)) {
                try {
                    println(getValue(input));
                } catch (Exception e) {
                    println(invalidExpression);
                }
            } else {
                infixToPostfix(input);
                if (!postfix.isEmpty())
                    println(evaluatePostfix());
            }
        } else
            println(invalidIdentifier);
    }

    public void help() {
        println("The program calculates the sum of numbers.\n" +
                "You can also use any sign of the arithmetic operation.\n" +
                "You can use variables and parentheses..");
    }

    public void exit() { println("Bye!"); }
}