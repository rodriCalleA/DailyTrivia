package es.upm.miw.firebaselogin.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingleVolley {

    private static SingleVolley instanciaVolley = null;

    private final RequestQueue colaPeticiones;

    private SingleVolley(Context context) {
        colaPeticiones = Volley.newRequestQueue(context);
    }

    public static SingleVolley getInstance(Context context) {
        if (instanciaVolley == null) {
            instanciaVolley = new SingleVolley(context);
        }

        return instanciaVolley;
    }

    public RequestQueue getRequestQueue() {
        return colaPeticiones;
    }

    @NonNull
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
