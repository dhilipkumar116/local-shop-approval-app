package com.example.localapproval;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShopDetailsActivity extends AppCompatActivity {

    private TextView shopName,shopPhone,shopAddress,shopApproval,shopType,shopCode;
    private Button giveApproval,setCode,saveBank;
    private RadioGroup typeGroup;
    private EditText dis_code , bankName,bankNumber;
    private DatabaseReference shopRef;
    private String name,phone,address,approval,type,code;
    private RadioButton checkedRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        shopRef = FirebaseDatabase.getInstance().getReference().child("Admins");
        name = getIntent().getStringExtra("shopname");
        phone = getIntent().getStringExtra("shopphone");
        address = getIntent().getStringExtra("shopaddress");
        approval = getIntent().getStringExtra("shopapproved");
        type = getIntent().getStringExtra("shoptype");
        code = getIntent().getStringExtra("code");


        shopName = findViewById(R.id.shop_details_name);
        shopPhone = findViewById(R.id.shop_details_phno);
        shopAddress = findViewById(R.id.shop_details_address);
        shopApproval = findViewById(R.id.shop_details_approved);
        shopType = findViewById(R.id.shop_details_type);
        shopCode = findViewById(R.id.shop_details_distribution_code);
        giveApproval = findViewById(R.id.give_approval);
        setCode = findViewById(R.id.save_code);
        dis_code = findViewById(R.id.distributed_code);
        typeGroup = findViewById(R.id.typeGroup);
        checkedRadioButton = (RadioButton)findViewById(R.id.own);
        bankName = findViewById(R.id.bank_name);
        bankNumber = findViewById(R.id.bank_number);
        saveBank = findViewById(R.id.save_bank_detials);

        shopName.setText("name : "+name);
        shopPhone.setText("phone : "+phone);
        shopAddress.setText("address : "+address);
        shopType.setText("type : "+type);
        shopCode.setText("code : "+code);
        shopApproval.setText("status : "+approval);
        if(approval.equals("approved")){
            shopApproval.setTextColor(Color.GREEN);
        }else {
            shopApproval.setTextColor(Color.RED);
        }

        typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedRadioButton = (RadioButton) group.findViewById(checkedId);
               switch (checkedId){
                   case R.id.own:
                       dis_code.setVisibility(View.GONE);
                       break;
                   case R.id.distributed:
                       dis_code.setVisibility(View.VISIBLE);
                       break;
               }
            }
        });

        giveApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogue();
            }
        });

        setCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_code();
            }
        });
        saveBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_bank();
            }
        });

        shopRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(name).child("bank_name").exists()){
                    bankName.setText(dataSnapshot.child(name).child("bank_name").getValue().toString());
                }
                if(dataSnapshot.child(name).child("bank_number").exists()){
                    bankNumber.setText(dataSnapshot.child(name).child("bank_number").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void save_bank() {
        if(bankName.getText().toString().trim().isEmpty()){
            Toast.makeText(ShopDetailsActivity.this,"enter bank name" ,Toast.LENGTH_SHORT).show();
        }else if(bankNumber.getText().toString().trim().isEmpty()){
            Toast.makeText(ShopDetailsActivity.this,"enter bank number" ,Toast.LENGTH_SHORT).show();
        }else {
            shopRef.child(name).child("bank_name").setValue(bankName.getText().toString().trim());
            shopRef.child(name).child("bank_number").setValue(bankNumber.getText().toString().trim());
            Toast.makeText(ShopDetailsActivity.this,"saved" ,Toast.LENGTH_SHORT).show();
        }
    }

    private void save_code() {

        if(checkedRadioButton.getText().toString().equals("own shop")){
            shopRef.child(name).child("distributed_code").removeValue();
            shopRef.child(name).child("type").setValue(checkedRadioButton.getText().toString());
            Toast.makeText(ShopDetailsActivity.this,"saved" ,Toast.LENGTH_SHORT).show();
        }else {
            if(dis_code.getText().toString().trim().isEmpty()){
                Toast.makeText(ShopDetailsActivity.this,"enter code" ,Toast.LENGTH_SHORT).show();
            }else if(dis_code.length()==6){
                Toast.makeText(ShopDetailsActivity.this,"enter 6 digit number" ,Toast.LENGTH_SHORT).show();
            }else {
                shopRef.child(name).child("distributed_code").setValue(dis_code.getText().toString().trim());
                shopRef.child(name).child("type").setValue(checkedRadioButton.getText().toString());
                Toast.makeText(ShopDetailsActivity.this,"saved" ,Toast.LENGTH_SHORT).show();
                shopCode.setText("code : "+dis_code.getText().toString().trim());
            }
        }

    }

    private void openDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("give approval", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shopRef.child(name).child("approval").setValue("approved");
                Toast.makeText(ShopDetailsActivity.this,"approved" ,Toast.LENGTH_SHORT).show();
                shopApproval.setText("status : "+"approved");
                shopApproval.setTextColor(Color.GREEN);
            }
        });
        builder.setNegativeButton("cancel approval", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shopRef.child(name).child("approval").setValue("not approved");
                Toast.makeText(ShopDetailsActivity.this,"approval cancelled" ,Toast.LENGTH_SHORT).show();
                shopApproval.setText("status : "+"not approved");
                shopApproval.setTextColor(Color.RED);
            }
        });
        builder.show();
    }


}
