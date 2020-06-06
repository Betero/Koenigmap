package com.example.koenigmap.Controller;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koenigmap.Adapters.CategoryAdapter;
import com.example.koenigmap.R;
import com.example.koenigmap.Model.CategoryModel;
import com.example.koenigmap.Model.QuestionModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private Dialog loadingDialog;

    private RecyclerView recyclerView;
    private List<CategoryModel> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT
                ,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_corners));
        loadingDialog.setCancelable(false);

        recyclerView = findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        final CategoryAdapter adapter = new CategoryAdapter(list);
        recyclerView.setAdapter(adapter);

        loadingDialog.show();

        myRef.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                   list.add(dataSnapshot.getValue(CategoryModel.class));
                }
                adapter.notifyDataSetChanged();
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CategoriesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class QuestionsActivity extends AppCompatActivity {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        private TextView question, noIndication;
        private LinearLayout optionContainer;
        private Button shareBtn, nextBtn;
        private int count = 0;
        private List<QuestionModel> list;
        private int position = 0;
        private int score = 0;
        private String category;
        private int setNo;
        private  Dialog loadingDialog;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_questions);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            question = findViewById(R.id.question);
            noIndication = findViewById(R.id.no_indificator);
            optionContainer = findViewById(R.id.options_container);
            shareBtn = findViewById(R.id.share_btn);
            nextBtn = findViewById(R.id.next_btn);

            category = getIntent().getStringExtra("category");
            setNo = getIntent().getIntExtra("setNo", 1);

            loadingDialog = new Dialog(this);
            loadingDialog.setContentView(R.layout.loading);
            loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_corners));
            loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT
                    ,LinearLayout.LayoutParams.WRAP_CONTENT);
            loadingDialog.setCancelable(false);


            list = new ArrayList<>();

            loadingDialog.show();

            myRef.child("SETS").child("Category").child("questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                     list.add(snapshot.getValue(QuestionModel.class));
                 }
                 if (list.size() > 0 ) {

                     for (int i = 0; i < 4; i++) {
                         optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 checkAnswer((Button) v);
                             }
                         });
                     }
                     playAnim(question, 0, list.get(position).getQuestion());
                     nextBtn.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             nextBtn.setEnabled(false);
                             nextBtn.setAlpha(0.7f);
                             enableOption(true);
                             position++;
                             if (position == list.size()) {
                                 Intent scoreIntent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                                 scoreIntent.putExtra("score", score);
                                 scoreIntent.putExtra("total", list.size());
                                 startActivity(scoreIntent);
                                 finish();
                                 return;
                             }
                             count = 0;
                             playAnim(question, 0, list.get(position).getQuestion());
                         }
                     });
                 }
                 else {
                     finish();
                     Toast.makeText(QuestionsActivity.this, "no questions", Toast.LENGTH_SHORT).show();
                 }
                 loadingDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    finish();
                }
            });

            playAnim(question,0, list.get(position).getQuestion());


        }

        private void playAnim(final View view, final int value, final String data) {

            view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                    .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (value == 0 && count < 4) {
                        String option = "";
                        if (count == 0) {
                            option = list.get(position).getOptionA();
                        } else if (count == 1) {
                            option = list.get(position).getOptionB();
                        } else if (count == 2) {
                            option = list.get(position).getOptionC();
                        } else if (count == 3) {
                            option = list.get(position).getOptionD();
                        }

                        playAnim(optionContainer.getChildAt(count), 0, option);
                        count++;
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    if (value == 0) {
                        try {
                            ((TextView) view).setText(data);
                            noIndication.setText(position+1+"/"+list.size());
                        }
                        catch (ClassCastException ex){
                            ((Button) view).setText(data);
                        }
                        view.setTag(data);
                        playAnim(view, 1, data);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void checkAnswer(Button selectOption) {
            enableOption(false);
            nextBtn.setEnabled(true);
            nextBtn.setAlpha(1);
            if (selectOption.getText().toString().equals((list.get(position).getCorrectANS()))){
                //correct
                score++;

                selectOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4caf50")));
            }
            else {
                //incorrect
                selectOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
                Button correctoption = (Button) optionContainer.findViewWithTag(list.get(position).getCorrectANS());
                correctoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4caf50")));
            }

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void enableOption(boolean enable){
                for (int i = 0; i < 4; i++) {
                    optionContainer.getChildAt(i).setEnabled(enable);
                    if (enable){

                        optionContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
                    }

                }
        }

    }
}
