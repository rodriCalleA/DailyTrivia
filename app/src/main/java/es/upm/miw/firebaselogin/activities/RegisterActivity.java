package es.upm.miw.firebaselogin.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import es.upm.miw.firebaselogin.R;
import es.upm.miw.firebaselogin.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextConfirmPassword, editTextName;
    private TextView errorEditText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        errorEditText = findViewById(R.id.textViewError);

    }

    private void newUser(Task task, String name){
        if (task.isSuccessful()) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
            if (mAuth.getCurrentUser() != null){
                mAuth.getCurrentUser().updateProfile(profileUpdates);
            }
            db.collection("Users").document(mAuth.getCurrentUser().getUid())
                    .set(new User());
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(RegisterActivity.this, getString(R.string.authenticationFailed),
                    Toast.LENGTH_SHORT).show();
            errorEditText.setText(getString(R.string.authenticationFailed));
            errorEditText.setVisibility(View.VISIBLE);
        }
    }

    public void registerUser(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String name = editTextName.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (password.equals(confirmPassword)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> newUser(task, name));
        } else {
            Toast.makeText(RegisterActivity.this, getString(R.string.passwordsDosntMatch),
                    Toast.LENGTH_SHORT).show();
            errorEditText.setText(getString(R.string.passwordsDosntMatch));
            errorEditText.setVisibility(View.VISIBLE);
            Drawable border = getDrawable(R.drawable.edittext_border_red);
            editTextPassword.setBackground(border);
            editTextConfirmPassword.setBackground(border);
        }
    }
}

