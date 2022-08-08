package com.cst2335.ticketmaster;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class WishFragment extends Fragment {
    ArrayList<Events> events = new ArrayList<>();
    WishSubListAdapter wishSubrAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MYSUBCITY = "mySubCity";
    private static final String MYSUBTYPE = "mySubType";
    // TODO: Rename and change types of parameters
    private String mySubCity;
    private String mySubType;
    private View newView;
    RecyclerView rView;
    SharedPreferences sp;

    public WishFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mySubCity = getArguments().getString(MYSUBCITY);
            mySubType = getArguments().getString(MYSUBTYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newView = inflater.inflate(R.layout.fragment_my_wish_fragment, container, false);
        rView = (RecyclerView) newView.findViewById(R.id.mySubLRecycler);
        rView.setAdapter(wishSubrAdapter = new WishSubListAdapter());
        rView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        return newView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


//        Log.i("mySubCity : ", mySubCity);
//        Log.i("mySubType : ", mySubType);

        try {

            String resultText = new WishTask().execute("keyword=" + mySubType).get();
//            Log.i("test", "GOOOD!!");

            String name = null;
            String type = null;
            String id = null;
            String url = null;
            String imgUrl = null;
            JSONArray jsonArr = null;
            String startDate = null;
            String status = null;
            String city = null;
            try {
                JSONObject jobject = new JSONObject(resultText);
                JSONArray jsonArray = jobject.optJSONObject("_embedded").optJSONArray("attractions");
                Log.i("size ", jsonArray.length() + "");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.optJSONObject(i);

                    name = jObject.optString("name");
                    imgUrl = jObject.optJSONArray("images").optJSONObject(0).optString("url");
                    events.add(new Events(name, type, id, url, imgUrl, startDate, status, city));
                    Log.i("LOOP", name + "/" + type + "/" + id + "/" + url + "/" + imgUrl + "/" + startDate + "/" + status + "/" + city);
                }
                wishSubrAdapter.notifyItemInserted(events.size() - 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        super.onViewCreated(view, savedInstanceState);
    }

    private class WishSubListAdapter extends RecyclerView.Adapter<SubViewHolder> {

        @NonNull
        @Override
        public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater linear = getLayoutInflater();
            View view = linear.inflate(R.layout.my_sub_list, parent, false);

            return new SubViewHolder(view);
        }


        @Override
        public int getItemCount() {
            return events.size();
        }

        @Override
        public void onBindViewHolder(@NonNull SubViewHolder holder, int position) {

            Events thisEvent = events.get(position);
            holder.mySubName.setText(thisEvent.getName());
            new DownloadImageTask((ImageView) holder.mySubImg)
                    .execute(thisEvent.getImgUrl());
            Log.i("testtesting", thisEvent.getName() + "/" + thisEvent.getCity() + "/" + thisEvent.getStartDate());
        }


    }

    public class SubViewHolder extends RecyclerView.ViewHolder {
        TextView mySubName;
        ImageView mySubImg;

        public SubViewHolder(@NonNull View itemView) {
            super(itemView);
            mySubName = itemView.findViewById(R.id.mySubList_name);
            mySubImg = itemView.findViewById(R.id.mySubList_image);

        }
    }
}