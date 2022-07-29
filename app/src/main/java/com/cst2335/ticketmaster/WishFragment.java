package com.cst2335.ticketmaster;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class WishFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MYSUBNAME = "mySubName";
    private static final String MYSUBIMAGE = "mySubImage";
    private static final String MYSUBTYPE = "mySubType";

    // TODO: Rename and change types of parameters
    private String mySubName;
    private String mySubImage;
    private String mySubType;
    private ImageView imageView;
    private TextView textView;

    public WishFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mySubName = getArguments().getString(MYSUBNAME);
            mySubImage = getArguments().getString(MYSUBIMAGE);
            mySubType = getArguments().getString(MYSUBTYPE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_wish_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        textView = (TextView) view.findViewById(R.id.mySubName);
        textView.setText(mySubName);

        Log.i("mySubName : ", mySubName);
        Log.i("mySubImage : ", mySubImage);
        Log.i("mySubType : ", mySubType);


//        new DownloadImageTask((ImageView) view.findViewById(R.id.mySubImage))
//                .execute(mySubImage);
        super.onViewCreated(view, savedInstanceState);
    }
}