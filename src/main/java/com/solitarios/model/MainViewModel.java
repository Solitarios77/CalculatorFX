package com.solitarios.model;

import com.solitarios.service.CalculatorService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MainViewModel {

    // TODO: 使用监听，将事件信息转换到服务类中，服务类更新Model
    private static class MainViewModelHolder {
        private static MainViewModel instance = new MainViewModel();
    }

    private MainViewModel() {
        if (MainViewModelHolder.instance != null) {
            throw new RuntimeException("MainViewModel is already instanced!");
        }
        initInputNumber();
        initInputOperator();
    }

    public static MainViewModel getInstance() {
        return MainViewModelHolder.instance;
    }

    // 服务层
    private CalculatorService calculatorService = CalculatorService.getInstance();
    // textField显示的文本
    private static final String DEFAULT_NUMBER = "0";
//    private StringProperty inputText = new SimpleStringProperty(DEFAULT_NUMBER);
//
//    public String getInputText() {
//        return inputText.get();
//    }
//
//    public StringProperty inputTextProperty() {
//        return inputText;
//    }

    private boolean isNewNumber = true;

//    private boolean hasPoint = false;

//    private static final String NUMBER_POINT = ".";

    //    private char point = ' ';
//    public void putNumber(String input) {
//        if (NUMBER_POINT.equals(input)) {
//            // 如果是小数点并且当前没有小数点
//            if (!hasPoint) {
//                hasPoint = true;
//                isNewNumber = false;
//                inputText.set(inputText.get() + input);
//            }
//        } else if (isNewNumber && !"0".equals(input)) {
//            // 如果是新的数并且不为0，则直接赋值
//            inputText.set(input);
//            isNewNumber = false;
//        } else if (!isNewNumber) {
//            // 如果不是新的数，则添加在末尾
//            inputText.set(inputText.get() + input);
//        }
//    }


    // 重置面板
    public void clear() {
//        inputText.set(DEFAULT_NUMBER);
        inputNumber.set(DEFAULT_NUMBER);
        expressionText.set("");
        isNewNumber = true;
        inputOperator.set("");
//        hasPoint = false;
    }


    /*
     * 重构
     * */

    // 输入数字模型
    private StringProperty inputNumber = new SimpleStringProperty(DEFAULT_NUMBER);

    public StringProperty inputNumberProperty() {
        return inputNumber;
    }

    public void setInputNumber(String inputNumber) {
        if (isNewNumber) {
            this.inputNumber.set(inputNumber);
            isNewNumber = false;
            isComputed = false;
        } else {
            this.inputNumber.set(this.inputNumber.get() + inputNumber);
        }
    }

    // 输入数字正则匹配
    private static final String INPUT_NUMBER_REGEX = "[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)";

    private void initInputNumber() {
        inputNumber.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (computeDigitCount(newValue) > 16 ||
                        !newValue.matches(INPUT_NUMBER_REGEX)) {
                    inputNumber.set(oldValue);
                }
            }
        });
    }

    private int computeDigitCount(String string) {
        if (string == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c >= '0' && c <= '9') {
                count++;
            }
        }
        return count;
    }

    // 输入操作符模型
    private StringProperty inputOperator = new SimpleStringProperty("");
    private boolean isComputed = false;

    public void setInputOperator(String inputOperator) throws ArithmeticException, IllegalArgumentException {
        if (!inputOperator.matches(OPERATOR_REGEX)) {
            throw new IllegalArgumentException(inputOperator + " is invalid input");
        }
        if (inputOperator.equals("=")) {
            if (isComputed) {
                return;
            }
            double number = Double.parseDouble(inputNumber.get());
            calculatorService.putOperand(number);
            double ret = calculatorService.compute();
            isNewNumber = true;
            this.inputOperator.set(inputOperator);
            expressionText.set("");
            inputNumber.set(ret + "");
            isComputed = true;
            return;
        }
        double number = Double.parseDouble(inputNumber.get());
        calculatorService.putOperand(number);
        calculatorService.putOperator(inputOperator.charAt(0));
        isNewNumber = true;
        expressionText.set(inputNumber.get() + " " + inputOperator);
        double ret = calculatorService.compute();
        inputNumber.set(ret + "");
        this.inputOperator.set(inputOperator);
    }

    private static final String OPERATOR_REGEX = "[\\+\\-\\*\\/\\=]";

    private void initInputOperator() {
//        inputOperator.addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                if (!newValue.matches(OPERATOR_REGEX)) {
//                    throw new IllegalArgumentException(newValue + " is invalid input");
//                }
//                double number = Double.parseDouble(inputNumber.get());
//                calculatorService.putOperand(number);
//                calculatorService.putOperator(newValue.charAt(0));
//                isNewNumber = true;
//                expressionText.set(inputNumber.get() + " " + newValue);
//                double ret = calculatorService.compute();
//                inputNumber.set(ret + "");
//            }
//        });
    }


    // 表达式文本
    private StringProperty expressionText = new SimpleStringProperty("");

    public StringProperty expressionTextProperty() {
        return expressionText;
    }
}
