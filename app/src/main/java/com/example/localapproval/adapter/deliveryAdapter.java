package com.example.localapproval.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localapproval.R;
import com.example.localapproval.detailsActivity;
import com.example.localapproval.modelclass.Delivery;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class deliveryAdapter extends RecyclerView.Adapter<deliveryAdapter.myviewholder> {

    private ArrayList<Delivery> deliveries;
    private ProgressDialog progressDialog;
    private Context context;

    public deliveryAdapter(Context context,ArrayList deliveries,ProgressDialog progressDialog){
        this.context = context;
        this.deliveries = deliveries;
        this.progressDialog = progressDialog;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myviewholder(LayoutInflater.from(context).
                inflate(R.layout.delivery_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, final int position) {
        progressDialog.dismiss();
        holder.name.setText(deliveries.get(position).getUsername());
        holder.phno.setText(deliveries.get(position).getPhno());
        holder.vnum.setText(deliveries.get(position).getVehicleNum());
        holder.vtype.setText(deliveries.get(position).getVehicleType());
        Picasso.get().load(deliveries.get(position).getSelfie()).into(holder.image);
        final DatabaseReference delivery_account = FirebaseDatabase.getInstance().getReference().child("delivery_account");
        if(deliveries.get(position).getApproval().equals("approved")){
            holder.approvedornot.setText("approved");
            holder.approvedornot.setTextColor(context.getResources().getColor(R.color.green));

        }else {
            holder.approvedornot.setText("not approved");
            holder.approvedornot.setTextColor(context.getResources().getColor(R.color.red));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, detailsActivity.class);
                intent.putExtra("deliveryId",deliveries.get(position).getUsername());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return deliveries.size();
    }

    public void updateList(ArrayList<Delivery> filteredList) {
        deliveries = new ArrayList<>();
        deliveries.addAll(filteredList);
        notifyDataSetChanged();
    }

    public class  myviewholder extends RecyclerView.ViewHolder{

        TextView name , phno , vnum , vtype , approvedornot;
        ImageView image;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            image=(ImageView)itemView.findViewById(R.id.delivery_pic);
            name=(TextView)itemView.findViewById(R.id.delivery_name);
            phno=(TextView)itemView.findViewById(R.id.delivery_phno);
            vnum=(TextView)itemView.findViewById(R.id.delivery_vnum);
            vtype=(TextView)itemView.findViewById(R.id.delivery_vtype);
            approvedornot= itemView.findViewById(R.id.approvedornot);
        }
    }
}
