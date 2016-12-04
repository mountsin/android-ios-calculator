package com.niv.example.calcaulator;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.icu.text.NumberFormat;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

public class CalcaulatorActivity extends AppCompatActivity {

    private TextView textView;
    private double firstNumber;
    private double secondNumber;
    private String operator = "";
    private AlphaAnimation buttonClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcaulator);
        textView = (TextView)findViewById(R.id.textView1);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/helvetica-light.ttf");
        textView.setTypeface(type);
        buttonClicked = new AlphaAnimation(1f, 0f);
    }

    public void onNumClick(View view) {
        Button button = (Button)view;
        long number = Long.parseLong(button.getText().toString());

        if (operator.isEmpty()) {
            firstNumber = firstNumber * 10 + number;
            displayNumberOnScreen(firstNumber);
        } else {
            secondNumber = secondNumber * 10 + number;
            displayNumberOnScreen(secondNumber);
        }
    }

    public void onActionClick(View view) {

        view.setAnimation(buttonClicked);
        Button button = (Button)view;

        if (button.getId() == R.id.buttonClear) {
            textView.setText("0");
            button.setText(R.string.buttonClear);
            operator = "";
            firstNumber = 0;
            secondNumber = 0;
        } else if (button.getId() == R.id.buttonToggleSign) {
            if (textView.getText().toString().contains("-")) {
                textView.setText(textView.getText().toString().substring(1, textView.getText().toString().length()));
            } else {
                textView.setText("-" + textView.getText());
            }
        } else if (button.getId() == R.id.buttonEquals) {
            displayNumberOnScreen(firstNumber = calcaulate());
        }
        else {
            operator = button.getText().toString();
        }

    }

    private double calcaulate() {

        if (operator.equals("+")) {
            return firstNumber + secondNumber;
        } else if (operator.equals("-")) {
            return firstNumber - secondNumber;
        } else if (operator.equals("X")) {
            return firstNumber * secondNumber;
        } else {
            return secondNumber == 0 ? 0 : firstNumber / secondNumber;
        }
    }

    private void displayNumberOnScreen(double number) {
        if(number == (long) number)
            textView.setText(String.format("%,d", (long)number));
        else
            textView.setText(String.format("%1$,.2f", number));
    }
}
