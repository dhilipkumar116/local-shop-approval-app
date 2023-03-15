package com.example.localapproval;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localapproval.modelclass.Shops;
import com.example.localapproval.adapter.shopAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button deliverybtn;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ProgressDialog progressDialog;
    private ArrayList<Shops> shopList;
    private shopAdapter shopAdapter;
    private FloatingActionButton fabBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.shopRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        deliverybtn = findViewById(R.id.delivery);
        fabBtn = findViewById(R.id.fab_new_order);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        deliverybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, deliveryActivity.class));
                finish();
            }
        });


        final DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference()
                .child("Admins");
        shopList = new ArrayList<>();
        shopRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot shop : dataSnapshot.getChildren()) {
                    Shops shops = shop.getValue(Shops.class);
                    shopList.add(shops);
                }

                shopAdapter = new shopAdapter(MainActivity.this,shopList,progressDialog);
                recyclerView.setAdapter(shopAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FinishedOrderListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userinput = newText.toLowerCase();
                ArrayList<Shops> shopFiltered = new ArrayList<>();
                for (Shops filter : shopList) {
                    if (filter.getShop_name().toLowerCase().contains(userinput)) {
                        shopFiltered.add(filter);
                    }
                }
                shopAdapter.updateNewList(shopFiltered);
                return true;
            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.orders){
            Intent intent = new Intent(MainActivity.this , tab1Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
