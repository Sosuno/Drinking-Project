package com.example.layouts;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout list_of_all;
    private RelativeLayout recipe;
    MyListAdapter adapter;
    List<Hero> heroList;
    ListView listView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            list_of_all = (RelativeLayout) findViewById(R.id.list_of_all);
            recipe = (RelativeLayout) findViewById(R.id.recipe);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    list_of_all = (RelativeLayout) findViewById(R.id.list_of_all);
                    adapter.setHeroList(heroList);
                    listView.setAdapter(adapter);
                    System.out.println(recipe);
                  //  list_of_all.setVisibility(View.VISIBLE);
                    //recipe.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_dashboard:

                    //recipe = findViewById(R.id.recipe);
                    System.out.println(recipe);
                    adapter.setHeroList(new ArrayList<Hero>());
                    listView.setAdapter(adapter);
                    System.out.println(list_of_all);
                   // list_of_all.setVisibility(View.INVISIBLE);
                    //recipe.setVisibility(View.VISIBLE);


            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        System.out.println(navView);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        heroList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.bubu);

        heroList.add(new Hero(R.drawable.tequila, "Spiderman", "Avengers"));
        heroList.add(new Hero(R.drawable.drinks, "Joker", "Injustice Gang"));
        heroList.add(new Hero(R.drawable.hhhhh, "Iron Man", "Avengers"));
        heroList.add(new Hero(R.drawable.whiskysour, "Doctor Strange", "Avengers"));
        heroList.add(new Hero(R.drawable.whiskysour, "Doctor Strange", "Avengers"));
        heroList.add(new Hero(R.drawable.whiskysour, "Doctor Strange", "Avengers"));
        heroList.add(new Hero(R.drawable.whiskysour, "Doctor Strange", "Avengers"));
        heroList.add(new Hero(R.drawable.whiskysour, "Doctor Strange", "Avengers"));
        heroList.add(new Hero(R.drawable.whiskysour, "Doctor Strange", "Avengers"));
        heroList.add(new Hero(R.drawable.whiskysour, "Doctor Strange", "Avengers"));

        adapter = new MyListAdapter(this, R.layout.my_custom_list, heroList);

        //attaching adapter to the listview
        listView.setAdapter(adapter);

        //creating the adapter

    }

}
