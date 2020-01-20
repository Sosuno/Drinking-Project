package com.drinkingTeam.drinkingProject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Created by Belal on 9/14/2017.
 */

//we need to extend the ArrayAdapter class as we are building an adapter
public class MyListAdapter extends ArrayAdapter<Drink> {

    //the list values in the List of type hero
    private List<Drink> drinkList;
    private List<Drink> favsList;

    //activity context
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
    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);


        View view = layoutInflater.inflate(resource, null, false);

        if(drinkList.size() < 1) return new View(context);



        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewTeam = view.findViewById(R.id.textViewTeam);


        Drink drink = drinkList.get(position);

        if(checkIfIsFavourite(drink,favsList)) {
            ImageButton imageButton = view.findViewById(R.id.favourites_Button);
            imageButton.setActivated(true);
        }

        byte[] base64converted = Base64.decode(drink.getImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(base64converted, 0, base64converted.length);

        imageView.setImageBitmap(bitmap);
        textViewName.setText(drink.getName());
        textViewTeam.setText(drink.getId()+"");
        return view;
    }

    //this method will remove the item from the list
    private void removeHero(final int position) {
        //Creating an alert dialog to confirm the deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete this?");

        //if the response is positive in the alert
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //removing the item
                drinkList.remove(position);

                //reloading the list
                notifyDataSetChanged();
            }
        });

        //if response is negative nothing is being done
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //creating and displaying the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

}