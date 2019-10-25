package com.via_android.letmein.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.via_android.letmein.adapters.DayEntryAdapter;
import com.via_android.letmein.entities.DayEntry;
import com.via_android.letmein.entities.Member;
import com.via_android.letmein.entities.Visit;
import com.via_android.letmein.R;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
