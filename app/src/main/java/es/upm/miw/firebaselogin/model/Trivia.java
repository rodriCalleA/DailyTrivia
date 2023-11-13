package es.upm.miw.firebaselogin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trivia {

    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("results")
    @Expose
    private List<Result> results;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void setRandomAnswers(){
        for(Result result : results){
            result.setAllAnswers();
        }
    }

    @Override
    public String toString() {
        return "Trivia{" +
                "responseCode=" + responseCode +
                ", results=" + results +
                '}';
    }
}
