package org.example;
import java.util.*;
import java.util.regex.*;
class Model {
    public double calc(String expression) {
        try {
            //проверка символов
            if (!Pattern.matches("^[0-9+\\-*/^(). ]+$", expression))
            {
                throw new IllegalArgumentException("Ошибка недопустимые символы");
            }
            //тута у нас типа низя больше чем одну сотню слагаемых
            String[] terms = expression.split("0-9+\\-*/^(). ]+");
            if (terms.length > 100) {
                throw new IllegalArgumentException("Ошибка");
            }
            //очибяка
            return eval(expression);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка вычисления");
        }
    }
    //парсим и считаем,ыыы
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
                if (index < expression.length()) throw new RuntimeException("Ошибка");
                return value;
            }
            //даем и забираем
            double parseExp() {
                double value = parseTerm();
                while (true) {
                    if      (match('+')) value += parseTerm();
                    else if (match('-')) value -= parseTerm();
                    else return value;
                }
            }
            //умножаем делим и возводим
            double parseTerm() {
                double value = parseFactor();
                while (true) {
                    if      (match('*')) value *= parseFactor();
                    else if (match('/')) value /= parseFactor();
                    else if (match('^')) value = Math.pow(value, parseFactor());
                    else return value;
                }
            }
            // смотрим на скобачки
            double parseFactor() {
                if (match('+')) return parseFactor();
                if (match('-')) return -parseFactor();
                double value;
                int startPos = this.index;
                if (match('(')) {
                    value = parseExp();
                    match(')');
                } else if ((character >= '0' && character <= '9') || character == '.') {
                    while ((character >= '0' && character <= '9') || character == '.') next();
                    value = Double.parseDouble(expression.substring(startPos, this.index));
                } else {
                    throw new RuntimeException("Ошибка");
                }
                return value;
            }
        }.parse();
    }
}
//вывод результата(как будто есть какойто косяк, но где?)
class View {
    public void displayRes(double result) {
        System.out.println("Результат: " + result);
    }
}
// процесс вычисления
class Controller {
    private Model model;
    private View view;

    public Controller() {
        this.model = new Model();
        this.view = new View();
    }
    //выводим результаты наших трудов и литра кофе o_0
    public void process(String equation) {
        double result = model.calc(equation);
        view.displayRes(result);
    }
}

// разгоняемся
public class Calculator{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Controller controller = new Controller();
        System.out.print("выражение: ");
        String equation = scanner.nextLine();
        controller.process(equation);
    }
}
