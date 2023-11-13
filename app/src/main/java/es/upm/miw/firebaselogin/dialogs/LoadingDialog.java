package es.upm.miw.firebaselogin.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import es.upm.miw.firebaselogin.R;

public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context);
        setCancelable(false); // Para evitar que se cierre tocando fuera del diálogo
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
    }
}

