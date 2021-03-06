package com.codemaster.fancorner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.codemaster.fancorner.SharedPreference.SharedPreference;
import com.codemaster.fancorner.model.UserDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CreateAccountScreen extends AppCompatActivity {
    private DatabaseReference mDatabase;
    List<String> teamList = new ArrayList<>();
    AutoCompleteTextView teamDropDown;
    Button createAccountBtn;
    TextInputEditText userNameText;
    String uid, userName, team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_screen);

        //initialization
        teamDropDown = findViewById(R.id.teamDropDownText);
        createAccountBtn = findViewById(R.id.createBtn);
        userNameText = findViewById(R.id.userNameText);

        setUpDropDownTeams();

        //Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        createAccountBtn.setOnClickListener(v -> {
            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentFirebaseUser != null) {
                uid = currentFirebaseUser.getUid();
            }
            userName = userNameText.getText().toString();
            team = teamDropDown.getText().toString();
            writeNewUser(uid, userName, team, currentFirebaseUser.getPhoneNumber());
        });

    }

    private void writeNewUser(String uid, String userName, String team, String phone) {
        UserDetails user = new UserDetails(uid, userName, team, phone);

        mDatabase.child("users").child(uid).setValue(user).addOnSuccessListener(aVoid -> {
            SharedPreference.setUserVerified(getApplicationContext(), true);
            Intent mainIntent = new Intent(CreateAccountScreen.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    //dropdown set up
    private void setUpDropDownTeams() {
        teamList.add("ATK Mohun Bagan");
        teamList.add("Bengaluru");
        teamList.add("Goa");
        teamList.add("Chennaiyin");
        teamList.add("Kerala Blasters");
        teamList.add("Mumbai City");
        teamList.add("Odisha");
        teamList.add("East Bengal");
        teamList.add("Hyderaba");
        teamList.add("Jamshedpur");
        teamList.add("NorthEast United");
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(CreateAccountScreen.this, R.layout.drop_down_item, teamList);
        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamDropDown.setAdapter(teamAdapter);
    }
}