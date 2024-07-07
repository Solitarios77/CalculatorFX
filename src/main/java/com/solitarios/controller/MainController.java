package com.solitarios.controller;

import com.solitarios.model.MainViewModel;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // TODO: 获取事件信息，并更新Model
    private MainViewModel model = MainViewModel.getInstance();

    //    @FXML
//    private GridPane root;
    @FXML
    private GridPane buttonArea;
    @FXML
    private TextField numberTextField;
    @FXML
    private Label viewLabel;
    @FXML
    private Button btnNumber7;
    private Stage stage;


    /*
     * 常量区
     * */

    private static final byte MAX_ROW = 4;
    private static final byte MAX_COLUMN = 4;

    private static final String[] BUTTON_LABELS = {
            "7", "8", "9", "+",
            "4", "5", "6", "-",
            "1", "2", "3", "*",
            "C", "0", ".", "/"
    };
    //    private static final Button[] BUTTONS = new Button[MAX_ROW * MAX_COLUMN];
    private static final Map<String, Button> STRING_BUTTON_MAP = new HashMap<>();
    /*
     * 常量区结束
     * */

    /*
     * 构造器
     * */
    public MainController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 跟Model绑定
//        numberTextField.textProperty().bind(model.inputTextProperty());
        numberTextField.textProperty().bind(model.inputNumberProperty());
        viewLabel.textProperty().bind(model.expressionTextProperty());

        // 绑定按钮字体大小监听
        btnNumber7.widthProperty().addListener(changeButtonFontSize());
        btnNumber7.heightProperty().addListener(changeButtonFontSize());
//        root.setMinSize(300,420);
        stage.addEventHandler(EventType.ROOT, event -> {
            if (event instanceof KeyEvent) {
                KeyEvent keyEvent = ((KeyEvent) event);
                System.out.println("keyEvent = " + keyEvent);
                if (keyEvent.getText().equals("\r")) {
                    put("=");
                } else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
                    String text = keyEvent.getText();
                    Button button = findButton(text);
                    if (button != null) {
                        button.requestFocus();
                        buttonArea.requestFocus();
                    }
                    put(text);
                }
            }
        });
//        initRowAndColumnConstraints();
//        initButton();
    }

    private Button findButton(String text) {
        for (Node child : buttonArea.getChildren()) {
            Button button = (Button) child;
            if (button.getText().equals(text)) {
                return button;
            }
        }
        return null;
    }

    private ChangeListener<Number> changeButtonFontSize() {
        return (observable, oldValue, newValue) -> {
            if (newValue != null) {
                double min = btnNumber7.getWidth() < btnNumber7.getHeight() ? btnNumber7.getWidth() : btnNumber7.getHeight();
                for (Node child : buttonArea.getChildren()) {
                    Button button = (Button) child;

                    button.setFont(Font.font(min / 3));
                }
            }
        };
    }

    /*
     * 初始化表格面板
     * */
//    private void initRowAndColumnConstraints() {
//        for (int i = 0; i < MAX_ROW; i++) {
//            RowConstraints rowConstraints = new RowConstraints();
//            rowConstraints.setVgrow(Priority.ALWAYS);
//            rowConstraints.setFillHeight(true);
//            buttonArea.getRowConstraints().add(rowConstraints);
//        }
//        for (int i = 0; i < MAX_COLUMN; i++) {
//            ColumnConstraints columnConstraints = new ColumnConstraints();
//            columnConstraints.setHgrow(Priority.ALWAYS);
//            columnConstraints.setFillWidth(true);
//            buttonArea.getColumnConstraints().add(columnConstraints);
//        }
//    }

//    private void initButton() {
//        for (int i = 0; i < BUTTON_LABELS.length; i++) {
//            Button button = new Button(BUTTON_LABELS[i]);
////            button.setOnAction(event -> System.out.println(button.getText()));
//            button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
//            buttonArea.add(button, i % MAX_COLUMN, i / MAX_ROW);
//            STRING_BUTTON_MAP.put(BUTTON_LABELS[i], button);
//        }
//    }

    /*
     * 全局按键拦截器
     * TODO: 拦截按键，判断按键属于操作符还是操作数
     *       操作数的话就添加到文本框内，直到遇到操作符
     *       如果是操作符，则将文本框的操作数压到栈内
     * */

    private static final String OPERAND_REGEX = "[\\d\\.]";
    private static final String OPERATOR_REGEX = "[\\+\\-\\*\\/\\=]";

    @FXML
    private void buttonClicked(ActionEvent event) {
        Button button = (Button) event.getSource();
        String text = button.getText();
        put(text);
//        try {
//            // TODO: 如果该按钮是数字，则对Model进行更新
//            int value = Integer.parseInt(text);
//            changeNumber(value);
//        }catch (NumberFormatException e) {
//            // TODO: 如果该按钮不是数字，则继续进行判断
//        }

    }

    private void put(String text) {
        if (text == null) {
            return;
        }
        if (text.matches(OPERAND_REGEX)) {
            // operand 操作数
            model.setInputNumber(text);
        } else if (text.matches(OPERATOR_REGEX)) {
            // operator 操作符
            try {
                model.setInputOperator(text);
            } catch (ArithmeticException e) {
                showAlert(Alert.AlertType.WARNING, e.getMessage());
            } catch (IllegalArgumentException e) {
                showAlert(Alert.AlertType.WARNING, e.getMessage());
            }
        } else if ("C".matches(text)) {
            model.clear();
        }
    }

    private void showAlert(Alert.AlertType alertType, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setContentText(contentText);
        alert.show();
    }
}
