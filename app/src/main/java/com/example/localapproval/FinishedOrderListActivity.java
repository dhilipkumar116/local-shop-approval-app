package com.example.localapproval;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.localapproval.modelclass.Orders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FinishedOrderListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_order_list);

        recyclerView = findViewById(R.id.final_orderListR1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }


    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference finalOrderRef = FirebaseDatabase.getInstance().getReference();

        FirebaseRecyclerOptions<Orders> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(finalOrderRef.child("Final_OrderList"),Orders.class).build();

        FirebaseRecyclerAdapter<Orders,myViewHolder> Adapter =
                new FirebaseRecyclerAdapter<Orders, myViewHolder>(firebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull final myViewHolder holder, int i, @NonNull final Orders orders) {
                        holder.shopName.setText("shop : "+orders.getShopname());
                        holder.shopPh.setText("phno : "+orders.getShop_ph());
                        holder.orderId.setText("orderId : "+orders.getID());
                        holder.deliveryAdd.setText("delivery : "+orders.getAddress());
                        holder.orderStatus.setText(orders.getStatus());
                        holder.price.setText("price : "+orders.getTotprice());
                        holder.bankNum.setText("bank no : "+orders.getBankNum());
                        holder.bankName.setText("bank name : "+orders.getBankName());
                        holder.riderName.setText("rider : "+orders.getRiderName());
                        holder.riderPh.setText("phno : "+orders.getRiderPhno());
                        holder.riderVnum.setText("vehicle : "+orders.getRiderVnum());
                        holder.riderTime.setText("picked up: "+orders.getPickUpTime());
                        if(orders.getStatus().equals("delivered")){
                            holder.deleteTxt.setVisibility(View.VISIBLE);
                        }
                        if(orders.getType().equals("bulk")){
                            holder.bulkTxt.setVisibility(View.VISIBLE);
                        }
                        holder.deleteTxt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalOrderRef.child("Final_OrderList").child(orders.getID()).removeValue();
                            }
                        });
                        if(orders.getPaid().equals("paid")){
                            holder.checkBox.setChecked(true);
                        }

                        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                if(holder.checkBox.isChecked()){
                                    finalOrderRef.child("Final_OrderList").child(orders.getID()).child("paid").setValue("paid");
                                }else {
                                    finalOrderRef.child("Final_OrderList").child(orders.getID()).child("paid").setValue("not");
                                }
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.finished_order_layout, parent, false);
                        return new myViewHolder(view);
                    }
                };
        recyclerView.setAdapter(Adapter);
        Adapter.startListening();

    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        private TextView shopName,shopPh,orderId,deliveryAdd,orderStatus,price,bankName,bankNum,
                        riderName,riderPh,riderVnum,riderTime,deleteTxt,bulkTxt;
        private CheckBox checkBox;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.O_shopname);
            shopPh = itemView.findViewById(R.id.O_phonenum);
            orderId = itemView.findViewById(R.id.O_id);
            deliveryAdd = itemView.findViewById(R.id.O_address);
            orderStatus = itemView.findViewById(R.id.O_oderstatus);
            price = itemView.findViewById(R.id.O_price);
            bankName = itemView.findViewById(R.id.O_bankname);
            bankNum = itemView.findViewById(R.id.O_banknum);
            riderName = itemView.findViewById(R.id.O_ridername);
            riderPh = itemView.findViewById(R.id.O_riderphonenum);
            riderVnum = itemView.findViewById(R.id.O_riderVnum);
            riderTime = itemView.findViewById(R.id.O_riderTime);
            deleteTxt = itemView.findViewById(R.id.O_delete_ordertxt);
            bulkTxt = itemView.findViewById(R.id.O_bulk_ordertxt);
            checkBox = itemView.findViewById(R.id.paid_checkbox);
        }
    }
}
