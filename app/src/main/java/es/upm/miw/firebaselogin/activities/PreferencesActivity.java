package es.upm.miw.firebaselogin.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import es.upm.miw.firebaselogin.R;
import es.upm.miw.firebaselogin.dialogs.LogoutDialog;

public class PreferencesActivity extends AppCompatActivity implements LogoutDialog.OnLogoutDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new PreferencesFragment())
                .commit();
    }

    @Override
    public void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, getString(R.string.logged_out), Toast.LENGTH_SHORT).show();
        finish();
    }

    public static class PreferencesFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            findPreference("user_name_preference").setOnPreferenceChangeListener((preference, newValue) -> changeUserName(newValue.toString()));
            findPreference("change_password_preference").setOnPreferenceClickListener(preference -> changePassword());
            findPreference("logout_preference").setOnPreferenceClickListener(preference -> onLogoutUser());
        }

        private boolean onLogoutUser(){
            LogoutDialog logoutDialog = new LogoutDialog(getContext());
            logoutDialog.setOnLogoutDialogListener((LogoutDialog.OnLogoutDialogListener) getActivity());
            logoutDialog.show();
            return true;
        }

        private boolean changePassword(){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String email = mAuth.getCurrentUser().getEmail();
            if (email == null){
                Toast.makeText(requireContext(), "No email", Toast.LENGTH_SHORT).show();
                return false;
            }
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                    });
            return true;
        }

        private boolean changeUserName(String newValue){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            if (newValue.isEmpty()){
                Toast.makeText(requireContext(), "Empty User Name", Toast.LENGTH_SHORT).show();
                return false;
            } else{
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(newValue)
                        .build();
                if (mAuth.getCurrentUser() != null){
                    mAuth.getCurrentUser().updateProfile(profileUpdates);
                }
                Toast.makeText(requireContext(), "Changed User Name", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

    }
}
