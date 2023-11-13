package es.upm.miw.firebaselogin.model;

import android.os.Parcelable;
import android.os.Parcel;

public class Category implements Parcelable {
    private Integer correctAnswers;
    private Integer incorrectAnswers;
    private Integer noAnswered;

    public Category() {
        this.correctAnswers = 0;
        this.incorrectAnswers = 0;
        this.noAnswered = 0;
    }

    public Category(Integer correctAnswers, Integer incorrectAnswers, Integer noAnswered) {
        this.correctAnswers = 0;
        this.incorrectAnswers = 0;
        this.noAnswered = 0;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void addNoAnswered() {
        this.noAnswered++;
    }

    public Integer getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(Integer incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public void addIncorrectAnswer() {
        this.incorrectAnswers++;
    }

    public Integer getNoAnswered() {
        return noAnswered;
    }

    public void setNoAnswered(Integer noAnswered) {
        this.noAnswered = noAnswered;
    }

    public void addCorrectAnswer() {
        this.correctAnswers++;
    }

    @Override
    public String toString() {
        return "Category{" +
                ", correctAnswers=" + correctAnswers +
                ", incorrectAnswers=" + incorrectAnswers +
                ", noAnswered=" + noAnswered +
                '}';
    }

    protected Category(Parcel in) {
        correctAnswers = in.readInt();
        incorrectAnswers = in.readInt();
        noAnswered = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(correctAnswers);
        dest.writeInt(incorrectAnswers);
        dest.writeInt(noAnswered);
    }
}
