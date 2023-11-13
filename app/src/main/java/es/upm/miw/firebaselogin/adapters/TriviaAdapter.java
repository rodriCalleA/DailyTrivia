package es.upm.miw.firebaselogin.adapters;

import static es.upm.miw.firebaselogin.utils.ColorMapper.getColorName;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.stream.Collectors;

import es.upm.miw.firebaselogin.R;
import es.upm.miw.firebaselogin.model.Result;
import es.upm.miw.firebaselogin.model.Trivia;

public class TriviaAdapter extends RecyclerView.Adapter<TriviaAdapter.ViewHolder> {

    private List<Result> questionList;
    private LayoutInflater inflater;
    private Boolean finished;

    public TriviaAdapter(Trivia trivia, Boolean finished) {
        this.questionList = trivia.getResults();
        this.finished = finished;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Result currentQuestion = questionList.get(position);
        holder.answerRadioGroup.setOnCheckedChangeListener(null);
        holder.btnClearAnswer.setOnClickListener(null);

        if (!finished) {
            holder.answerRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                if (checkedRadioButton != null) {
                    currentQuestion.setSelectedAnswer(checkedRadioButton.getText().toString());
                    Log.i("MiW", "checkedRadioButton: " + checkedRadioButton.getText().toString());
                }
            });
            holder.btnClearAnswer.setOnClickListener(v -> {
                holder.answerRadioGroup.clearCheck();
                currentQuestion.setSelectedAnswer(null);
            });
        } else {
            holder.btnClearAnswer.setVisibility(View.GONE);
        }

        holder.cardView.setStrokeColor(
                holder.itemView.getContext().getColor(
                        holder.itemView.getResources().getIdentifier(
                                getColorName(currentQuestion.getCategory()), "color",
                                holder.itemView.getContext().getPackageName())));

        holder.tvQuestion.setText(currentQuestion.getQuestion());
        if (currentQuestion.getType().equals("boolean")) {
            holder.option3RadioButton.setVisibility(View.GONE);
            holder.option4RadioButton.setVisibility(View.GONE);
        }
        List<String> answers = currentQuestion.getAllAnswers();
        int chars = 0;
        for (int i = 0; i < answers.size(); i++) {
            RadioButton option = null;
            switch (i) {
                case 0:
                    option = holder.option1RadioButton;
                    break;
                case 1:
                    option = holder.option2RadioButton;
                    break;
                case 2:
                    option = holder.option3RadioButton;
                    break;
                case 3:
                    option = holder.option4RadioButton;
                    break;
            }
            if (option != null) {
                option.setText(String.valueOf(answers.get(i)));
                chars += answers.get(i).length();
                if (finished) {
                    Log.i("MiW", "selectedAnswer: " + currentQuestion);
                    if (currentQuestion.getSelectedAnswer() != null &&
                            answers.get(i).equals(currentQuestion.getSelectedAnswer())) {
                        option.setChecked(true);
                        option.setTextColor(holder.itemView.getContext().getColor(R.color.red));
                        option.setText(String.valueOf(answers.get(i)) + " ❌️");

                    }
                    if (answers.get(i).equals(currentQuestion.getCorrectAnswer())) {
                        option.setText(String.valueOf(answers.get(i)) + " ✅️");
                        option.setTextColor(holder.itemView.getContext().getColor(R.color.green));
                    }
                    option.setEnabled(false);
                }

            }

            if (chars > 20) {
                holder.answerRadioGroup.setOrientation(RadioGroup.VERTICAL);
            }
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public List<Result> getQuestions() {
        return this.questionList;
    }

    public void setFinished(boolean b) {
        this.finished = b;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView tvQuestion;
        RadioButton option1RadioButton;
        RadioButton option2RadioButton;
        RadioButton option3RadioButton;
        RadioButton option4RadioButton;
        RadioGroup answerRadioGroup;
        ImageButton btnClearAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            option1RadioButton = itemView.findViewById(R.id.option1RadioButton);
            option2RadioButton = itemView.findViewById(R.id.option2RadioButton);
            option3RadioButton = itemView.findViewById(R.id.option3RadioButton);
            option4RadioButton = itemView.findViewById(R.id.option4RadioButton);
            answerRadioGroup = itemView.findViewById(R.id.answerRadioGroup);
            btnClearAnswer = itemView.findViewById(R.id.btnClearAnswer);
        }
    }
}
