package com.example.loverapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loverapplication.Model.LoverType;

import java.util.List;

public class Adapter_Spinner extends ArrayAdapter<LoverType> {
    public Adapter_Spinner(Context context, List<LoverType> typeModels) {
        super(context, 0, typeModels);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_item);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
    }

    private View createViewFromResource(int position, @Nullable View convertView, @NonNull ViewGroup parent, int resource) {
        TextView textView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resource, parent, false);
            textView = convertView.findViewById(android.R.id.text1);
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }

        LoverType typeModel = getItem(position);
        if (typeModel != null) {
            textView.setText(typeModel.getName());
        }
        return convertView;
    }
}
