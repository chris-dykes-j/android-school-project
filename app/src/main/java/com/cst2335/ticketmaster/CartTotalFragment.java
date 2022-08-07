package com.cst2335.ticketmaster;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CartTotalFragment extends Fragment {

    CartActivity cartActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cartActivity = (CartActivity) getActivity();


    }

    @Override
    public void onDetach() {
        super.onDetach();
        cartActivity = null;


    }

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_cart_total,container,false);


        //payment
        Button payBtn = (Button) rootview.findViewById(R.id.payBtn);
        payBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Alert");
                builder.setMessage("Do you want to pay?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(getActivity(), PaymentEmptyActivity.class);
                        startActivity(intent);

                    }

                });

                    AlertDialog ad = builder.create();
                    ad.show();

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });

        return rootview;


    }
}