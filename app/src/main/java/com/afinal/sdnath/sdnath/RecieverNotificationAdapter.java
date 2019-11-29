package com.afinal.sdnath.sdnath;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecieverNotificationAdapter extends Adapter<RecieverNotificationAdapter.myViewHolder> {

    Context context;
    List<Reciever> reciever;
    String blood;
    String phone;

    String myNotificationTime;

    public RecieverNotificationAdapter(Context c, List<Reciever> r) {
        context=c;
        reciever=r;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.reciever_notification_row, viewGroup, false);
        return new myViewHolder(view);
    }

    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {
        blood=reciever.get(i).getBlood();
        phone=reciever.get(i).getPhone();

        String timeInMill=reciever.get(i).getTime();
        myNotificationTime=DateFormat.format("dd/MM/yyyy hh:mm:ss", Long.parseLong(timeInMill)).toString();


        myViewHolder.name.setText("NAME: "+ reciever.get(i).getName());
        myViewHolder.phone.setText("PHONE: "+ reciever.get(i).getPhone());
        myViewHolder.address.setText("ADDRESS: "+reciever.get(i).getAddress());
        myViewHolder.notificationTime.setText(myNotificationTime);



        if ("A+".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.ap);
        }
        if ("A-".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.an);
        }
        if ("B+".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.bp);
        }
        if ("B-".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.bn);
        }
        if ("AB+".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.abp);
        }
        if ("AB-".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.abn);
        }
        if ("O+".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.op);
        }
        if ("O-".equals(blood)) {
            myViewHolder.img.setImageResource(R.mipmap.on);
        }







        myViewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:+88"+phone;
                i.setData(Uri.parse(p));
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return reciever.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,phone,address,hospital,bag;
        Button call;
        TextView notificationTime;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.img);
            name=itemView.findViewById(R.id.name);
            phone=itemView.findViewById(R.id.phone);
            address=itemView.findViewById(R.id.address);
            call=itemView.findViewById(R.id.call);
            notificationTime=itemView.findViewById(R.id.notificationTime);
        }
    }






}
