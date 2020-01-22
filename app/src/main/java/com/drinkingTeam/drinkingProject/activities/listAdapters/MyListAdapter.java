package com.drinkingTeam.drinkingProject.activities.listAdapters;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.drinkingTeam.drinkingProject.types.Drink;
import com.drinkingTeam.drinkingProject.R;
import com.drinkingTeam.drinkingProject.activities.DrinksDisplayActivity;
import com.drinkingTeam.drinkingProject.entities.DrinkEntity;
import com.drinkingTeam.drinkingProject.tables.DrinksDbHelper;
import com.drinkingTeam.drinkingProject.tables.IngredientsDbHelper;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Belal on 9/14/2017.
 */

//we need to extend the ArrayAdapter class as we are building an adapter
public class MyListAdapter extends ArrayAdapter<Drink> {

    //the list values in the List of type hero
    private List<Drink> drinkList;
    private List<Drink> favsList;
    private Button seeMore;

    private DrinksDbHelper drinkDb;
    private IngredientsDbHelper ingredientsDb;

    private Context context;

    //the layout resource file for the list items
    private int resource;


    //constructor initializing the values
    public MyListAdapter(Context context, int resource, List<Drink> drinkList, List<Drink> favs) {
        super(context, resource, drinkList);
        this.context = context;
        this.resource = resource;
        this.drinkList = drinkList;
        this.favsList = favs;
        drinkDb= new DrinksDbHelper(context);
        ingredientsDb= new IngredientsDbHelper(context);

    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null, false);

        System.out.println("adapter size " + drinkList.size());
        if(drinkList.size() < 1) return new View(context);

        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewTeam = view.findViewById(R.id.textViewTeam);
        ImageButton imageButton = view.findViewById(R.id.favourites_Button);

        final Drink drink = drinkList.get(position);
        final boolean isFavourite = checkIfIsFavourite(drink,favsList);

        if(isFavourite) {
            imageButton.setActivated(true);
        }
        seeMore = view.findViewById(R.id.buttonSeeMore);
        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent displayDrink = new Intent(context, DrinksDisplayActivity.class);
                Gson gson = new Gson();
                String drinkToTransfer = gson.toJson(drink);
                displayDrink.putExtra("Drink",drinkToTransfer);
                displayDrink.putExtra("favourite", isFavourite);
                context.startActivity(displayDrink);
            }
        });

        byte[] base64converted = Base64.decode(drink.getImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(base64converted, 0, base64converted.length);

        imageView.setImageBitmap(bitmap);
        textViewName.setText(drink.getName());
        textViewTeam.setText("");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favDrink(position,view);
            }
        });

        return view;
    }

    private void favDrink(final int position,View view) {
        ImageButton imageButton = view.findViewById(R.id.favourites_Button);
        if(checkIfIsFavourite(drinkList.get(position),favsList)) {
            imageButton.setActivated(false);
            drinkDb.removeFromFavourites(drinkDb.getWritableDatabase(), removeFromFavList(position));
        }else{
            imageButton.setActivated(true);
            drinkDb.addToFavourites(drinkDb.getWritableDatabase(),new DrinkEntity(drinkList.get(position)));
            favsList.add(drinkList.get(position));
        }
        notifyDataSetChanged();
    }

    public void setDrinkList(List<Drink> drinkList) {
        this.drinkList = drinkList;
    }

    public boolean checkIfIsFavourite(Drink drinks, List<Drink> favs) {
        for (Drink d:favs) {
            if(d.getName().equals(drinks.getName())) return true;
        }
        return false;
    }

    private Long removeFromFavList(int position) {
        for (Drink d: favsList) {
            if(d.getName().equals(drinkList.get(position).getName())) {
                favsList.remove(d);
                return d.getId();
            }
        }
        return 0L;
    }


}