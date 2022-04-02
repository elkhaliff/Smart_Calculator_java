package calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcEngine {
    Pattern patternBase = Pattern.compile("(-?\\w+)\\s?");
    Matcher matcherBase;
    Pattern patternValue = Pattern.compile("([-+*/]+)\\s*(\\w+)");
    Matcher matcherValue;
    Pattern patternVariables = Pattern.compile("([a-zA-Z]+)\\s*([=])\\s*(\\d+)\\b+");
    Matcher matcherVariables;
    Pattern patternVariablesFor = Pattern.compile("([a-zA-Z]+)\\s*([=])\\s*([a-zA-Z]+)\\b+");
    Matcher matcherVariablesFor;
    final Map<String, Integer> variables = new HashMap<>();

    final private String unknownVariable = "Unknown variable";
    final private String unknownOperator = "Unknown operator";
    final private String invalidIdentifier = "Invalid identifier";
    final private String invalidAssignment = "Invalid assignment";

    private void println(String text) {
        System.out.println(text);
    }

    private boolean isBase(String input) { return patternBase.matcher(input).find(); }
    private boolean isValue(String input) { return patternValue.matcher(input).find(); }
    private boolean isVariables(String input) { return patternVariables.matcher(input).matches(); }
    private boolean isVariablesFor(String input) { return patternVariablesFor.matcher(input).matches(); }

    private boolean testString(String input) {
        return (!input.contains("=")) ? isBase(input) :
                (isValue(input) || isVariables(input) || isVariablesFor(input));
    }

    private int getValue(String input) {
        try {
            return Integer.parseInt(input);
        } catch (Exception e) {
            return variables.get(input);
        }
    }

    public void calc(String input) {
        if (testString(input)) {
            if (isVariables(input)) {
                matcherVariables = patternVariables.matcher(input);
                while (matcherVariables.find()) {
                    var variable = matcherVariables.group(1).trim();
                    var value = Integer.parseInt(matcherVariables.group(3));
                    variables.put(variable, value);
                }
            } else if (isVariablesFor(input)) {
                matcherVariablesFor = patternVariablesFor.matcher(input);
                while (matcherVariablesFor.find()) {
                    var variable = matcherVariablesFor.group(1).trim();
                    var value = matcherVariablesFor.group(3);
                    if (variables.containsKey(value))
                        variables.put(variable, variables.get(value));
                    else
                        println(unknownVariable);
                }
            } else if (isBase(input) && !input.contains("=")) {
                var base = 0;
                matcherBase = patternBase.matcher(input);
                matcherBase.find();
                try {
                    base = getValue(matcherBase.group(1).trim());
                } catch (Exception e) {
                    println(unknownVariable);
                    return;
                }
                if (isValue(input)) {
                    matcherValue = patternValue.matcher(input);
                    while (matcherValue.find()) {
                        int value;
                        try {
                            value = getValue(matcherValue.group(2).trim());
                        } catch (Exception e) {
                            println(unknownVariable);
                            return;
                        }
                        var opArr = matcherValue.group(1).trim().toCharArray();
                        String op = (opArr[0] == '-' && opArr.length % 2 == 0) ? "+" : String.valueOf(opArr[0]);
                        switch (op) {
                            case "+": {
                                base += value;
                                break;
                            }
                            case "-": {
                                base -= value;
                                break;
                            }
                            case "*": {
                                base *= value;
                                break;
                            }
                            case "/": {
                                base /= value;
                                break;
                            }
                            default: {
                                println(unknownOperator);
                                return;
                            }
                        }
                    }
                }
                println(String.valueOf(base));
            } else
                println(invalidAssignment);
        } else
            println(invalidIdentifier);
    }

    public void help() {
        println("The program calculates the sum of numbers. You can also use any sign of the arithmetic operation.");
    }

    public void exit() {
        println("Bye!");
    }
}