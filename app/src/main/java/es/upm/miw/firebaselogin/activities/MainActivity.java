package es.upm.miw.firebaselogin.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.gson.Gson;

import es.upm.miw.firebaselogin.dialogs.AboutDialog;
import es.upm.miw.firebaselogin.dialogs.LoadingDialog;
import es.upm.miw.firebaselogin.R;
import es.upm.miw.firebaselogin.model.Result;
import es.upm.miw.firebaselogin.model.User;
import es.upm.miw.firebaselogin.utils.SingleVolley;
import es.upm.miw.firebaselogin.adapters.TriviaAdapter;
import es.upm.miw.firebaselogin.model.Trivia;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import es.upm.miw.firebaselogin.model.Record;


public class MainActivity extends AppCompatActivity {

    public final static String LOG_TAG = "MiW";
    private FirebaseAuth mFirebaseAuth;
    final static String PATH = "trivia.json";
    private FirebaseFirestore db;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 2019;
    static final int N_QUESTIONS = 5;
    static final String URL_RECURSO = "https://opentdb.com/api.php?amount=" + N_QUESTIONS;
    private RequestQueue colaPeticiones;
    Gson gson;
    private Trivia trivia;
    private TriviaAdapter triviaAdapter;
    private RecyclerView listView;
    private LoadingDialog loadingDialog;
    private User user;


