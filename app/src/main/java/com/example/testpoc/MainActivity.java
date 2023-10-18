package com.example.testpoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testpoc.views.LoginActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button testButton;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testButton = findViewById(R.id.testBtn);
        listView = findViewById(R.id.sampleList);

        ArrayList<String> userList = new ArrayList<String>();
        userList.add("User 1");
        userList.add("User 2");
        userList.add("User 3");
        userList.add("User 4");
        userList.add("User 5");
        userList.add("User 5");
        userList.add("User 5");
        userList.add("User 5");
        userList.add("User 5");
        userList.add("User 5");
        userList.add("User 5");
        userList.add("User 5");
        userList.add("User 5");
        userList.add("User 5");
        userList.add("User 5");
        userList.add("User 5");
        userList.add("User 5");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userList);
        listView.setAdapter(arrayAdapter);
//        listView.notify();

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"How do u do amigo",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}