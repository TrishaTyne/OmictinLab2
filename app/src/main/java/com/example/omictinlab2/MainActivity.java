package com.example.omictinlab2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private StringBuilder currentInput = new StringBuilder();
    private BigDecimal firstNumber = BigDecimal.ZERO;
    private String operator = "";
    private boolean operatorPressed = false;
    private boolean percentPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                String buttonText = b.getText().toString();

                switch (buttonText) {
                    case "C":
                        clearDisplay();
                        break;
                    case "=":
                        calculateResult();
                        break;
                    case "sin":
                    case "cos":
                    case "tan":
                    case "√":
                        handleUnaryOperation(buttonText);
                        break;
                    case "+":
                    case "-":
                    case "*":
                    case "/":
                        handleBinaryOperation(buttonText);
                        break;
                    case "⌫": // Backspace button
                        handleBackspace();
                        break;
                    case "log":
                        onLogButtonClick(v);
                        break;
                    case "%":
                        onPercentageButtonClick(v);
                        break;
                    case "^": // Power button
                        handleBinaryOperation("^");
                        break;
                    default:
                        appendToCurrentInput(buttonText);
                        break;
                }
            }
        };

        int[] buttonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
                R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
                R.id.btn_8, R.id.btn_9, R.id.btn_add, R.id.btn_subtract,
                R.id.btn_multiply, R.id.btn_divide, R.id.btn_equals,
                R.id.btn_clear, R.id.btn_sin, R.id.btn_cos, R.id.btn_tan,
                R.id.btn_sqrt, R.id.btn_decimal, R.id.btn_backspace,
                R.id.btn_log, R.id.btn_percentage, R.id.btn_power
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void clearDisplay() {
        currentInput.setLength(0);
        display.setText("0");
        firstNumber = BigDecimal.ZERO;
        operator = "";
        operatorPressed = false;
        percentPressed = false;
    }

    private void appendToCurrentInput(String text) {
        if (operatorPressed) {
            currentInput.setLength(0); // clear if an operator was pressed
            operatorPressed = false;
        }
        currentInput.append(text);
        display.setText(currentInput.toString());
    }

    private void handleUnaryOperation(String operation) {
        if (!currentInput.toString().isEmpty()) {
            BigDecimal value = new BigDecimal(currentInput.toString());
            BigDecimal result = BigDecimal.ZERO;

            switch (operation) {
                case "sin"://Input or press a number first then press the sin button
                    double radians = Math.toRadians(value.doubleValue());
                    result = new BigDecimal(Math.sin(radians));
                    break;
                case "cos"://Input or press a number first then press the cos button
                    radians = Math.toRadians(value.doubleValue());
                    result = new BigDecimal(Math.cos(radians));
                    break;
                case "tan"://Input or press a number first then press the tan button
                    radians = Math.toRadians(value.doubleValue());
                    result = new BigDecimal(Math.tan(radians));
                    break;
                case "√"://Input or press a number first then press the sqrt button
                    result = new BigDecimal(Math.sqrt(value.doubleValue()));
                    break;
            }

            display.setText(result.stripTrailingZeros().toPlainString());
            currentInput.setLength(0); // clear input after unary operation
            currentInput.append(result.stripTrailingZeros().toPlainString());
        }
    }

    private void handleBinaryOperation(String operation) {
        if (!currentInput.toString().isEmpty()) {
            if (operation.equals("^")) {
                firstNumber = new BigDecimal(currentInput.toString());
                operator = "^";
                operatorPressed = true;
            } else {
                firstNumber = new BigDecimal(currentInput.toString());
                operator = operation;
                operatorPressed = true;
            }
        }
    }

    private void calculateResult() {
        if (!operator.isEmpty() && !currentInput.toString().isEmpty()) {
            BigDecimal secondNumber = new BigDecimal(currentInput.toString());
            BigDecimal result = BigDecimal.ZERO;

            switch (operator) {
                case "+":
                    result = firstNumber.add(secondNumber);
                    break;
                case "-":
                    result = firstNumber.subtract(secondNumber);
                    break;
                case "*":
                    result = firstNumber.multiply(secondNumber);
                    break;
                case "/":
                    if (secondNumber.compareTo(BigDecimal.ZERO) != 0) {
                        result = firstNumber.divide(secondNumber, 10, RoundingMode.HALF_UP);
                    } else {
                        display.setText("Error");
                        return;
                    }
                    break;
                case "^":
                    result = firstNumber.pow(secondNumber.intValue());
                    break;
            }

            display.setText(result.stripTrailingZeros().toPlainString());
            currentInput.setLength(0); // clear input after calculation
            currentInput.append(result.stripTrailingZeros().toPlainString());
            operator = "";
            operatorPressed = false;
            percentPressed = false;
        }
    }

    private void handleBackspace() {
        if (currentInput.length() > 0) {
            currentInput.deleteCharAt(currentInput.length() - 1);
            display.setText(currentInput.toString());
        }
    }

    public void onLogButtonClick(View view) {
        if (!currentInput.toString().isEmpty()) {
            BigDecimal value = new BigDecimal(currentInput.toString());
            double logResult = Math.log10(value.doubleValue());
            display.setText(String.valueOf(logResult));
            currentInput.setLength(0); // clear input after logarithm operation
            currentInput.append(logResult);
        }
    }

    public void onPercentageButtonClick(View view) {
        if (!currentInput.toString().isEmpty() && !percentPressed) {
            BigDecimal value = new BigDecimal(currentInput.toString());
            value = value.divide(new BigDecimal(100), 10, RoundingMode.HALF_UP);
            currentInput.setLength(0);
            currentInput.append(value.stripTrailingZeros().toPlainString());
            display.setText(currentInput.toString());
            percentPressed = true;
        }
    }

}
