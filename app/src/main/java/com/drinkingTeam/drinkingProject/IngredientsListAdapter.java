package com.drinkingTeam.drinkingProject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class IngredientsListAdapter extends ArrayAdapter<Ingredient> {

    private List<Ingredient> ingredients;

    private Context context;

    //the layout resource file for the list items
    private int resource;

    public IngredientsListAdapter(@NonNull Context context, int resource, @NonNull List<Ingredient> ingredientList) {
        super(context, resource, ingredientList);
        ingredients = ingredientList;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null, false);

        TextView units = view.findViewById(R.id.units);
        units.setText(ingredients.get(position).getUnits());
        TextView quantity = view.findViewById(R.id.quantity);
        quantity.setText(ingredients.get(position).getQuantity());
        TextView name = view.findViewById(R.id.Name);
        name.setText(ingredients.get(position).getName());

        return view;

    }
}
