package org.example;
import java.util.*;
import java.util.regex.*;

class Model {
    public double calc(String expression) {
        try {
            // проверка символов
            if (!Pattern.matches("^[0-9+\\-*/^()|. elogxps]+$", expression)) {
                throw new IllegalArgumentException("Ошибка: недопустимые символы");
            }

            // проверка скобок
            if (!checkParentheses(expression)) {
                throw new IllegalArgumentException("Ошибка: неправильное количество скобок");
            }

            // колво чисел до 15
            String[] terms = expression.split("[+\\-*/^]");
            if (terms.length > 15) {
                throw new IllegalArgumentException("Ошибка: слишком много слагаемых (максимум 15)");
            }

            // замена ** на ^
            expression = expression.replace("**", "^");

            return eval(expression);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка вычисления: " + e.getMessage());
        }
    }

    private boolean checkParentheses(String expression) {
        int count = 0;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') count++;
            if (expression.charAt(i) == ')') count--;
            if (count < 0) return false;
        }
        return count == 0;
    }

    private double eval(String expression) {
        return new Object() {
            int index = -1, character;

            void next() { character = (++index < expression.length()) ? expression.charAt(index) : -1; }

            boolean match(int expected) {
                while (character == ' ') next();
                if (character == expected) { next(); return true; }
                return false;
            }

            double parse() {
                next();
                double value = parseExp();
                if (index < expression.length()) throw new RuntimeException("неправильный символ: " + (char)character);
                return value;
            }

            double parseExp() {
                double value = parseTerm();
                while (true) {
                    if      (match('+')) value += parseTerm();
                    else if (match('-')) value -= parseTerm();
                    else return value;
                }
            }

            double parseTerm() {
                double value = parseFactor();
                while (true) {
                    if      (match('*')) value *= parseFactor();
                    else if (match('/')) value /= parseFactor();
                    else if (match('^')) value = Math.pow(value, parseFactor());
                    else return value;
                }
            }

            double parseFactor() {
                if (match('+')) return parseFactor();
                if (match('-')) return -parseFactor();

                double value;
                int startPos = this.index;

                if (match('(')) {
                    value = parseExp();
                    if (!match(')')) throw new RuntimeException("Отсутствует закрывающая скобка");
                }
                else if (match('|')) {
                    value = factorial(parseFactor());
                }
                else if (match('e')) {
                    if (match('x') && match('p') && match('(')) {
                        value = Math.exp(parseExp());
                        if (!match(')')) throw new RuntimeException("Отсутствует закрывающая скобка после exp()");
                    } else {
                        throw new RuntimeException("Ожидалась функция exp()");
                    }
                }
                else if (match('l')) {
                    if (match('o') && match('g') && match('(')) {
                        value = Math.log(parseExp()) / Math.log(2);
                        if (!match(')')) throw new RuntimeException("Отсутствует закрывающая скобка после log()");
                    } else {
                        throw new RuntimeException("Ожидалась функция log()");
                    }
                }
                else if ((character >= '0' && character <= '9') || character == '.') {
                    while ((character >= '0' && character <= '9') || character == '.') next();
                    value = Double.parseDouble(expression.substring(startPos, this.index));
                }
                else {
                    throw new RuntimeException("неправильный символ: " + (char)character);
                }

                return value;
            }

            private double factorial(double n) {
                if (n < 0) throw new RuntimeException("Факториал отрицательного числа");
                if (n % 1 != 0) throw new RuntimeException("Факториал нецелого числа");
                int num = (int)n;
                double result = 1;
                for (int i = 2; i <= num; i++) {
                    result *= i;
                }
                return result;
            }
        }.parse();
    }
}

class View {
    public void displayRes(double result) {
        System.out.println("Результат: " + result);
    }

    public void displayError(String message) {
        System.out.println("Ошибка: " + message);
    }
}

class Controller {
    private Model model;
    private View view;

    public Controller() {
        this.model = new Model();
        this.view = new View();
    }

    public void process(String equation) {
        try {
            double result = model.calc(equation);
            view.displayRes(result);
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }
}

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Controller controller = new Controller();
        System.out.print("Введите выражение: ");
        String equation = scanner.nextLine();
        controller.process(equation);
    }
}