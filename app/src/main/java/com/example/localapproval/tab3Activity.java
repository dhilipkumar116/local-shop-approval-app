package com.example.localapproval;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localapproval.prevalent.constants;
import com.example.localapproval.prevalent.userPrevalent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class tab3Activity extends AppCompatActivity {

    private TextView username,vehiclenum,vehicletype,password,totolearnings,wallet;
    private EditText phonenumber;
    private ImageView imageview,hidepassword;
    private Button save , savepass;
    DatabaseReference userRef;
    private Boolean hide = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab3);

        username = findViewById(R.id.username);
        vehiclenum = findViewById(R.id.vehiclenumber);
        vehicletype = findViewById(R.id.vehicletype);
        password = findViewById(R.id.password);
        totolearnings = findViewById(R.id.earnings);
        phonenumber = findViewById(R.id.phonenumber);
        imageview = findViewById(R.id.image);
        save  = findViewById(R.id.savebtn);
        hidepassword = findViewById(R.id.hidePassword);
        savepass = findViewById(R.id.savepassbtn);
        wallet = findViewById(R.id.wallet);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.account);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), tab1Activity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return  true;
                    case R.id.waiting:
                        startActivity(new Intent(getApplicationContext(), tab2Activity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.account:
                        return true;
                }
                return false;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phonenumber.getText().toString().trim().isEmpty()){
                    Toast.makeText(tab3Activity.this,"enter phone number",Toast.LENGTH_SHORT).show();
                }else {
                    userRef.child("phno").setValue(phonenumber.getText().toString().trim());
                    Toast.makeText(tab3Activity.this,"saved",Toast.LENGTH_SHORT).show();
                }

            }
        });

        hidepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hide==true){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hide=false;
                }else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hide=true;
                }
            }
        });

        savepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().trim().isEmpty()){
                    Toast.makeText(tab3Activity.this,"enter password",Toast.LENGTH_SHORT).show();
                }else {
                    userRef.child("password").setValue(password.getText().toString().trim());
                    userPrevalent.Userpasskey = password.getText().toString().trim();
                    Toast.makeText(tab3Activity.this,"saved",Toast.LENGTH_SHORT).show();
                }
            }
        });

        userRef = FirebaseDatabase.getInstance().getReference()
                .child("delivery_account").child(userPrevalent.Usernamekey);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username.setText(dataSnapshot.child("username").getValue().toString());
                phonenumber.setText(dataSnapshot.child("phno").getValue().toString());
                vehiclenum.setText(dataSnapshot.child("vehicleNum").getValue().toString());
                vehicletype.setText(dataSnapshot.child("vehicleType").getValue().toString());
                password.setText(dataSnapshot.child("password").getValue().toString());
                wallet.setText("wallet balance : "+ constants.notation+dataSnapshot.child("wallet").getValue().toString());
                totolearnings.setText("total earnings : "+constants.notation+dataSnapshot.child("earnings").getValue().toString());
                String image = dataSnapshot.child("selfie").getValue().toString();
                Picasso.get().load(image).into(imageview);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
