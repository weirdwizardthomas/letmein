package com.via_android.letmein.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.via_android.letmein.R;

import java.util.ArrayList;
import java.util.List;

public class AddMember extends AppCompatActivity {
    private Spinner roleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        roleSpinner = findViewById(R.id.addMember_selectRoleSpinner);

        ArrayAdapter<String> rolesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mockupData());
        roleSpinner.setAdapter(rolesAdapter);
        rolesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private List<String> mockupData() {
        final List<String> roles = new ArrayList<>();
        roles.add("Member");
        roles.add("Owner");
        roles.add("Postman");
        roles.add("Cleaning lady");
        return roles;
    }


    public void onAddPictureButtonClick(View view) {
        //TODO send a request
        //TODO display a notification
    }

    public void onAddFingeprintButtonClick(View view) {
        //TODO send a request
        //TODO display a notification
    }

    public void onSaveButtonClick(View view) {
        //TODO send a request
        //TODO display a notification
        //TODO return from the activity
          /*
            accessing spinner's value
            String dummy = roleSpinner.getSelectedItem().toString();
            Toast.makeText(this, dummy, Toast.LENGTH_SHORT).show();
            */
    }
}