    //=====================================================================================
    //                                      Live Cycle
    //=====================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        checkLogin();
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        gson = new Gson();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (colaPeticiones != null) {
            colaPeticiones.cancelAll(LOG_TAG);
        }
        Log.i(LOG_TAG, "onStop()");
    }

    //=====================================================================================
    //                                        View
    //=====================================================================================

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public void onStadistics(MenuItem item) {
        if (this.user == null) {
            Toast.makeText(this, R.string.no_conexion, Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, StadisticsActivity.class);
        intent.putExtra("User", this.user);
        startActivity(intent);
    }

    public void onAbout(MenuItem item) {
        AboutDialog aboutDialog = new AboutDialog(this);
        aboutDialog.show();
    }

    public void onPreferences(MenuItem item) {
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }

    private void setTrivia(Trivia newTrivia, Boolean finished) {
        this.trivia = newTrivia;
        this.triviaAdapter = new TriviaAdapter(newTrivia, finished);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(triviaAdapter);
        loadingDialog.dismiss();
    }

    private void setViewToEnd(Integer correctAnswers) {
        int correctPercent = Math.round(((float) correctAnswers / (float) N_QUESTIONS) * 100);
        ((ProgressBar) findViewById(R.id.pbCorrectAnswers)).setProgress(correctPercent);
        ((TextView) findViewById(R.id.tvCorrectAnswers)).setText(correctAnswers + "/" + N_QUESTIONS);
        findViewById(R.id.pbCorrectAnswers).setVisibility(View.VISIBLE);
        findViewById(R.id.tvCorrectAnswers).setVisibility(View.VISIBLE);
        findViewById(R.id.btnCheckAnswers).setVisibility(View.GONE);
    }

    public void checkAnswers(View view) {
        this.trivia.setResults(this.triviaAdapter.getQuestions());
        saveTrivia();
        int correctAnswers = 0;
        int incorrectAnswers = 0;
        int noAnswered = 0;
        for (Result result : this.trivia.getResults()) {
            if (result.getSelectedAnswer() != null) {
                if (result.getCorrectAnswer().equals(result.getSelectedAnswer())) {
                    correctAnswers++;
                    this.user.addCorrectAnswer(result.getCategory());
                } else {
                    incorrectAnswers++;
                    this.user.addIncorrectAnswer(result.getCategory());
                }
            } else {
                noAnswered++;
                this.user.addNoAnswered(result.getCategory());
            }
        }
        updateDatabase(correctAnswers, incorrectAnswers, noAnswered);
        setViewToEnd(correctAnswers);
        this.triviaAdapter.setFinished(true);
    }

    //=====================================================================================
    //                                      User Auth
    //=====================================================================================

    private void checkLogin() {
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                if (user.getDisplayName() != null) {
                    SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_name_preference", user.getDisplayName());
                    editor.apply();
                }
                getUserData();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.signed_in, Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, "onActivityResult " + getString(R.string.signed_in));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.signed_cancelled, Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, "onActivityResult " + getString(R.string.signed_cancelled));
                finish();
            }
        }
    }

    //=====================================================================================
    //                                       Database
    //=====================================================================================

    private void updateDatabase(int correctAnswers, int incorrectAnswers, int noAnswered) {
        if (mFirebaseAuth.getCurrentUser() != null) {
            db.collection("Users").document(mFirebaseAuth.getCurrentUser().getUid())
                    .update("correctAnswers", FieldValue.increment(correctAnswers),
                            "incorrectAnswers", FieldValue.increment(incorrectAnswers),
                            "noAnswered", FieldValue.increment(noAnswered),
                            "records", FieldValue.arrayUnion(new Record(correctAnswers, incorrectAnswers, noAnswered)),
                            "categories", this.user.getCategories()
                    );
        }
    }

    private void getUserData() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            DocumentReference docRef = db.collection("Users").document(mFirebaseAuth.getCurrentUser().getUid());
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        this.user = document.toObject(User.class);
                    } else {
                        Log.d(LOG_TAG, "No such document");
                    }
                } else {
                    Log.d(LOG_TAG, "get failed with ", task.getException());
                }
                checkLastRecord();
            });
        } else {
            this.user = new User();
        }
    }

    private void checkLastRecord() {
        Date lastFile = lastModifiedDate();
        Trivia savedTrivia = loadTrivia();
        Boolean finished = false;
        Boolean answersInFile = false;
        if (lastFile != null) {
            answersInFile = itsToday(lastFile);
        }
        Record lastRecord = null;
        if (this.user.getRecords().size() == 0) {
            finished = false;
        } else {
            lastRecord = this.user.getRecords().get(this.user.getRecords().size() - 1);
            if (lastRecord != null && itsToday(lastRecord.getDate())) {
                finished = true;
            }
        }

        if(finished && answersInFile){
            setTrivia(savedTrivia, true);
            setViewToEnd(lastRecord.getCorrectAnswers());
        } else {
            if(!finished && answersInFile) {
                setTrivia(savedTrivia, false);
            } else {
                if (finished){
                    setViewToEnd(lastRecord.getCorrectAnswers());
                } else {
                    SingleVolley volley;
                    volley = SingleVolley.getInstance(getApplicationContext());
                    colaPeticiones = volley.getRequestQueue();
                    doPetitions();
                }
            }
        }
    }

    //=====================================================================================
    //                                    Local Storage
    //=====================================================================================

    private void saveTrivia() {
        try {
            FileOutputStream fos = openFileOutput(PATH, Context.MODE_PRIVATE);
            fos.write(this.gson.toJson(this.trivia).getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e("Error: ", Objects.requireNonNull(e.getMessage()));
        }
    }

    private Trivia loadTrivia() {
        try {
            FileInputStream fis = openFileInput(PATH);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String string = "";
            String newLine = br.readLine();

            while (newLine != null) {
                string += newLine;
                newLine = br.readLine();
            }

            br.close();
            isr.close();
            fis.close();
            return this.gson.fromJson(string, Trivia.class);
        } catch (Exception e) {
            Log.e("Error: ", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    private Date lastModifiedDate() {
        File file = new File(getFilesDir(), PATH);
        if (file.exists()) {
            long lastModified = file.lastModified();
            return new Date(lastModified);
        } else {
            Log.i(LOG_TAG, "lastModifiedDate() - File not found");
            return null;
        }
    }


    //=====================================================================================
    //                                      Utils
    //=====================================================================================


    public static boolean itsToday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String today = sdf.format(new Date());
        String last = sdf.format(date);
        return today.equals(last);
    }

    public void doPetitions() {
        // Error listener, or null to ignore errors.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL_RECURSO, null, response -> {
                    Trivia loadTrivia = gson.fromJson(response.toString(), Trivia.class);
                    loadTrivia.setRandomAnswers();
                    saveTrivia();
                    setTrivia(loadTrivia, false);
                },
                        volleyError -> Log.e(LOG_TAG, volleyError.toString())
                );
        encolarPeticion(jsonObjectRequest);
    }

    public void encolarPeticion(JsonObjectRequest peticion) {
        if (peticion != null) {
            peticion.setTag(LOG_TAG);  // Tag for this request. Can be used to cancel all requests with this tag
            peticion.setRetryPolicy(
                    new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,  // initial timeout for the policy.
                            3,      // maximum number of retries
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            colaPeticiones.add(peticion);
        }
    }
}
