package com.cst2335.ticketmaster;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_cart_total,container,false);

        return rootview;
    }
}