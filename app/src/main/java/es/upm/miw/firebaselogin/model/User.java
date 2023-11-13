package es.upm.miw.firebaselogin.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.upm.miw.firebaselogin.utils.ColorMapper;

public class User implements Parcelable {
    private Integer correctAnswers;
    private Integer incorrectAnswers;
    private Integer noAnswered;
    private HashMap<String, Category> categories;
    private List<Record> records;

    public User() {
        this.correctAnswers = 0;
        this.incorrectAnswers = 0;
        this.noAnswered = 0;
        this.records = new ArrayList<Record>();
        this.categories = new HashMap<>();

        for (String categoryName : ColorMapper.categories) {
            this.categories.put(categoryName, new Category());
        }
    }

    public User(Integer correctAnswers, Integer incorrectAnswers, Integer noAnswered, HashMap<String, Category> categories, List<Record> records) {
        this.correctAnswers = correctAnswers;
        this.incorrectAnswers = incorrectAnswers;
        this.noAnswered = noAnswered;
        this.categories = categories;
        this.records = records;
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

    public HashMap<String, Category> getCategories() {
        return categories;
    }

    public void setCategories(HashMap<String, Category> categories) {
        this.categories = categories;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public void addRecord(Record record) {
        this.records.add(record);
    }

    public void addCorrectAnswer() {
        this.correctAnswers++;
    }

    public void addIncorrectAnswer() {
        this.incorrectAnswers++;
    }

    public void addNoAnswered() {
        this.noAnswered++;
    }

    public void addCorrectAnswer(String categoryName) {
        this.categories.get(categoryName).addCorrectAnswer();
    }

    public void addIncorrectAnswer(String categoryName) {
        this.categories.get(categoryName).addIncorrectAnswer();
    }

    public void addNoAnswered(String categoryName) {
        this.categories.get(categoryName).addNoAnswered();
    }

    public HashMap<String, Integer> calculatePercentageCorrectAnswers() {
        HashMap<String, Integer> result = new HashMap<>();

        for (Map.Entry<String, Category> entry : categories.entrySet()) {
            int total = this.correctAnswers;

            if (total > 0) {
                int percentage = (entry.getValue().getCorrectAnswers() * 100) / total;

                if (percentage > 5) {
                    result.put(entry.getKey(), percentage);
                }
            }
        }

        return result;
    }

    public HashMap<String, Integer> calculatePercentageIncorrectAnswers() {
        HashMap<String, Integer> result = new HashMap<>();

        for (Map.Entry<String, Category> entry : categories.entrySet()) {
            int total = this.incorrectAnswers;

            if (total > 0) {
                int percentage = (entry.getValue().getIncorrectAnswers() * 100) / total;

                if (percentage > 5) {
                    result.put(entry.getKey(), percentage);
                }
            }
        }

        return result;
    }

    public HashMap<String, Integer> calculatePercentageNoAnswered() {
        HashMap<String, Integer> result = new HashMap<>();

        for (Map.Entry<String, Category> entry : categories.entrySet()) {
            int total = this.noAnswered;

            if (total > 0) {
                int percentage = (entry.getValue().getNoAnswered() * 100) / total;

                if (percentage > 5) {
                    result.put(entry.getKey(), percentage);
                }
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                ", correctAnswers=" + correctAnswers +
                ", incorrectAnswers=" + incorrectAnswers +
                ", noAnswered=" + noAnswered +
                ", categories=" + categories +
                ", records=" + records +
                '}';
    }

    protected User(Parcel in) {
        correctAnswers = in.readInt();
        incorrectAnswers = in.readInt();
        noAnswered = in.readInt();

        // Leer el HashMap de categorías
        categories = new HashMap<>();
        in.readMap(categories, Category.class.getClassLoader());

        // Leer la lista de registros
        records = new ArrayList<>();
        in.readList(records, Record.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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

        // Escribir el HashMap de categorías
        dest.writeMap(categories);

        // Escribir la lista de registros
        dest.writeList(records);
    }

}
