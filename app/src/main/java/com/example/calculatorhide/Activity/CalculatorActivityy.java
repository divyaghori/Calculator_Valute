package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.calculatorhide.Dialog.TipsDialog;
import com.example.calculatorhide.Model.Securityitem;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.InterstitialAdManager;
import com.example.calculatorhide.database.DBController;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CalculatorActivityy extends AppCompatActivity {
    TextView workingsTV;
    TextView resultsTV, simplemsg, simplesubmsg;
    String workings = "";
    String formula = "";
    String tempFormula = "";
    private boolean isPinSet = true;
    private Activity activity = this;
    private InterstitialAdManager manager;
    private Boolean isPinConfirm;
    Securityitem getpassword;
    List<Securityitem> getpasswordsize;
    DBController db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator1);
        getWindow().setFlags(1024, 1024);
        db = new DBController(this);
        manager = new InterstitialAdManager();
        manager.fetchAd(this, false);
        simplemsg = findViewById(R.id.simplemsg);
        simplesubmsg = findViewById(R.id.simplesubmsg);
        simplesubmsg.setText(SplashActivity.resources.getString(R.string.PEqual));
        initTextViews();
        getpassword = new Securityitem();
        getpasswordsize = new ArrayList<>();
        getpasswordsize = db.getSecurity();
//        if (!MyApplication.CheckPrefs(this, MyApplication.PIN)) {
        if (getpasswordsize.size() == 0) {
            HideSpecialChar();
            isPinConfirm = false;
            ShowTipsDialog();
            simplemsg.setVisibility(View.VISIBLE);
            simplesubmsg.setVisibility(View.VISIBLE);
            isPinSet = false;
        }
    }

    private void HideSpecialChar() {
        findViewById(R.id.button_divide).setVisibility(View.INVISIBLE);
        findViewById(R.id.button_multi).setVisibility(View.INVISIBLE);
        findViewById(R.id.button_sub).setVisibility(View.INVISIBLE);
        findViewById(R.id.button_add).setVisibility(View.INVISIBLE);
        findViewById(R.id.button_dot).setVisibility(View.INVISIBLE);
    }

    public boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isLastCharIsNumber(String str) {
        if (str.isEmpty()) return true;
        char l = str.charAt(str.length() - 1);
        if (Character.isDigit(l) || l == '(' || l == ')' || l == '.') {
            return true;
        }
        return false;
    }

    private void ShowTipsDialog() {
        TipsDialog td = new TipsDialog(this);
        td.show();
    }

    private void initTextViews() {
        workingsTV = (TextView) findViewById(R.id.workingsTextView);
        resultsTV = (TextView) findViewById(R.id.resultTextView);
    }

    private void setWorkings(String givenValue) {
        if (isNumeric(givenValue) || givenValue == "(" || givenValue == ")") {
            workings = workings + givenValue;
        } else {
            if (workings.length() != 0) {
                if (!isLastCharIsNumber(workings)) {
                    workings = workings.substring(0, workings.length() - 1) + givenValue;
                } else {
                    workings = workings + givenValue;
                }
            }
        }
        workingsTV.setText(workings);
    }

    private String firstPin;

    private String AddStar(String temp) {
        StringBuilder a = new StringBuilder();
        for (int i = 0; i < temp.length(); i++) {
            a.append("*");
        }
        return a.toString();
    }

    public void equalsOnClick(View view) {
        Double result = null;
        if (!isPinSet) {
            if (!isPinConfirm) {
                if (workings.length() == 4) {
                    firstPin = workings;
                    workings = "";
                    simplemsg.setText(SplashActivity.resources.getString(R.string.ConfirmPass));
                    resultsTV.setText(AddStar(firstPin));
                    workingsTV.setText("");
                    isPinConfirm = true;
                    return;
                }
            }
            if (workings.equals(firstPin)) {
                MyApplication.SetStringToPrefs(this, MyApplication.PIN, workings);
                Intent i = new Intent(this, QuestionsActivity.class);
                i.putExtra("password", workings);
                startActivity(i);
                finish();
            } else {
                simplemsg.setText(SplashActivity.resources.getString(R.string.E4Pass));
                workings = "";
                firstPin = "";
                isPinConfirm = false;
                resultsTV.setText("");
                workingsTV.setText("");
                Toast.makeText(activity, "Set Your Password for the first time. Only 4 Digit Password Valid.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            getpassword = db.getqueans();
            if (getpassword.getPassword().equals(workings)) {
                InterstitialAd interstitialAd = manager.showIfItAvaible();
                if (interstitialAd != null) {
                    interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            Intent intent = new Intent(activity, HomeActivity.class);
                            intent.putExtra("start", false);
                            startActivity(intent);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            Intent intent = new Intent(activity, HomeActivity.class);
                            intent.putExtra("start", false);
                            startActivity(intent);
                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent();
                        }
                    });
                    interstitialAd.show(this);
                } else {
                    Intent intent = new Intent(activity, HomeActivity.class);
                    intent.putExtra("start", false);
                    startActivity(intent);
                }
            }else if(workings.equals("11223344")){
                Intent i = new Intent(CalculatorActivityy.this, ResetQuestionActivity.class);
                startActivity(i);
            }
            else{
                Toast.makeText(getApplicationContext(), "please Correct password", Toast.LENGTH_SHORT).show();
            }
        }
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
        checkForPowerOf();
        try {
            result = (double) engine.eval(formula);
        } catch (ScriptException e) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
        }
