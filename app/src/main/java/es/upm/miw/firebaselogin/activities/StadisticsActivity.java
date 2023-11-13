package es.upm.miw.firebaselogin.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.upm.miw.firebaselogin.R;
import es.upm.miw.firebaselogin.model.Record;
import es.upm.miw.firebaselogin.model.User;

public class StadisticsActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.user = (User) getIntent().getParcelableExtra("User", User.class);
        }
        setContentView(R.layout.activity_stadistics);

        PieChart pieChartCorrect = findViewById(R.id.pieChartCorrect);
        PieChart pieChartIncorrect = findViewById(R.id.pieChartIncorrect);
        PieChart pieChartNoAnswered = findViewById(R.id.pieChartNoAnswered);
        LineChart lineChart = findViewById(R.id.lineChart);

        setupPieChart(pieChartCorrect, "Correct Answers", this.user.calculatePercentageCorrectAnswers());
        setupPieChart(pieChartIncorrect, "Incorrect Answers", this.user.calculatePercentageIncorrectAnswers());
        setupPieChart(pieChartNoAnswered, "No Answered", this.user.calculatePercentageNoAnswered());
        setupLineChart(lineChart, this.user.getRecords());
    }

    private void setupPieChart( PieChart pieChart, String label, HashMap<String, Integer> categories) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : categories.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        Log.i("entries", entries.toString());

        PieDataSet dataSet = new PieDataSet(entries, label);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getLegend().setTextColor(Color.WHITE);
        pieChart.invalidate();
    }

    private void setupLineChart(LineChart lineChart, List<Record> records) {
        // Configurar la gráfica de líneas
        lineChart.getDescription().setEnabled(false);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        List<Entry> entriesCorrect = new ArrayList<>();
        List<Entry> entriesIncorrect = new ArrayList<>();
        List<Entry> entriesNoAnswered = new ArrayList<>();
        for (Record record : records) {
            entriesCorrect.add(new Entry(record.getDate().getTime(), record.getCorrectAnswers()));
            entriesIncorrect.add(new Entry(record.getDate().getTime(), record.getIncorrectAnswers()));
            entriesNoAnswered.add(new Entry(record.getDate().getTime(), record.getNoAnswered()));
        }

        LineDataSet dataSetCorrect = new LineDataSet(entriesCorrect, "Correct Answers");
        dataSetCorrect.setColor(Color.GREEN);
        dataSetCorrect.setValueTextColor(Color.WHITE);
        dataSetCorrect.setValueTextSize(10f);

        LineDataSet dataSetIncorrect = new LineDataSet(entriesIncorrect, "Incorrect Answers");
        dataSetIncorrect.setColor(Color.RED);
        dataSetIncorrect.setValueTextColor(Color.WHITE);
        dataSetIncorrect.setValueTextSize(10f);

        LineDataSet dataSetNoAnswered = new LineDataSet(entriesNoAnswered, "No Answered");
        dataSetNoAnswered.setColor(Color.GRAY);
        dataSetNoAnswered.setValueTextColor(Color.WHITE);
        dataSetNoAnswered.setValueTextSize(10f);

        LineData lineData = new LineData(dataSetCorrect, dataSetIncorrect, dataSetNoAnswered);
        lineChart.getLegend().setTextColor(Color.WHITE);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

}
