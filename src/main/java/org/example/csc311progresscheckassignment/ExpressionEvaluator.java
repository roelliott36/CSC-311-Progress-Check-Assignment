package org.example.csc311progresscheckassignment;

import java.util.ArrayList;
import java.util.List;

public final class ExpressionEvaluator {

    private ExpressionEvaluator() {
    }

    public static Evaluation evaluate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression is empty.");
        }

        Parser parser = new Parser(expression);
        double result = parser.parseExpression();

        parser.skipWhitespace();

        if (parser.position != parser.input.length()) {
            throw new IllegalArgumentException("Invalid expression.");
        }

        return new Evaluation(result, parser.numbers);
    }

    public static final class Evaluation {
        private final double value;
        private final List<Integer> numbers;

        private Evaluation(double value, List<Integer> numbers) {
            this.value = value;
            this.numbers = new ArrayList<>(numbers);
        }

        public double getValue() {
            return value;
        }

        public List<Integer> getNumbers() {
            return new ArrayList<>(numbers);
        }
    }

    private static final class Parser {
        private final String input;
        private final List<Integer> numbers = new ArrayList<>();
        private int position = 0;

        private Parser(String input) {
            this.input = input;
        }

        private double parseExpression() {
            double result = parseTerm();

            while (true) {
                if (match('+')) {
                    result += parseTerm();
                } else if (match('-')) {
                    result -= parseTerm();
                } else {
                    return result;
                }
            }
        }

        private double parseTerm() {
            double result = parseUnary();

            while (true) {
                if (match('*')) {
                    result *= parseUnary();
                } else if (match('/')) {
                    double divisor = parseUnary();

                    if (divisor == 0.0) {
                        throw new IllegalArgumentException(
                                "Division by zero."
                        );
                    }

                    result /= divisor;
                } else {
                    return result;
                }
            }
        }

        private double parseUnary() {
            if (match('+')) {
                return parseUnary();
            }

            if (match('-')) {
                return -parseUnary();
            }

            return parsePrimary();
        }

        private double parsePrimary() {
            skipWhitespace();

            if (match('(')) {
                double result = parseExpression();

                if (!match(')')) {
                    throw new IllegalArgumentException(
                            "Missing closing parenthesis."
                    );
                }

                return result;
            }

            skipWhitespace();

            if (position < input.length()
                    && Character.isDigit(input.charAt(position))) {

                int start = position;

                while (position < input.length()
                        && Character.isDigit(input.charAt(position))) {
                    position++;
                }

                int number = Integer.parseInt(
                        input.substring(start, position)
                );

                numbers.add(number);
                return number;
            }

            throw new IllegalArgumentException(
                    "Expected a number."
            );
        }

        private boolean match(char expected) {
            skipWhitespace();

            if (position < input.length()
                    && input.charAt(position) == expected) {
                position++;
                return true;
            }

            return false;
        }

        private void skipWhitespace() {
            while (position < input.length()
                    && Character.isWhitespace(input.charAt(position))) {
                position++;
            }
        }
    }
}
