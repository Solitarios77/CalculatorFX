package com.solitarios.service;

import com.solitarios.model.MainViewModel;

import java.util.EmptyStackException;
import java.util.Stack;

public class CalculatorService {
    // TODO: 将计算结果更新回Model

    private static class CalculatorServiceHolder {
        private static CalculatorService instance = new CalculatorService();
    }

    private CalculatorService() {
        if (CalculatorService.CalculatorServiceHolder.instance != null) {
            throw new RuntimeException("CalculatorService is already instanced!");
        }
    }

    public static CalculatorService getInstance() {
        return CalculatorService.CalculatorServiceHolder.instance;
    }

    private MainViewModel model = MainViewModel.getInstance();
    // 操作数栈
    private Stack<Double> operandStack = new Stack<>();
    // 操作符栈
    private Stack<Character> operatorStack = new Stack<>();

    public void putOperand(Double number) {
        operandStack.push(number);
    }

    public void putOperator(Character operator) {
        if (!operatorStack.isEmpty()) {
            tempOperator = operatorStack.pop();
        }
        operatorStack.push(operator);
    }
    private Character tempOperator;
    public void reset() {
        operatorStack.clear();
        operandStack.clear();
    }

    public double compute() throws ArithmeticException, IllegalArgumentException {
        if (tempOperator == null) {
            tempOperator = operatorStack.pop();
        }
        if (operandStack.size() >= 2) {
            double number2 = operandStack.pop();
            double number1 = operandStack.pop();
            double ret = compute(number1, number2, tempOperator);
            System.out.println(number1 + " " + tempOperator + " " + number2 + " = " + ret);
            operandStack.push(ret);
            return ret;
        }else {
            return 0;
        }
    }

    private double compute(double number1, double number2, char operator) throws ArithmeticException, IllegalArgumentException {
        switch (operator) {
            case '+':
                return number1 + number2;
            case '-':
                return number1 - number2;
            case '*':
                return number1 * number2;
            case '/': {
                if (number2 == 0) {
                    throw new ArithmeticException(number2 + " is division by Zero Error");
                }
                return number1 / number2;
            }
            default:
                throw new IllegalArgumentException(operator + " is invalid operator");
        }
    }
}
