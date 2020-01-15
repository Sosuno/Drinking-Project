package com.example.layouts;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private ConstraintLayout home_layout;
    List<Hero> heroList;
    ListView listView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    //home_layout = findViewById(R.id.home_layout);
                    home_layout.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        //navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        heroList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);

        heroList.add(new Hero(R.drawable.tequila, "Spiderman", "Avengers"));
        heroList.add(new Hero(R.drawable.drinks, "Joker", "Injustice Gang"));
        heroList.add(new Hero(R.drawable.hhhhh, "Iron Man", "Avengers"));
        heroList.add(new Hero(R.drawable.whiskysour, "Doctor Strange", "Avengers"));


        //creating the adapter
        MyListAdapter adapter = new MyListAdapter(this, R.layout.my_custom_list, heroList);

        //attaching adapter to the listview
        listView.setAdapter(adapter);
    }

}
