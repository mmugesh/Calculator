package com.example.mugeshm.calculator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;

public class MainActivity extends Activity {

    final Context context = this;
    public ButtonClickListener mClickListener;
    int flag = 0;
    Stack <Integer> mStack;
    private WebView mWebView;
    private TextView textView;
    private StringBuilder mMathString,mMathStringCopy,historyString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMathString = new StringBuilder();
        mMathStringCopy = new StringBuilder();
        historyString =   new StringBuilder();
        mWebView = (WebView) findViewById(R.id.Screen);
        mWebView.getSettings().setJavaScriptEnabled(true);
        textView = (TextView) findViewById(R.id.Formula);
        mStack = new Stack <>();
        mClickListener = new ButtonClickListener();


        int idList[] = {R.id.btnZero, R.id.btnOne, R.id.btnTwo,
                R.id.btnThree, R.id.btnFour, R.id.btnFive, R.id.btnSix,
                R.id.btnSeven, R.id.btnEight, R.id.btnNine, R.id.btnAdd,
                R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide,
                R.id.btnDot, R.id.btnClear, R.id.btnBackspace, R.id.btnEqual,
                R.id.btnSin, R.id.btnCos, R.id.btnTan, R.id.btnLn, R.id.btnLog,
                R.id.btnExp, R.id.btnPi, R.id.btnSq, R.id.btnCube, R.id.btnInv,
                R.id.btnBracketOpen, R.id.btnBracketClose, R.id.btnSqrt, R.id.btnOption};

        for (int id : idList) {
            View v = findViewById(id);
            v.setOnClickListener(mClickListener);
        }
    }

private void updateTextView() {
    textView.setText(mMathString);
}

    private void updateWebView() {
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body style=\"text-align:right; vertical-align: text-top\">");
        builder.append("<font size=7; color=6C000000><br>");
        builder.append("<script> ");
        builder.append("document.write(eval(\"");
        builder.append(mMathStringCopy.toString());
        builder.append("\"));</script>");
        builder.append("</font></body></html>");
        if(flag == 0) {
            mWebView.loadData(" ", "text/html",null);
        }
        if(flag == 1) {
            mWebView.loadDataWithBaseURL(null, builder.toString(), "text/html", "utf-8", null);
        }
    }

    private class ButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btnClear:
                    if (mMathString.length() > 0)
                        mMathString.delete(0, mMathString.length());
                    mMathStringCopy.delete(0, mMathStringCopy.length());
                    for(int i : mStack){
                        mStack.pop();
                    }
                        flag = 0;
                    break;

                case R.id.btnBackspace:
                    flag =0;
                    if(mMathString.length() > 0) {
                        mMathString.deleteCharAt(mMathString.length() - 1);
                        mMathStringCopy.deleteCharAt(mMathStringCopy.length() - 1);
                    }
                    break;

                case R.id.btnEqual:
                    flag=1;
                  historyString.append(mMathString.toString());
                    historyString.append(System.getProperty("line.separator"));
                   break;
                case  R.id.btnSin: case  R.id.btnCos:case R.id.btnTan:
                    flag = 0;
                    mMathString.append(((Button) v).getText());
                    mMathString.append("(");
                    mMathStringCopy.append("Math.");
                    mMathStringCopy.append(((Button) v).getText());
                    mMathStringCopy.append("(");
                    mStack.push(0);
                    break;
                case R.id.btnLn:
                    flag =0;
                    mMathString.append(((Button) v).getText());
                    mMathString.append("(");
                    mMathStringCopy.append("Math.log(");
                    mStack.push(0);
                    break;
                case R.id.btnSqrt:
                    flag =0;
                    mMathString.append("√");
                    mMathString.append("(");
                    mMathStringCopy.append("Math.sqrt(");
                    mStack.push(0);
                    break;

                case R.id.btnSq :
                    flag=0;
                    mMathString.append("sqr(");
                    mMathStringCopy.append("Math.pow(");
                    mStack.push(1);
                    break;
                case R.id.btnCube:
                    flag=0;
                    mMathString.append("cube(");
                    mMathStringCopy.append("Math.pow(");
                    mStack.push(2);
                    break;
                case R.id.btnInv:
                    flag=0;
                    mMathString.append("Inv(");
                    mMathStringCopy.append("Math.pow(");
                    mStack.push(3);
                    break;
                case R.id.btnOption:
                    flag=0;
                    final Dialog dialog = new Dialog(context,R.style.cust_dialog);
                    dialog.setContentView(R.layout.popup_layout);
                    dialog.setTitle("Options");
                    Button normalView = (Button) dialog.findViewById(R.id.btnNormal);
                    Button sciView = (Button) dialog.findViewById(R.id.btnScientific);
                    Button showHistory = (Button) dialog.findViewById(R.id.btnShowHistory);
                    normalView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        }
                    });
                    sciView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        }
                    });
                    showHistory.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            final Dialog dialogHistory = new Dialog(context,R.style.cust_dialog);
                            dialogHistory.setContentView(R.layout.history);
                            Button back = (Button) dialogHistory.findViewById(R.id.btnBack);
                            Button clrHistory = (Button) dialogHistory.findViewById(R.id.btnClearHistory);
                           final TextView historyStack = (TextView) dialogHistory.findViewById(R.id.list);
                            dialogHistory.show();
                            historyStack.setText(historyString);
                            back.setOnClickListener(new View.OnClickListener()

                                                    {
                                                        @Override
                                                        public void onClick(View v) {

                                                            dialogHistory.dismiss();
                                                        }
                                                    }

                            );
                            clrHistory.setOnClickListener(new View.OnClickListener()

                                                          {
                                                              @Override
                                                              public void onClick(View v) {
                                                                  historyString.delete(0, historyString.length());
                                                                  historyStack.setText(historyString);
                                                              }
                                                          }

                            );
                        }
                    });

                    dialog.show();
                    break;
                case R.id.btnLog:
                    flag = 0;
                    mMathString.append("log(");
                    mMathStringCopy.append("Math.log(");
                    mStack.push(4);
                    break;
                case R.id.btnPi:
                    flag = 0;
                    mMathString.append(((Button) v).getText());
                    mMathStringCopy.append("Math.PI");
                    break;
                case R.id.btnExp:
                    flag = 0;
                    mMathString.append(((Button) v).getText());
                    mMathString.append("(");
                    mMathStringCopy.append("Math.exp(");
                    mStack.push(0);
                    break;
                case R.id.btnBracketOpen:
                    flag=0;
                    mMathString.append("(");
                    mMathStringCopy.append("(");
                    mStack.push(0);
                    break;
                case R.id.btnBracketClose:
                    flag=0;
                    if(mStack.empty())
                    {

                        mMathString.append(")");
                        mMathStringCopy.append(")");
                        break;
                    }
                    if(!mStack.empty())
                    switch(mStack.pop()){

                        case 1:
                            mMathString.append(")");
                            mMathStringCopy.append(",2)");
                            break;
                        case 2:
                            mMathString.append(")");
                            mMathStringCopy.append(",3)");
                            break;
                        case 3:
                            mMathString.append(")");
                            mMathStringCopy.append(",-1)");
                            break;
                        case 4:
                            mMathString.append(")");
                            mMathStringCopy.append(") / Math.log(10)");
                            break;
                        default:
                            mMathString.append(")");
                            mMathStringCopy.append(")");
                            break;
                    }
                    break;

                default:
                    flag = 0;
                    mMathString.append(((Button) v).getText());
                    mMathStringCopy.append(((Button) v).getText());
            }
            updateTextView();
            updateWebView();
        }

    }
}

