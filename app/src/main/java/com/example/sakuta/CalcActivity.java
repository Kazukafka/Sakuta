package com.example.sakuta;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CalcActivity extends Activity {

    String sin_inverse, cos_inverse, tan_inverse, RorD = "RAD", function;
    String num_one = "", num_two = "", sCalculation = "", sAnswer = "", cur_operator = "", previous_ans = "";
    TextView calculation, answer;
    Double Result = 0.0, temp = 0.0, numberOne = 0.0, numberTwo = 0.0;
    Boolean num_allow = true, inv_allow = true, power_pres = false, dot_pres = false, root_pres = false;
    Boolean function_pres = false, factorial_pres = false, constant_pres = false, inverted_value = false;
    NumberFormat longformat, format;
    Button detaBaseButton;

    MyOpenHelper helper = new MyOpenHelper(this);
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        detaBaseButton = (Button) findViewById(R.id.dbbutton);

        calculation = findViewById(R.id.calculation);
        calculation.setMovementMethod(new ScrollingMovementMethod());
        answer = findViewById(R.id.answer);
        longformat = new DecimalFormat("0.#E0");
        format = new DecimalFormat("#.####");
        sin_inverse = String.valueOf(Html.fromHtml("sin<sup><small>-1</small></sup>"));
        cos_inverse = String.valueOf(Html.fromHtml("cos<sup><small>-1</small></sup>"));
        tan_inverse = String.valueOf(Html.fromHtml("tan<sup><small>-1</small></sup>"));

        final Button btn_RorD = findViewById(R.id.btn_RorD);
        btn_RorD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RorD = btn_RorD.getText().toString();
                RorD = RorD.equals("RAD") ? "DEG" : "RAD";
                btn_RorD.setText(RorD);
            }
        });

        detaBaseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent dbIntent = new Intent(CalcActivity.this,
                        RecordActivity.class);
                startActivity(dbIntent);

            }
        });
    }


    public void onClickNumber(View v) {
        if (num_allow) {
            Button bn = (Button) v;
            sCalculation += bn.getText();
            num_one += bn.getText();
            numberOne = Double.parseDouble(num_one);


            if (function_pres) {
                calculateFunction(function);
                return;
            }
            if (root_pres) {
                numberOne = Math.sqrt(numberOne);
            }
            switch (cur_operator) {
                case "":
                    if (power_pres) {
                        temp = Result + Math.pow(numberTwo, numberOne);
                    } else {
                        temp = Result + numberOne;
                    }
                    break;
                case "+":

                    if (power_pres) {
                        temp = Result + Math.pow(numberTwo, numberOne);
                    } else {
                        temp = Result + numberOne;
                    }
                    break;
                case "-":
                    if (power_pres) {
                        temp = Result - Math.pow(numberTwo, numberOne);

                    } else {
                        temp = Result - numberOne;
                    }
                    break;
                case "/":
                    try {

                        if (power_pres) {
                            temp = Result / Math.pow(numberTwo, numberOne);
                        } else {
                            temp = Result / numberOne;
                        }
                    } catch (Exception e) {
                        sAnswer = e.getMessage();
                    }
                    break;
                case "x":
                    if (power_pres) {
                        temp = Result * Math.pow(numberTwo, numberOne);
                    } else {
                        temp = Result * numberOne;


                    }
                    break;
            }
            sAnswer = format.format(temp).toString();
            updateCalculation();
        }
    }

    public void onClickOprator(View v) {
        Button ob = (Button) v;

        if (sAnswer != "") {
            if (cur_operator != "") {
                char c = getcharfromLast(sCalculation, 2);
                if (c == '+' || c == '-' || c == 'x' || c == '/') {
                    sCalculation = sCalculation.substring(0, sCalculation.length() - 3);
                }
            }
            sCalculation = sCalculation + "\n" + ob.getText() + " ";
            num_one = "";
            Result = temp;
            cur_operator = ob.getText().toString();
            updateCalculation();

            dot_pres = false;
            constant_pres = false;
            function_pres = false;
            inverted_value = false;
            num_allow = true;
            root_pres = false;
            num_two = "";
            numberTwo = 0.0;
            inv_allow = true;
            power_pres = false;
            factorial_pres = false;
        }

    }

    private char getcharfromLast(String s, int i) {
        char c = s.charAt(s.length() - i);
        return c;
    }

    public void onClickClear(View v) {
        cleardata();
    }
    public void onDotClick(View view) {

        if (!dot_pres) {

            if (num_one.length() == 0) {
                num_one = "0.";
                sCalculation += "0.";
                sAnswer = "0.";
                dot_pres = true;
                updateCalculation();
            } else {
                num_one += ".";
                sCalculation += ".";

                sAnswer += ".";
                dot_pres = true;
                updateCalculation();
            }
        }
    }
    public void cleardata() {
        sCalculation = "";
        sAnswer = "";

        cur_operator = "";
        num_one = "";
        num_two = "";
        previous_ans = "";
        Result = 0.0;
        numberOne = 0.0;

        numberTwo = 0.0;
        temp = 0.0;
        updateCalculation();
        dot_pres = false;
        num_allow = true;
        root_pres = false;
        inv_allow = true;

        power_pres = false;
        factorial_pres = false;
        function_pres = false;

        constant_pres = false;
        inverted_value = false;
    }
    public void updateCalculation() {
        calculation.setText(sCalculation);
        answer.setText(sAnswer);
    }
    public void onClickEqual(View view) {
        showresult();
    }

    //only after clikc"=" the equation will be saved
    public void showresult() {
        if (sAnswer != "" && sAnswer != previous_ans) {
            sCalculation += "\n= " + sAnswer + "\n----------\n" + sAnswer + " ";
            num_one = "";
            num_two = "";
            numberTwo = 0.0;

            numberOne = 0.0;
            Result = temp;


            previous_ans = sAnswer;


            updateCalculation();



            dot_pres = true;
            power_pres = false;
            num_allow = false;

            factorial_pres = false;


            constant_pres = false;
            function_pres = false;
            inverted_value = false;

            final SQLiteDatabase db = helper.getWritableDatabase();

            long millis = System.currentTimeMillis();
            String equation = sCalculation;
            String TimeStanp = Long.toString(millis);
            String TimeStamp = "Oppai";
            ContentValues insertValues = new ContentValues();
            insertValues.put("equation", equation);
            insertValues.put("TimeStamp", TimeStamp);
            long id = db.insert("calcBB", equation, insertValues);



        }
    }
    public void onModuloClick(View view) {
        if (sAnswer != "" && getcharfromLast(sCalculation, 1) != ' ') {
            sCalculation += "% ";
            switch (cur_operator) {
                case "":
                    temp = temp / 100;
                    break;
                case "+":
                    temp = Result + ((Result * numberOne) / 100);
                    break;
                case "-":
                    temp = Result - ((Result * numberOne) / 100);
                    break;
                case "/":
                    try {
                        temp = Result / (numberOne / 100);
                    } catch (Exception e) {
                        sAnswer = e.getMessage();
                    }
                    break;
                case "x":
                    temp = Result * (numberOne / 100);
                    break;
            }
            sAnswer = format.format(temp).toString();
            if (sAnswer.length() > 9) {
                sAnswer = longformat.format(temp).toString();
            }
            Result = temp;
            showresult();

        }
    }
    public void onPorMClick(View view) {
        if (inv_allow) {
            if (sAnswer != "" && getcharfromLast(sCalculation, 1) != ' ') {
                numberOne = numberOne * (-1);
                num_one = format.format(numberOne).toString();
                switch (cur_operator) {
                    case "":
                        temp = numberOne;
                        sCalculation = num_one;
                        break;
                    case "+":
                        temp = Result + numberOne;
                        removeuntilchar(sCalculation, ' ');
                        sCalculation += num_one;
                        break;
                    case "-":
                        temp = Result - numberOne;
                        removeuntilchar(sCalculation, ' ');
                        sCalculation += num_one;
                        break;
                    case "/":
                        try {
                            temp = Result / numberOne;
                            removeuntilchar(sCalculation, ' ');
                            sCalculation += num_one;
                        } catch (Exception e) {
                            sAnswer = e.getMessage();
                        }
                        break;
                    case "*":
                        temp = Result * numberOne;
                        removeuntilchar(sCalculation, ' ');
                        sCalculation += num_one;
                        break;
                }
                sAnswer = format.format(temp).toString();
                inverted_value = inverted_value ? false : true;
                updateCalculation();
            }
        }
    }

    public void removeuntilchar(String str, char chr) {
        char c = getcharfromLast(str, 1);
        if (c != chr) {
            str = removechar(str, 1);
            sCalculation = str;
            updateCalculation();
            removeuntilchar(str, chr);
        }
    }

    public String removechar(String str, int i) {
        char c = str.charAt(str.length() - i);
        if (c == '.' && !dot_pres) {
            dot_pres = false;
        }
        if (c == '^') {
            power_pres = false;
        }
        if (c == ' ') {
            return str.substring(0, str.length() - (i - 1));
        }
        return str.substring(0, str.length() - i);
    }

    public void onRootClick(View view) {
        Button root = (Button) view;
        if (sAnswer == "" && Result == 0 && !root_pres && !function_pres) {
            sCalculation = root.getText().toString();
            root_pres = true;
            inv_allow = false;
            updateCalculation();
        } else if (getcharfromLast(sCalculation, 1) == ' ' && cur_operator != "" && !root_pres) {
            sCalculation += root.getText().toString();
            root_pres = true;
            inv_allow = false;
            updateCalculation();
        }
    }

    public void onPowerClick(View view) {
        Button power = (Button) view;
        if (sCalculation != "" && !root_pres && !power_pres && !function_pres) {
            if (getcharfromLast(sCalculation, 1) != ' ') {
                sCalculation += power.getText().toString();
                num_two = num_one;
                numberTwo = numberOne;
                num_one = "";
                power_pres = true;
                updateCalculation();
            }
        }
    }

    public void onSquareClick(View view) {
        if (sCalculation != "" && sAnswer != "") {
            if (!root_pres && !function_pres && !power_pres && getcharfromLast(sCalculation, 1) != ' ' && getcharfromLast(sCalculation, 1) != ' ') {
                numberOne = numberOne * numberOne;
                num_one = format.format(numberOne).toString();
                if (cur_operator == "") {
                    if (num_one.length() > 9) {
                        num_one = longformat.format(numberOne);
                    }
                    sCalculation = num_one;
                    temp = numberOne;
                } else {
                    switch (cur_operator) {
                        case "+":
                            temp = Result + numberOne;
                            break;
                        case "-":
                            temp = Result - numberOne;
                            break;
                        case "/":
                            try {
                                temp = Result / numberOne;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;
                        case "x":
                            temp = Result * numberOne;
                            break;
                    }
                    removeuntilchar(sCalculation, ' ');
                    if (num_one.length() > 9) {
                        num_one = longformat.format(numberOne);
                    }
                    sCalculation += num_one;
                }
                sAnswer = format.format(temp);
                if (sAnswer.length() > 9) {
                    sAnswer = longformat.format(temp);
                }
                updateCalculation();
            }
        }
    }

    public void onClickFactorial(View view) {
        if (!sAnswer.equals("") && !factorial_pres && !root_pres && !dot_pres && !power_pres && !function_pres) {
            if (getcharfromLast(sCalculation, 1) != ' ') {
                for (int i = 1; i < Integer.parseInt(num_one); i++) {
                    numberOne *= i;
                }
                if (numberOne.equals(0.0)) {
                    numberOne = 1.0;
                }
                num_one = format.format(numberOne).toString();
                switch (cur_operator) {
                    case "":
                        Result = numberOne;
                        break;
                    case "+":
                        Result += numberOne;
                        break;
                    case "-":
                        Result -= numberOne;
                        break;
                    case "/":
                        try {
                            Result /= numberOne;
                        } catch (Exception e) {
                            sAnswer = e.getMessage();
                        }
                        break;
                    case "x":
                        Result *= numberOne;
                        break;
                }
                sAnswer = Result.toString();
                temp = Result;
                sCalculation += "! ";
                factorial_pres = true;
                num_allow = false;
                updateCalculation();
            }
        }
    }

    public void onClickInverse(View view) {
        if (!sAnswer.equals("") && !factorial_pres && !root_pres && !dot_pres && !power_pres && !function_pres) {
            if (getcharfromLast(sCalculation, 1) != ' ') {
                numberOne = Math.pow(numberOne, -1);
                num_one = format.format(numberOne).toString();
                switch (cur_operator) {
                    case "":
                        temp = numberOne;
                        sCalculation = num_one;
                        break;
                    case "+":
                        temp = Result + numberOne;
                        removeuntilchar(sCalculation, ' ');
                        sCalculation += num_one;
                        break;
                    case "-":
                        temp = Result - numberOne;
                        removeuntilchar(sCalculation, ' ');
                        sCalculation += num_one;
                        break;
                    case "/":
                        try {
                            temp = Result / numberOne;
                            removeuntilchar(sCalculation, ' ');
                            sCalculation += num_one;
                        } catch (Exception e) {
                            sAnswer = e.getMessage();
                        }
                        break;
                    case "x":
                        temp = Result * numberOne;
                        removeuntilchar(sCalculation, ' ');
                        sCalculation += num_one;
                        break;
                }
                sAnswer = format.format(temp).toString();
                updateCalculation();
            }
        }
    }

    public void onClickPIorE(View view) {
        Button btn_PIorE = (Button) view;
        num_allow = false;
        if (!root_pres && !dot_pres && !power_pres && !factorial_pres && !constant_pres && !function_pres) {
            String str_PIorE = btn_PIorE.getText().toString() + " ";
            if (!str_PIorE.equals("e ")) {
                str_PIorE = "\u03A0" + " ";
            }
            if (sCalculation == "") {
                num_one = str_PIorE;
                if (str_PIorE.equals("e ")) {
                    numberOne = Math.E;
                } else {
                    numberOne = Math.PI;
                }
                temp = numberOne;
            } else {
                if (str_PIorE.equals("e ")) {
                    numberOne = getcharfromLast(sCalculation, 1) == ' ' ? Math.E : Double.parseDouble(num_one) * Math.E;
                } else {
                    numberOne = getcharfromLast(sCalculation, 1) == ' ' ? Math.PI : Double.parseDouble(num_one) * Math.PI;
                }
                switch (cur_operator) {
                    case "":
                        temp = Result + numberOne;
                        break;

                    case "+":
                        temp = Result + numberOne;
                        break;

                    case "-":
                        temp = Result - numberOne;
                        break;
                    case "/":
                        try {
                            temp = Result / numberOne;
                        } catch (Exception e) {
                            sAnswer = e.getMessage();
                        }
                        break;
                    case "x":
                        temp = Result * numberOne;
                        break;
                }
            }
            sCalculation += str_PIorE;
            sAnswer = format.format(temp).toString();
            updateCalculation();
            constant_pres = true;
        }
    }

    public void onClickFunction(View view) {
        Button func = (Button) view;
        function = func.getHint().toString();
        if (!function_pres && !root_pres && !power_pres && !factorial_pres && !dot_pres) {
            calculateFunction(function);

        }
    }

    public void calculateFunction(String function) {
        function_pres = true;
        if (cur_operator != "" && getcharfromLast(sCalculation, 1) == ' ') {
            switch (function) {
                case "sin_inverse":
                    sCalculation += sin_inverse + "(";
                    break;
                case "cos_inverse":
                    sCalculation += cos_inverse + "(";
                    break;
                case "tan_inverse":
                    sCalculation += tan_inverse + "(";
                    break;
                default:
                    sCalculation += function + "(";
                    break;
            }
            updateCalculation();
        } else {
            switch (cur_operator) {
                case "":
                    if (sCalculation.equals("")) {
                        switch (function) {
                            case "sin_inverse":
                                sCalculation += sin_inverse + "( ";
                                break;
                            case "cos_inverse":
                                sCalculation += cos_inverse + "( ";
                                break;
                            case "tan_inverse":
                                sCalculation += tan_inverse + "( ";
                                break;
                            default:
                                sCalculation += function + "( ";
                                break;
                        }
                    } else {
                        switch (function) {
                            case "log":
                                temp = Result + Math.log10(numberOne);
                                sCalculation = "log( " + num_one;
                                break;

                            case "ln":
                                temp = Result + Math.log(numberOne);
                                sCalculation = "ln( " + num_one;
                                break;

                            case "sin":
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result + Math.sin(numberOne);
                                sCalculation = "sin( " + num_one;
                                break;
                            case "cos":
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result + Math.cos(numberOne);
                                sCalculation = "cos( " + num_one;
                                break;
                            case "tan":
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result + Math.tan(numberOne);
                                sCalculation = "tan( " + num_one;
                                break;
                            case "sin_inverse":
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result + Math.asin(numberOne);
                                sCalculation = sin_inverse + "( " + num_one;
                                break;
                            case "cos_inverse":
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result + Math.acos(numberOne);
                                sCalculation = cos_inverse + "( " + num_one;
                                break;
                            case "tan_inverse":
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result + Math.atan(numberOne);
                                sCalculation = tan_inverse + "( " + num_one;
                                break;
                        }
                    }
                    sAnswer = temp.toString();
                    updateCalculation();
                    break;

                case "+":
                    removeuntilchar(sCalculation, ' ');
                    switch (function) {
                        case "log":
                            temp = Result + Math.log10(numberOne);
                            sCalculation += "log(" + num_one;
                            break;

                        case "ln":
                            temp = Result + Math.log(numberOne);
                            sCalculation += "ln(" + num_one;
                            break;

                        case "sin":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result + Math.sin(numberOne);
                            sCalculation += "sin(" + num_one;
                            break;

                        case "sin_inverse":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result + Math.asin(numberOne);
                            sCalculation += sin_inverse + "(" + num_one;
                            break;

                        case "cos":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result + Math.cos(numberOne);
                            sCalculation += "cos(" + num_one;
                            break;

                        case "cos_inverse":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result + Math.acos(numberOne);
                            sCalculation += cos_inverse + "(" + num_one;
                            break;

                        case "tan":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result + Math.tan(numberOne);
                            sCalculation += "tan(" + num_one;
                            break;

                        case "tan_inverse":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result + Math.atan(numberOne);
                            sCalculation += tan_inverse + "(" + num_one;
                            break;
                    }
                    sAnswer = temp.toString();
                    updateCalculation();
                    break;

                case "-":
                    removeuntilchar(sCalculation, ' ');
                    switch (function) {
                        case "log":
                            temp = Result - Math.log10(numberOne);
                            sCalculation += "log(" + num_one;
                            break;

                        case "ln":
                            temp = Result - Math.log(numberOne);
                            sCalculation += "ln(" + num_one;
                            break;

                        case "sin":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result - Math.sin(numberOne);
                            sCalculation += "sin(" + num_one;
                            break;
                        case "cos":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result - Math.cos(numberOne);
                            sCalculation += "cos(" + num_one;
                            break;
                        case "tan":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result - Math.tan(numberOne);
                            sCalculation += "tan(" + num_one;
                            break;
                        case "sin_inverse":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result - Math.asin(numberOne);
                            sCalculation += sin_inverse + "(" + num_one;
                            break;
                        case "cos_inverse":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result - Math.acos(numberOne);
                            sCalculation += cos_inverse + "(" + num_one;
                            break;
                        case "tan_inverse":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result - Math.atan(numberOne);
                            sCalculation += tan_inverse + "(" + num_one;
                            break;
                    }
                    sAnswer = temp.toString();
                    updateCalculation();
                    break;

                case "x":
                    removeuntilchar(sCalculation, ' ');
                    switch (function) {
                        case "log":
                            temp = Result * Math.log10(numberOne);
                            sCalculation += "log(" + num_one;
                            break;

                        case "ln":
                            temp = Result * Math.log(numberOne);
                            sCalculation += "ln(" + num_one;
                            break;

                        case "sin":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result * Math.sin(numberOne);
                            sCalculation += "sin(" + num_one;
                            break;
                        case "cos":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result * Math.cos(numberOne);
                            sCalculation += "cos(" + num_one;
                            break;
                        case "tan":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result * Math.tan(numberOne);
                            sCalculation += "tan(" + num_one;
                            break;
                        case "sin_inverse":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result * Math.asin(numberOne);
                            sCalculation += sin_inverse + "(" + num_one;
                            break;
                        case "cos_inverse":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result * Math.acos(numberOne);
                            sCalculation += cos_inverse + "(" + num_one;
                            break;
                        case "tan_inverse":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result * Math.atan(numberOne);
                            sCalculation += tan_inverse + "(" + num_one;
                            break;
                    }
                    sAnswer = temp.toString();
                    updateCalculation();
                    break;

                case "/":
                    removeuntilchar(sCalculation, ' ');
                    switch (function) {
                        case "log":
                            try {
                                temp = Result / Math.log10(numberOne);
                                sCalculation += "log(" + num_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;

                        case "ln":
                            try {
                                temp = Result / Math.log(numberOne);
                                sCalculation += "ln(" + num_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;

                        case "sin":
                            try {
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result / Math.sin(numberOne);
                                sCalculation += "sin(" + num_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;
                        case "cos":
                            try {
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result / Math.cos(numberOne);
                                sCalculation += "cos(" + num_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;
                        case "tan":
                            try {
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result / Math.tan(numberOne);
                                sCalculation += "tan(" + num_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;
                        case "sin_inverse":
                            try {
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result / Math.asin(numberOne);
                                sCalculation += sin_inverse + "(" + num_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;
                        case "cos_inverse":
                            try {
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result / Math.acos(numberOne);
                                sCalculation += cos_inverse + "(" + num_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;
                        case "tan_inverse":
                            try {
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result / Math.atan(numberOne);
                                sCalculation += tan_inverse + "(" + num_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;
                    }
                    sAnswer = temp.toString();
                    updateCalculation();
                    break;
            }
        }
    }

    public void onClickDelete(View view) {
        if (function_pres) {
            DeleteFunction();
            return;
        }
        if (root_pres) {
            removeRoot();
            return;
        }
        if (power_pres) {
            removePower();
            return;
        }
        if (sAnswer != "") {
            if (getcharfromLast(sCalculation, 1) != ' ') {
                if (num_one.length() < 2 && cur_operator != "") {
                    num_one = "";
                    temp = Result;
                    sAnswer = format.format(Result).toString();

                    sCalculation = removechar(sCalculation, 1);
                    updateCalculation();
                } else {
                    switch (cur_operator) {
                        case "":
                            if (inverted_value) {
                                sAnswer = sAnswer.substring(1, sAnswer.length());
                                sCalculation = sCalculation.substring(1, sAnswer.length());
                                updateCalculation();
                            }
                            if (sCalculation.length() < 2) {
                                cleardata();
                            } else {
                                if (getcharfromLast(sCalculation, 1) == '.') {
                                    dot_pres = false;
                                }
                                num_one = removechar(num_one, 1);
                                numberOne = Double.parseDouble(num_one);
                                temp = numberOne;
                                sCalculation = num_one;
                                sAnswer = num_one;
                                updateCalculation();
                            }
                            break;

                        case "+":
                            if (inverted_value) {
                                numberOne = numberOne * (-1);
                                num_one = format.format(numberOne).toString();
                                temp = Result + numberOne;
                                sAnswer = format.format(temp).toString();
                                removeuntilchar(sCalculation, ' ');
                                sCalculation += num_one;
                                updateCalculation();
                                inverted_value = inverted_value ? false : true;
                            }
                            if (getcharfromLast(sCalculation, 1) == '.') {
                                dot_pres = false;
                            }
                            num_one = removechar(num_one, 1);
                            if (num_one.length() == 1 && num_one == ".") {
                                numberOne = Double.parseDouble(num_one);
                            }
                            numberOne = Double.parseDouble(num_one);
                            temp = Result + numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                            break;

                        case "-":
                            if (inverted_value) {
                                numberOne = numberOne * (-1);
                                num_one = format.format(numberOne).toString();
                                temp = Result - numberOne;
                                sAnswer = format.format(temp).toString();
                                removeuntilchar(sCalculation, ' ');
                                sCalculation += num_one;
                                updateCalculation();
                                inverted_value = inverted_value ? false : true;
                            }
                            if (getcharfromLast(sCalculation, 1) == '.') {
                                dot_pres = false;
                            }
                            num_one = removechar(num_one, 1);
                            if (num_one.length() == 1 && num_one == ".") {
                                numberOne = Double.parseDouble(num_one);
                            }
                            numberOne = Double.parseDouble(num_one);
                            temp = Result - numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                            break;
                        case "/":
                            try {
                                if (inverted_value) {
                                    numberOne = numberOne * (-1);
                                    num_one = format.format(numberOne).toString();
                                    temp = Result / numberOne;
                                    sAnswer = format.format(temp).toString();
                                    removeuntilchar(sCalculation, ' ');
                                    sCalculation += num_one;
                                    updateCalculation();
                                    inverted_value = inverted_value ? false : true;
                                }
                                if (getcharfromLast(sCalculation, 1) == '.') {
                                    dot_pres = false;
                                }
                                num_one = removechar(num_one, 1);
                                if (num_one.length() == 1 && num_one == ".") {
                                    numberOne = Double.parseDouble(num_one);
                                }
                                numberOne = Double.parseDouble(num_one);
                                temp = Result / numberOne;
                                sAnswer = format.format(temp).toString();
                                sCalculation = removechar(sCalculation, 1);
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            updateCalculation();
                            break;

                        case "x":
                            if (inverted_value) {
                                numberOne = numberOne * (-1);
                                num_one = format.format(numberOne).toString();
                                temp = Result * numberOne;
                                sAnswer = format.format(temp).toString();
                                removeuntilchar(sCalculation, ' ');
                                sCalculation += num_one;
                                updateCalculation();
                                inverted_value = inverted_value ? false : true;
                            }
                            if (getcharfromLast(sCalculation, 1) == '.') {
                                dot_pres = false;
                            }
                            num_one = removechar(num_one, 1);
                            if (num_one.length() == 1 && num_one == ".") {
                                numberOne = Double.parseDouble(num_one);
                            }
                            numberOne = Double.parseDouble(num_one);
                            temp = Result * numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                            break;
                    }
                }
            }
        }
    }

    public void removePower() {
        if (sAnswer != "" && sCalculation != "") {
            switch (cur_operator) {
                case "":
                    if (getcharfromLast(sCalculation, 1) == '^') {
                        sCalculation = removechar(sCalculation, 1);
                        num_one = num_two;

                        numberOne = Double.parseDouble(num_one);
                        num_two = "";
                        numberTwo = 0.0;

                        updateCalculation();
                    } else if (getcharfromLast(sCalculation, 2) == '^') {
                        num_one = "";

                        numberOne = 0.0;
                        temp = numberTwo;


                        sAnswer = format.format(temp).toString();
                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    } else {
                        if (getcharfromLast(sCalculation, 1) == '.') {
                            dot_pres = false;
                        }
                        num_one = removechar(num_one, 1);
                        numberOne = Double.parseDouble(num_one);
                        temp = Math.pow(numberTwo, numberOne);
                        sAnswer = format.format(temp).toString();

                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    }
                    break;

                case "+":
                    if (getcharfromLast(sCalculation, 1) == '^') {
                        sCalculation = removechar(sCalculation, 1);
                        num_one = num_two;
                        numberOne = Double.parseDouble(num_one);

                        num_two = "";
                        numberTwo = 0.0;
                        updateCalculation();
                    } else if (getcharfromLast(sCalculation, 2) == '^') {
                        num_one = "";
                        numberOne = 0.0;
                        temp = Result + numberTwo;

                        sAnswer = format.format(temp).toString();
                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    } else {
                        if (getcharfromLast(sCalculation, 1) == '.') {
                            dot_pres = false;
                        }
                        num_one = removechar(num_one, 1);

                        numberOne = Double.parseDouble(num_one);
                        temp = Result + Math.pow(numberTwo, numberOne);


                        sAnswer = format.format(temp).toString();
                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    }
                    break;

                case "-":
                    if (getcharfromLast(sCalculation, 1) == '^') {
                        sCalculation = removechar(sCalculation, 1);
                        num_one = num_two;
                        numberOne = Double.parseDouble(num_one);
                        num_two = "";
                        numberTwo = 0.0;

                        updateCalculation();
                    } else if (getcharfromLast(sCalculation, 2) == '^') {
                        num_one = "";


                        numberOne = 0.0;
                        temp = Result - numberTwo;

                        sAnswer = format.format(temp).toString();
                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    } else {
                        if (getcharfromLast(sCalculation, 1) == '.') {
                            dot_pres = false;
                        }
                        num_one = removechar(num_one, 1);
                        numberOne = Double.parseDouble(num_one);
                        temp = Result - Math.pow(numberTwo, numberOne);
                        sAnswer = format.format(temp).toString();
                        sCalculation = removechar(sCalculation, 1);

                        updateCalculation();
                    }
                    break;
                case "/":
                    try {
                        if (getcharfromLast(sCalculation, 1) == '^') {
                            sCalculation = removechar(sCalculation, 1);
                            num_one = num_two;
                            numberOne = Double.parseDouble(num_one);
                            num_two = "";

                            numberTwo = 0.0;
                            updateCalculation();
                        } else if (getcharfromLast(sCalculation, 2) == '^') {
                            num_one = "";
                            numberOne = 0.0;
                            temp = Result / numberTwo;
                            sAnswer = format.format(temp).toString();
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                        } else {
                            if (getcharfromLast(sCalculation, 1) == '.') {
                                dot_pres = false;
                            }
                            num_one = removechar(num_one, 1);
                            numberOne = Double.parseDouble(num_one);
                            temp = Result / Math.pow(numberTwo, numberOne);
                            sAnswer = format.format(temp).toString();
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                        }
                    } catch (Exception e) {
                        sAnswer = e.getMessage();
                    }
                    updateCalculation();
                    break;
                case "x":
                    if (getcharfromLast(sCalculation, 1) == '^') {
                        sCalculation = removechar(sCalculation, 1);
                        num_one = num_two;
                        numberOne = Double.parseDouble(num_one);
                        num_two = "";
                        numberTwo = 0.0;
                        updateCalculation();
                    } else if (getcharfromLast(sCalculation, 2) == '^') {
                        num_one = "";
                        numberOne = 0.0;
                        temp = Result * numberTwo;
                        sAnswer = format.format(temp).toString();
                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    } else {
                        if (getcharfromLast(sCalculation, 1) == '.') {
                            dot_pres = false;
                        }
                        num_one = removechar(num_one, 1);
                        numberOne = Double.parseDouble(num_one);
                        temp = Result * Math.pow(numberTwo, numberOne);
                        sAnswer = format.format(temp).toString();
                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    }
                    break;


            }
        }
    }

    public void removeRoot() {
        if (getcharfromLast(sCalculation, 1) != ' ') {
            if (String.valueOf(getcharfromLast(sCalculation, 1)).equals("\u221A")) {
                sCalculation = removechar(sCalculation, 1);
                root_pres = false;
                inv_allow = true;
                updateCalculation();
            }
            if (sAnswer != "") {
                if (num_one.length() < 2 && cur_operator != "") {
                    num_one = "";
                    numberOne = Result;
                    temp = Result;
                    sAnswer = format.format(Result).toString();
                    sCalculation = removechar(sCalculation, 1);
                    updateCalculation();
                } else {
                    switch (cur_operator) {
                        case "":
                            if (sCalculation.length() <= 2) {
                                cleardata();
                            } else {
                                if (getcharfromLast(sCalculation, 1) == '.') {
                                    dot_pres = false;
                                }
                                num_one = removechar(num_one, 1);
                                numberOne = Double.parseDouble(num_one);
                                numberOne = Math.sqrt(numberOne);
                                temp = numberOne;
                                sAnswer = format.format(temp).toString();
                                sCalculation = "\u221A" + num_one;
                                updateCalculation();
                            }
                            break;

                        case "+":
                            if (getcharfromLast(sCalculation, 1) == '.') {
                                dot_pres = false;
                            }
                            num_one = removechar(num_one, 1);
                            if (num_one.length() == 1 && num_one == ".") {
                                numberOne = Double.parseDouble(num_one);
                            }
                            numberOne = Double.parseDouble(num_one);
                            numberOne = Math.sqrt(numberOne);
                            temp = Result + numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                            break;

                        case "-":
                            if (getcharfromLast(sCalculation, 1) == '.') {
                                dot_pres = false;
                            }
                            num_one = removechar(num_one, 1);
                            if (num_one.length() == 1 && num_one == ".") {
                                numberOne = Double.parseDouble(num_one);
                            }
                            numberOne = Double.parseDouble(num_one);
                            numberOne = Math.sqrt(numberOne);
                            temp = Result - numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                            break;

                        case "x":
                            if (getcharfromLast(sCalculation, 1) == '.') {
                                dot_pres = false;
                            }
                            num_one = removechar(num_one, 1);
                            if (num_one.length() == 1 && num_one == ".") {
                                numberOne = Double.parseDouble(num_one);
                            }
                            numberOne = Double.parseDouble(num_one);
                            numberOne = Math.sqrt(numberOne);
                            temp = Result * numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                            break;

                        case "/":
                            try {
                                if (getcharfromLast(sCalculation, 1) == '.') {
                                    dot_pres = false;
                                }
                                num_one = removechar(num_one, 1);
                                if (num_one.length() == 1 && num_one == ".") {
                                    numberOne = Double.parseDouble(num_one);
                                }
                                numberOne = Double.parseDouble(num_one);
                                numberOne = Math.sqrt(numberOne);
                                temp = Result + numberOne;
                                sAnswer = format.format(temp).toString();
                                sCalculation = removechar(sCalculation, 1);
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            updateCalculation();
                            break;
                    }
                }
            }
        }
    }

    public void DeleteFunction() {
        if (cur_operator == "") {

            if (getcharfromLast(sCalculation, 1) == ' ') {
                cleardata();
            } else if (getcharfromLast(sCalculation, 2) == ' ') {
                cleardata();
            } else {
                sCalculation = removechar(sCalculation, 1);
                num_one = removechar(num_one, 1);
                numberOne = Double.parseDouble(num_one);
                calculateFunction(function);
            }
            updateCalculation();
        } else {
            if (getcharfromLast(sCalculation, 1) == '(') {
                removeuntilchar(sCalculation, ' ');
                function_pres = false;

            } else if (getcharfromLast(sCalculation, 2) == '(') {
                sCalculation = removechar(sCalculation, 1);
                num_one = "";
                temp = Result;
                sAnswer = format.format(Result).toString();
            } else {
                sCalculation = removechar(sCalculation, 1);
                num_one = removechar(num_one, 1);
                numberOne = Double.parseDouble(num_one);

                calculateFunction(function);
            }
            updateCalculation();
        }
    }
}

