package es.upm.miw.firebaselogin.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Record implements Parcelable {
    @ServerTimestamp
    private Date date;
    private Integer correctAnswers;
    private Integer incorrectAnswers;
    private Integer noAnswered;

    public Record() {

    }

    public Record(Integer correctAnswers, Integer incorrectAnswers, Integer noAnswered) {
        this.date = new Date();
        this.correctAnswers = correctAnswers;
        this.incorrectAnswers = incorrectAnswers;
        this.noAnswered = noAnswered;
    }

    public Date getDate() {
        return date;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Integer getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(Integer incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public Integer getNoAnswered() {
        return noAnswered;
    }

    public void setNoAnswered(Integer noAnswered) {
        this.noAnswered = noAnswered;
    }

    @Override
    public String toString() {
        return "Record{" +
                "date=" + date +
                ", correctAnswers=" + correctAnswers +
                ", incorrectAnswers=" + incorrectAnswers +
                ", noAnswered=" + noAnswered +
                '}';
    }

    // Constructor Parcelable
    protected Record(Parcel in) {
        // Leer la fecha
        long timestamp = in.readLong();
        date = timestamp == -1 ? null : new Date(timestamp);

        correctAnswers = in.readInt();
        incorrectAnswers = in.readInt();
        noAnswered = in.readInt();
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    // Otros métodos Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Escribir la fecha como un timestamp
        dest.writeLong(date != null ? date.getTime() : -1);

        dest.writeInt(correctAnswers);
        dest.writeInt(incorrectAnswers);
        dest.writeInt(noAnswered);
    }
}
