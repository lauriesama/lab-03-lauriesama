package com.example.listycitylab3;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    private static final String ARG_CITY = "arg_city";
    private static final String ARG_POSITION = "arg_position";
    interface AddCityDialogListener {
        void addCity(City city);
        void updateCity(int position, City updated);
    }

    public static AddCityFragment newInstance() {
        AddCityFragment f = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable("city", new City ("", ""));
        f.setArguments(args);
        return f;
    }
    public static AddCityFragment newInstance(City city, int position) {
        AddCityFragment f = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);   // City implements Serializable
        args.putInt(ARG_POSITION, position);
        f.setArguments(args);
        return f;
    }

    private AddCityDialogListener listener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.fragment_add_city, null);
        final EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        final EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        Bundle args = getArguments();
        final boolean isEdit = args != null && args.containsKey("arg_position");
        final int position = isEdit ? args.getInt("arg_position", -1) : -1;
        final City existing = isEdit ? (City) args.getSerializable("arg_city") : null;


        if (isEdit && existing != null) {
            editCityName.setText(existing.getName());
            editProvinceName.setText(existing.getProvince());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add or Edit City")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(isEdit ? "Save" : "Add", (dialog, which) -> {
                    String name = editCityName.getText().toString().trim();
                    String province = editProvinceName.getText().toString().trim();
                    City updated = new City(name, province);

                    if (isEdit) {
                        listener.updateCity(position, updated); // <-- this is where it's used
                    } else {
                        listener.addCity(updated);
                    }
                }).create();}
}