//        if (result != null)
//            resultsTV.setText(String.valueOf(result.doubleValue()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void checkForPowerOf() {
        ArrayList<Integer> indexOfPowers = new ArrayList<>();
        for (int i = 0; i < workings.length(); i++) {
            if (workings.charAt(i) == '^')
                indexOfPowers.add(i);
        }
        formula = workings;
        tempFormula = workings;
        for (Integer index : indexOfPowers) {
            changeFormula(index);
        }
        formula = tempFormula;
    }

    private void changeFormula(Integer index) {
        String numberLeft = "";
        String numberRight = "";
        for (int i = index + 1; i < workings.length(); i++) {
            if (isNumeric(workings.charAt(i)))
                numberRight = numberRight + workings.charAt(i);
            else
                break;
        }
        for (int i = index - 1; i >= 0; i--) {
            if (isNumeric(workings.charAt(i)))
                numberLeft = numberLeft + workings.charAt(i);
            else
                break;
        }
        String original = numberLeft + "^" + numberRight;
        String changed = "Math.pow(" + numberLeft + "," + numberRight + ")";
        tempFormula = tempFormula.replace(original, changed);
    }

    private boolean isNumeric(char c) {
        if ((c <= '9' && c >= '0') || c == '.')
            return true;

        return false;
    }


    public void clearOnClick(View view) {
        workingsTV.setText("");
        workings = "";
        resultsTV.setText("");
        leftBracket = true;
        if (!isPinSet) {
            simplemsg.setText(SplashActivity.resources.getString(R.string.E4Pass));
            workings = "";
            firstPin = "";
            isPinConfirm = false;
            resultsTV.setText("");
            workingsTV.setText("");
        }
    }

    boolean leftBracket = true;

//    public void bracketsOnClick(View view) {
//        if (leftBracket) {
//            setWorkings("(");
//            leftBracket = false;
//        } else {
//            setWorkings(")");
//            leftBracket = true;
//        }
//    }

//    public void powerOfOnClick(View view) {
//        setWorkings("^");
//    }

    public void divisionOnClick(View view) {
        setWorkings("/");
    }

    public void sevenOnClick(View view) {
        setWorkings("7");
    }

    public void eightOnClick(View view) {
        setWorkings("8");
    }

    public void nineOnClick(View view) {
        setWorkings("9");
    }

    public void timesOnClick(View view) {
        setWorkings("*");
    }

    public void fourOnClick(View view) {
        setWorkings("4");
    }

    public void fiveOnClick(View view) {
        setWorkings("5");
    }

    public void sixOnClick(View view) {
        setWorkings("6");
    }

    public void minusOnClick(View view) {
        setWorkings("-");
    }

    public void oneOnClick(View view) {
        setWorkings("1");
    }

    public void twoOnClick(View view) {
        setWorkings("2");
    }

    public void threeOnClick(View view) {
        setWorkings("3");
    }

    public void plusOnClick(View view) {
        setWorkings("+");
    }

    public void decimalOnClick(View view) {
        setWorkings(".");
    }

    public void zeroOnClick(View view) {
        setWorkings("0");
    }
}