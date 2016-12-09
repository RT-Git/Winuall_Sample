package com.wordpress.obliviouscode.winualltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoggedIn extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        String[] question = new String[] {
                "Question1",
                "Question2",
                "Question3",
                "Question4",
                "Question5",
                "Question6"
        };
        final List<String> qlist = new ArrayList<String>(Arrays.asList(question));
        final ListView lv = (ListView) findViewById(R.id.list);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, qlist);

        lv.setAdapter(arrayAdapter);

    }
}
