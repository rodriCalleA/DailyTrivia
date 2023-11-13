package es.upm.miw.firebaselogin.adapters;


import static es.upm.miw.firebaselogin.utils.ColorMapper.getColorName;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.upm.miw.firebaselogin.R;

public class ColorAdapter extends ArrayAdapter<String> {

    private List<String> colors;

    public ColorAdapter(Context context, List<String> colors) {
        super(context, 0, colors);
        this.colors = colors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflar el diseño de elemento de la lista
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_about, parent, false);
        }

        String currentColor = getItem(position);

        ((TextView) convertView.findViewById(R.id.textViewColorName)).setText(currentColor);
        ((ImageView) convertView.findViewById(R.id.sampleColor))
                .setColorFilter(getContext()
                        .getColor(convertView.getResources()
                        .getIdentifier(getColorName(currentColor), "color", getContext().getPackageName()))
        );

        return convertView;
    }
}
