package com.example.localapproval;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.localapproval.modelclass.Delivery;
import com.example.localapproval.adapter.deliveryAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class deliveryActivity extends AppCompatActivity {

    private Button shopbtn;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ArrayList<Delivery> deliveries;
    private ProgressDialog progressDialog;
    private deliveryAdapter deliveryAdapter;
    private FloatingActionButton fabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        recyclerView = findViewById(R.id.deliveryRecyclerView);
        recyclerView.setHasFixedSize(true);
        fabBtn = findViewById(R.id.fab_new_order);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        shopbtn = findViewById(R.id.shops);

        shopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(deliveryActivity.this,MainActivity.class));
                finish();
            }
        });


        DatabaseReference deliRef = FirebaseDatabase.getInstance().getReference()
                .child("delivery_account");
        deliRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliveries = new ArrayList<Delivery>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    deliveries.add(dataSnapshot1.getValue(Delivery.class));
                }

                deliveryAdapter = new deliveryAdapter(deliveryActivity.this
                ,deliveries,progressDialog);
                recyclerView.setAdapter(deliveryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userinput = newText.toLowerCase();
                ArrayList<Delivery> filteredList = new ArrayList<>();
                for(Delivery list :deliveries){
                    if(list.getUsername().toLowerCase().contains(userinput)){
                        filteredList.add(list) ;
                    }
                }

                deliveryAdapter.updateList(filteredList);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.orders){
            Intent intent = new Intent(deliveryActivity.this , tab1Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
