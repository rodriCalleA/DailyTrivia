package es.upm.miw.firebaselogin.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import es.upm.miw.firebaselogin.R;
import android.widget.ListView;
import java.util.List;

import es.upm.miw.firebaselogin.adapters.ColorAdapter;
import es.upm.miw.firebaselogin.utils.ColorMapper;

public class AboutDialog extends Dialog {

    public AboutDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_about);

        ListView listView = findViewById(R.id.listViewColors);

        List<String> categories = ColorMapper.getCategories();

        ColorAdapter adapter = new ColorAdapter(getContext(), categories);
        listView.setAdapter(adapter);

    }
}
