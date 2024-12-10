package main;

// Лабораторная работа №2 по языку программирования Java. Выполнил: Зайцева Ульяна, 3 курс, 3 группа

/**
 * Задача: Выражение может содержать числа, знаки операций, скобки. В случае, если выражение записано корректно, вычислить значение, в противном случае — вывести сообщение об ошибке.
 */

import java.util.Scanner;
import java.util.Stack;

public class laba2 {

    /**
     * В main производятся такие этапы как: 1. Ввод выражения от пользователя
     * 2. Вычисление выражения с помощью метода evaluate
     * 3. Вывод результата выражения, это либо число, либо сообщение об ошибке.
     *
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите выражение: ");
        String expression = scanner.nextLine();

        try {
            double result = evaluate(expression);
            System.out.println("Результат: " + result);
        } catch (Exception e) {
            System.err.println("Ошибка в выражении: " + e.getMessage());
        }
    }

    /**
     * В evaluate описаны методы преобразования и вычисления постфиксных выражений
     *
     */
    public static double evaluate(String expression) throws Exception {
        // Преобразовать выражение в постфиксное
        String postfix = infixToPostfix(expression);

        // Вычисление постфиксное выражение
        return evaluatePostfix(postfix);
    }

    /**
     * В infixToPostfix выполнены следующие шаги:
     * 1. Разбиение выражение на лексемы. То если на числа, операторы, скобки.
     * 2. Создание стека для хранения операторов
     * 3. Создание строки для хранения постфиксного выражения
     * 4. Обработка лексем
     * 5. Перенос оставшихся операторов из стека
     */
    public static String infixToPostfix(String expression) throws Exception {
        // Разбиение выражение на лексемы
        String[] tokens = expression.split(" ");

        // Создание стека для хранения операторов
        Stack<String> operatorStack = new Stack<>();

        // Создание строки для хранения постфиксного выражения
        StringBuilder postfix = new StringBuilder();

        // Обработка лексем
        for (String token : tokens) {
            if (isNumeric(token)) {
                postfix.append(token).append(" ");
            } else if (isOperator(token)) {
                while (!operatorStack.isEmpty() && precedence(token) <= precedence(operatorStack.peek())) {
                    postfix.append(operatorStack.pop()).append(" ");
                }
                operatorStack.push(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    postfix.append(operatorStack.pop()).append(" ");
                }
                if (operatorStack.isEmpty() || !operatorStack.pop().equals("(")) {
                    throw new Exception("Неправильное количество скобок");
                }
            } else {
                throw new Exception("Недопустимый символ: " + token);
            }
        }

        // Перенос оставшихся операторов из стека
        while (!operatorStack.isEmpty()) {
            postfix.append(operatorStack.pop()).append(" ");
        }

        return postfix.toString().trim();    }

    /**
     * В evaluatePostfix выполнены следующие шаги:
     * 1. Создание стека для хранения операндов
     * 2. Разбиение постфиксного выражения на лексемы
     * 3. Обработка лексем
     * 4. Возвращение результата
     */
    public static double evaluatePostfix(String postfix) throws Exception {
        // Создание стека для хранения операндов
        Stack<Double> operandStack = new Stack<>();

        // Разбиение постфиксного выражения на лексемы
        String[] tokens = postfix.split(" ");

        // Обработка лексем
        for (String token : tokens) {
            if (isNumeric(token)) {
                operandStack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                if (operandStack.size() < 2) {
                    throw new Exception("Недостаточно операндов для операции: " + token);
                }
                double operand2 = operandStack.pop();
                double operand1 = operandStack.pop();

                switch (token) {
                    case "+":
                        operandStack.push(operand1 + operand2);
                        break;
                    case "-":
                        operandStack.push(operand1 - operand2);
                        break;
                    case "*":
                        operandStack.push(operand1 * operand2);
                        break;
                    case "/":
                        if (operand2 == 0) {
                            throw new Exception("Деление на ноль");
                        }
                        operandStack.push(operand1 / operand2);
                        break;
                    default:
                        throw new Exception("Неизвестный оператор: " + token);
                }
            } else {
                throw new Exception("Недопустимый символ в постфиксном выражении: " + token);
            }
        }

        // Возвращение результата
        if (operandStack.size() != 1) {
            throw new Exception("Ошибка в выражении: неверный формат");
        }
        return operandStack.pop();
    }

    /**
     * В этом блоке созданы вспомогательные методы.
     * Метод isNumeric используется для проверки того является ли строка числом.
     * Метод isOperator используется для проверки того является ли строка оператором.
     * Метод precedence используется для возвращения приоритета оператора в выражении.
     */
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isOperator(String str) {
        return str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/");
    }

    public static int precedence(String operator1) {
        switch (operator1) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return 0;
        }
    }
}