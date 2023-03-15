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
import com.example.localapproval.ShopDetailsActivity;
import com.example.localapproval.modelclass.Shops;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class shopAdapter extends RecyclerView.Adapter<shopAdapter.myviewholder> {

    Context context;
    ArrayList<Shops> Admins;
    ProgressDialog progressDialog;

    public shopAdapter(Context c , ArrayList<Shops> a,ProgressDialog progressDialog)
    {
        context = c;
        Admins = a;
        this.progressDialog = progressDialog;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myviewholder(LayoutInflater.from(context).
                inflate(R.layout.shoplayout,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, final int position) {
        progressDialog.dismiss();
        holder.txtshopname.setText(Admins.get(position).getShop_name());
        holder.txtstreet.setText(Admins.get(position).getStreet());
        holder.txtdistrict.setText(Admins.get(position).getDistrict());
        holder.txtpincode.setText(Admins.get(position).getPostcode());
        holder.txttype.setText(Admins.get(position).getType());
        Picasso.get().load(Admins.get(position).getImage()).into(holder.shopimage);
        final DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference().child("Admins");
        if(Admins.get(position).getApproval().equals("approved")){
            holder.txtapproved.setTextColor(context.getResources().getColor(R.color.green));
            holder.txtapproved.setText("approved");
        }else {
            holder.txtapproved.setText("not approved");
            holder.txtapproved.setTextColor(context.getResources().getColor(R.color.red));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ShopDetailsActivity.class);
                intent.putExtra("shopname",Admins.get(position).getShop_name());
                intent.putExtra("shopphone",Admins.get(position).getShop_phone());
                intent.putExtra("shopaddress",Admins.get(position).getStreet()+" "+
                                                        Admins.get(position).getDistrict()+" "+
                                                        Admins.get(position).getPostcode());
                intent.putExtra("shopapproved",Admins.get(position).getApproval());
                intent.putExtra("shoptype",Admins.get(position).getType());
                if(Admins.get(position).getType().equals("distributed shop")){
                    intent.putExtra("code",Admins.get(position).getDistributed_code());
                }else {
                    intent.putExtra("code","----");
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Admins.size();
    }

    public void updateNewList(ArrayList<Shops> shopFiltered) {
        Admins = new ArrayList<>();
       Admins.addAll(shopFiltered);
       notifyDataSetChanged();
    }

    public class  myviewholder extends RecyclerView.ViewHolder{
        TextView txtshopname,txtstreet,txtdistrict,txtpincode,txtapproved,txttype;
        ImageView shopimage;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            shopimage=(ImageView)itemView.findViewById(R.id.shopsearch_pic);
            txtshopname=(TextView)itemView.findViewById(R.id.shopsearch_name);
            txtstreet=(TextView)itemView.findViewById(R.id.search_shop_street);
            txtdistrict=(TextView)itemView.findViewById(R.id.search_shop_district);
            txtpincode=(TextView)itemView.findViewById(R.id.search_shop_postcode);
            txtapproved = itemView.findViewById(R.id.approvedornot);
            txttype = itemView.findViewById(R.id.search_shop_type);
        }
    }
}
