package es.upm.miw.firebaselogin.activities;

import static es.upm.miw.firebaselogin.activities.MainActivity.LOG_TAG;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import es.upm.miw.firebaselogin.R;
import es.upm.miw.firebaselogin.dialogs.ResetPasswordDialog;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView errorEditText;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        errorEditText = findViewById(R.id.textViewError);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> loginUser());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("...")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.btSignWithGoogle).setOnClickListener(v -> signInWithGoogle());
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorEditText.setText(getString(R.string.completeFields));
            errorEditText.setVisibility(View.VISIBLE);
            Toast.makeText(LoginActivity.this, getString(R.string.completeFields),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        finish();
                    } else {
                        Drawable border = getDrawable(R.drawable.edittext_border_red);
                        errorEditText.setVisibility(View.VISIBLE);
                        editTextEmail.setBackground(border);
                        editTextPassword.setBackground(border);
                        Toast.makeText(LoginActivity.this, getString(R.string.authenticationFailed),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onSignUpButtonClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void onResetPassword(View view) {
        ResetPasswordDialog resetPasswordDialog = new ResetPasswordDialog(this);
        resetPasswordDialog.show();
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.e(LOG_TAG, "Error, Google Login", e);
                Toast.makeText(LoginActivity.this, "Error, Google Login", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                            String uid = user.getUid();
                            String nombre = user.getDisplayName();
                            String email = user.getEmail();
                            Toast.makeText(LoginActivity.this, "New User", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error, Google login", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
