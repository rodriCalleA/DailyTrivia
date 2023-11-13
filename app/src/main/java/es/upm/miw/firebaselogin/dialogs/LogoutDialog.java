package es.upm.miw.firebaselogin.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import es.upm.miw.firebaselogin.R;

public class LogoutDialog extends Dialog {

    private OnLogoutDialogListener onLogoutDialogListener;

    public LogoutDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_logout);

        Button loginButton = findViewById(R.id.btnLogout);
        loginButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getContext(), getContext().getString(R.string.logged_out), Toast.LENGTH_SHORT).show();
            onLogoutDialogListener.logoutUser();
        });

        Button logoutButton = findViewById(R.id.btnCancel);
        logoutButton.setOnClickListener(view -> cancelLogout());
    }


    public void cancelLogout() {
        dismiss();
    }

    public void setOnLogoutDialogListener(OnLogoutDialogListener onLogoutDialogListener) {
        this.onLogoutDialogListener = onLogoutDialogListener;
    }

    public interface OnLogoutDialogListener {
        void logoutUser();
    }

}
