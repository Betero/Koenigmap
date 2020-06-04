package com.example.koenigmap;

import android.os.AsyncTask;

public class getTask extends AsyncTask<String,String, String> {

    MyDBHelper mHelper;
    MultipleChoiceFragment mf;
    MultipleChoiceFragment mf2;
public getTask(MyDBHelper pHelper, MultipleChoiceFragment pmf){mHelper = pHelper; mf = pmf;}

@Override
protected String doInBackground(String[] params) {
       mf.mQuestionBank = mHelper.getAllQuestions(mf.difficulty);
       mf2.mQuestionBank = mHelper.getAllQuestions(mf.difficulty);
       // mf.updateQuestion();

        return "";
        }
}
