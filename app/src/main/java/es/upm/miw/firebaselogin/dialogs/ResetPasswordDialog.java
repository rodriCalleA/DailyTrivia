package es.upm.miw.firebaselogin.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import es.upm.miw.firebaselogin.R;


public class ResetPasswordDialog extends Dialog {

    private EditText etEmail;

    public ResetPasswordDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_resetpassword);

        Button resetPasswordButton = findViewById(R.id.btnCancel);
        resetPasswordButton.setOnClickListener(view -> cancelResetPassword());

        Button cancelButton = findViewById(R.id.btnRestPassword);
        cancelButton.setOnClickListener(view -> resetPassword());

        etEmail = findViewById(R.id.editTextEmail);
    }

    private void cancelResetPassword() {
        dismiss();
    }

    public void resetPassword() {
        if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), getContext().getString(R.string.introduce_your_email), Toast.LENGTH_SHORT).show();
        } else {
            String emailAddress = etEmail.getText().toString();
            FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), getContext().getString(R.string.email_sent), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
