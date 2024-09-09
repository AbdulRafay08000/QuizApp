package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView qTextView, sTextView, tTextView, qNumTextView;
    private RadioGroup oGroup;
    private RadioButton o1, o2, o3, o4;
    private Button nBtn, pBtn, sAnsBtn, eExamBtn;

    private List<Question> qList;
    private int uCount = 0;
    private int wCount = 0;

    private int cIndex = 0;
    private int score = 0;
    private boolean isAnsShown = false;
    private CountDownTimer cTimer;
    private int[] uAnswers;
    private boolean[] isAns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing views
        qTextView = findViewById(R.id.tvQuestion);
        sTextView = findViewById(R.id.tvScore);
        tTextView = findViewById(R.id.tvTimer);
        oGroup = findViewById(R.id.rgOptions);
        o1 = findViewById(R.id.rbOption1);
        o2 = findViewById(R.id.rbOption2);
        o3 = findViewById(R.id.rbOption3);
        o4 = findViewById(R.id.rbOption4);
        nBtn = findViewById(R.id.btnNext);
        pBtn = findViewById(R.id.btnPrev);
        sAnsBtn = findViewById(R.id.btnShowAnswer);
        eExamBtn = findViewById(R.id.btnEndExam);
        qNumTextView = findViewById(R.id.tvQuestionNumber);

        // Load questions
        loadQuestions();
        uAnswers = new int[qList.size()];
        isAns = new boolean[qList.size()];
        for (int i = 0; i < isAns.length; i++) {
            uAnswers[i] = -1; // NO ANSWER SELECTED
        }

        displayQuestion(cIndex);
        startTimer(60 * 10000); // 10 MIN

        nBtn.setOnClickListener(v -> {
            if (!isAns[cIndex]) {
                if (isAnswerCorrect()) {
                    score += 5;
                } else {
                    score -= 1;
                }
                isAns[cIndex] = true;
            }
            isAnsShown = false;
            saveUserAnswer();
            cIndex++;
            if (cIndex < qList.size()) {
                displayQuestion(cIndex);
                loadUserAnswer();
            } else {
                finishExam();
            }
        });

        pBtn.setOnClickListener(v -> {
            if (cIndex > 0) {
                saveUserAnswer();
                cIndex--;
                displayQuestion(cIndex);
                loadUserAnswer();
                isAnsShown = false;
            }
        });

        sAnsBtn.setOnClickListener(v -> {
            if (!isAnsShown) {
                Toast.makeText(MainActivity.this, "Correct Answer: " + qList.get(cIndex).getCorrectAnswer(), Toast.LENGTH_LONG).show();
                if (!isAns[cIndex]) {
                    score = Math.max(0, score - 1); // NO NEGATIVE SCORE WHEN FINAL RESULT DISPLAYED
                }
                isAnsShown = true;
            }
        });

        eExamBtn.setOnClickListener(v -> finishExam());
    }

    private void loadQuestions() {
        qList = new ArrayList<>();
        qList.add(new Question("What is the capital of Pakistan?", "Karachi", "Lahore", "Islamabad", "Peshawar", "Islamabad"));
        qList.add(new Question("Which river is the longest in Pakistan?", "Ravi", "Chenab", "Indus", "Jhelum", "Indus"));
        qList.add(new Question("Who is known as the Father of the Nation in Pakistan?", "Allama Iqbal", "Liaquat Ali Khan", "Muhammad Ali Jinnah", "Zulfikar Ali Bhutto", "Muhammad Ali Jinnah"));
        qList.add(new Question("What is the national language of Pakistan?", "Punjabi", "Sindhi", "Urdu", "Pashto", "Urdu"));
        qList.add(new Question("Which is the highest peak in Pakistan?", "Nanga Parbat", "K2", "Rakaposhi", "Tirich Mir", "K2"));
        qList.add(new Question("When did Pakistan gain independence?", "1945", "1946", "1947", "1948", "1947"));
        qList.add(new Question("Which city is known as the 'City of Lights'?", "Lahore", "Karachi", "Islamabad", "Quetta", "Karachi"));
        qList.add(new Question("What is the national flower of Pakistan?", "Rose", "Jasmine", "Sunflower", "Tulip", "Jasmine"));
        qList.add(new Question("Who was the first Prime Minister of Pakistan?", "Liaquat Ali Khan", "Muhammad Ali Jinnah", "Ayub Khan", "Zulfikar Ali Bhutto", "Liaquat Ali Khan"));
        qList.add(new Question("Which province is the largest by area in Pakistan?", "Punjab", "Sindh", "Khyber Pakhtunkhwa", "Balochistan", "Balochistan"));
        qList.add(new Question("What is the national sport of Pakistan?", "Cricket", "Hockey", "Squash", "Football", "Hockey"));
        qList.add(new Question("Which city is known as the 'Heart of Pakistan'?", "Karachi", "Lahore", "Islamabad", "Peshawar", "Lahore"));
        qList.add(new Question("What is the currency of Pakistan?", "Dollar", "Rupee", "Taka", "Dinar", "Rupee"));
        qList.add(new Question("Who wrote the national anthem of Pakistan?", "Allama Iqbal", "Hafeez Jalandhari", "Faiz Ahmed Faiz", "Ahmad Faraz", "Hafeez Jalandhari"));
        qList.add(new Question("Which sea borders Pakistan to the south?", "Red Sea", "Arabian Sea", "Mediterranean Sea", "Caspian Sea", "Arabian Sea"));
        qList.add(new Question("What is the national animal of Pakistan?", "Lion", "Tiger", "Markhor", "Elephant", "Markhor"));
        qList.add(new Question("Which city is famous for its historical Badshahi Mosque?", "Karachi", "Lahore", "Islamabad", "Multan", "Lahore"));
        qList.add(new Question("What is the total number of provinces in Pakistan?", "3", "4", "5", "6", "4"));
        qList.add(new Question("Who was the first female Prime Minister of Pakistan?", "Fatima Jinnah", "Benazir Bhutto", "Hina Rabbani Khar", "Asma Jahangir", "Benazir Bhutto"));
        qList.add(new Question("Which desert is located in Pakistan?", "Thar Desert", "Sahara Desert", "Gobi Desert", "Kalahari Desert", "Thar Desert"));
    }

    private void displayQuestion(int index) {
        if (index < 0 || index >= qList.size()) {
            Toast.makeText(MainActivity.this, "Invalid question index.", Toast.LENGTH_SHORT).show();
            return;
        }
        Question q = qList.get(index);
        qTextView.setText(q.getQuestionText());
        o1.setText(q.getOption1());
        o2.setText(q.getOption2());
        o3.setText(q.getOption3());
        o4.setText(q.getOption4());
        oGroup.clearCheck();
        sTextView.setText("Score: " + score);
        qNumTextView.setText("Question " + (index + 1));
    }

    private boolean isAnswerCorrect() {
        int selectedId = oGroup.getCheckedRadioButtonId();
        RadioButton selectedOption = findViewById(selectedId);
        if (selectedOption != null) {
            return selectedOption.getText().equals(qList.get(cIndex).getCorrectAnswer());
        }
        return false;
    }

    private void startTimer(long duration) {
        cTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                tTextView.setText("Time: " + timeFormatted);
            }

            @Override
            public void onFinish() {
                finishExam();
            }
        }.start();
    }

    private void saveUserAnswer() {
        int selectedId = oGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedOption = findViewById(selectedId);
            String answer = selectedOption.getText().toString();
            uAnswers[cIndex] = getOptionIndex(answer);
        }
    }

    private void loadUserAnswer() {
        int savedAnswerIndex = uAnswers[cIndex];
        if (savedAnswerIndex != -1) {
            switch (savedAnswerIndex) {
                case 0:
                    o1.setChecked(true);
                    break;
                case 1:
                    o2.setChecked(true);
                    break;
                case 2:
                    o3.setChecked(true);
                    break;
                case 3:
                    o4.setChecked(true);
                    break;
            }
        } else {
            oGroup.clearCheck();
        }
    }

    private int getOptionIndex(String answer) {
        if (answer.equals(o1.getText().toString())) {
            return 0;
        } else if (answer.equals(o2.getText().toString())) {
            return 1;
        } else if (answer.equals(o3.getText().toString())) {
            return 2;
        } else if (answer.equals(o4.getText().toString())) {
            return 3;
        }
        return -1;
    }

    private void finishExam() {
        if (cTimer != null) {
            cTimer.cancel();
        }

        int totalQuestions = qList.size();
        int maxScore = totalQuestions * 5; // coorect ans -> 5 score

        int correctCount = 0;
        int aq = 0;

        for (int i = 0; i < uAnswers.length; i++) {
            int savedAnswerIndex = uAnswers[i];
            if (savedAnswerIndex == -1) {
                uCount++;
            } else {
                String userAnswerText = getSelectedOptionText(savedAnswerIndex);
                String correctAnswerText = qList.get(i).getCorrectAnswer();

                if (userAnswerText.equals(correctAnswerText)) {
                    correctCount++;
                    score += 5;
                } else {
                    aq++;
                    score = Math.max(0, score - 1);
                }
            }
        }


        int percentage = (maxScore > 0) ? (score * 100 / maxScore) : 0;

        String result = "Total Score: " + score + "\nPercentage: " + percentage + "%\n" +
                "Unattempted Questions: " + uCount + "\nAttempted Question: " + aq + "\nTotal Questions: " + totalQuestions;

        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("RESULT", result);
        startActivity(intent);

        finish();
    }



    private String getSelectedOptionText(int index) {
        switch (index) {
            case 0:
                return o1.getText().toString();
            case 1:
                return o2.getText().toString();
            case 2:
                return o3.getText().toString();
            case 3:
                return o4.getText().toString();
            default:
                return "";
        }
    }
}
