package es.upm.miw.firebaselogin.model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.text.StringEscapeUtils;
import java.util.List;
import java.util.stream.Collectors;

public class Result {

    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("difficulty")
    @Expose
    private String difficulty;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("correct_answer")
    @Expose
    private String correctAnswer;
    @SerializedName("incorrect_answers")
    @Expose
    private List<String> incorrectAnswers;
    @SerializedName("selected_answer")
    @Expose
    private String selectedAnswer;
    @SerializedName("all_answers")
    @Expose
    private List<String> allAnswers;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return StringEscapeUtils.escapeJava(question);
    }

    public void setQuestion(String question) {
        this.question = StringEscapeUtils.escapeJava(question);
    }

    public String getCorrectAnswer() {
        return StringEscapeUtils.escapeJava(correctAnswer);
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = StringEscapeUtils.escapeJava(correctAnswer);
    }

    public List<String> getIncorrectAnswers() {
        return this.incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public List<String> getAllAnswers() {
        return this.allAnswers;
    }

    public void setAllAnswers() {
        this.allAnswers = this.incorrectAnswers;
        int random = (int) Math.floor(Math.random()*this.incorrectAnswers.size());
        this.allAnswers.add(random, this.correctAnswer);
    }

    public String getSelectedAnswer() {
        return this.selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    @Override
    public String toString() {
        String s = "Result{" +
                "category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", question='" + question + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", incorrectAnswers=" + incorrectAnswers +
                ", selectedAnswer='" + selectedAnswer + '\'' +
                ", allAnswers=" + allAnswers +
                '}';

        return StringEscapeUtils.escapeJava(s);

    }
}
