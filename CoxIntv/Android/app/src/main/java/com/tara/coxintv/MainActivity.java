package com.tara.coxintv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.tara.coxintv.models.AnswerResponse;
import com.tara.coxintv.models.dto.DtoAnswer;

import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    TextView responseText;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseText = findViewById(R.id.text_response);

        submitButton = findViewById(R.id.button_submit);
        submitButton.setOnClickListener(view -> {
            SubmitDtoAnswerService.submitNewAnswer()
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(response -> {
                                final StringBuilder sb = new StringBuilder();
                                AnswerResponse answerResponse = response.answerResponse;
                                if (response.answerResponse != null) {
                                    sb.append(answerResponse.toString() + "\n");
                                }

                                DtoAnswer answer = response.answer;
                                if (answer != null) {
                                    sb.append("\nanswer:\n" + answer.toString() + "\n");
                                }

                                runOnUiThread(() -> responseText.setText(sb.toString()));

                            },
                            throwable ->
                                    runOnUiThread(() -> responseText.setText("\nError:\n" + throwable + "\n")));
        });
    }
}