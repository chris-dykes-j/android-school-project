package com.cst2335.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class WishList extends AppCompatActivity {
    ArrayList<Ticket> tickets = new ArrayList<>();
    WishListAdapter wishListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        String resultText;
        ListView listView = findViewById(R.id.myWishList);
        listView.setAdapter(wishListAdapter = new WishListAdapter());

        try {
            resultText = new Task().execute().get();
            Log.i("test", "GOOOD!!");
            listjsonParser(resultText, tickets);
            wishListAdapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public ArrayList listjsonParser(String jsonString, ArrayList<Ticket> tickets) {

        String name = null;
        String type = null;
        String id = null;
        String url = null;
        String imgUrl = null;
        JSONArray jsonArr = null;
        String startDate = null;
        String status = null;

        try {
            JSONObject jobject = new JSONObject(jsonString);
            JSONArray jsonArray = jobject.optJSONObject("_embedded").optJSONArray("events");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = jsonArray.optJSONObject(i);

                name = jObject.optString("name");
                type = jObject.optString("type");
                id = jObject.optString("id");
                url = jObject.optString("url");
                imgUrl = jObject.optJSONArray("images").optJSONObject(0).optString("url");
                Log.i("dates : ", jObject.optJSONObject("dates").optJSONObject("start").optString("localDate") + "");

                startDate = jObject.optJSONObject("dates").optJSONObject("start").optString("localDate");
                status= jObject.optJSONObject("dates").optJSONObject("status").optString("code");
                tickets.add(new Ticket(name, type, id, url, imgUrl, startDate, status));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    class WishListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return tickets.size();
        }

        @Override
        public Object getItem(int position) {
            return tickets.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Long.parseLong(tickets.get(position).getId());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.my_wish_list, parent, false);
            TextView textView = newView.findViewById(R.id.myWishList_name);
            Ticket ticket = (Ticket) getItem(position);
            new DownloadImageTask((ImageView) newView.findViewById(R.id.myWishList_image))
                    .execute(ticket.getImgUrl());
            textView.setText(ticket.getName() + "/" + ticket.getStatus() + "/" + ticket.getStartDate());
            return newView;
        }
    }


}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

class Ticket {
    private String name;
    private String type;
    private String id;
    private String url;
    private String imgUrl;
    private String startDate;
    private String status;

    public Ticket(String name, String type, String id, String url, String imgUrl, String startDate, String status) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.url = url;
        this.imgUrl = imgUrl;
        this.startDate = startDate;
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }


    public String getId() {
        return this.id;
    }


    public String getUrl() {
        return this.url;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getStatus() {
        return this.status;
    }

}

// get attraction
class Task extends AsyncTask<String, Void, String> {
    private String str, receiveMsg;

    @Override
    protected String doInBackground(String... strings) {

        String strUrl = "https://app.ticketmaster.com/discovery/v2/events.json?sort=date,asc&dmaId=527&size=5&countryCode=CA&apikey=LJclKZ6rnChg9m4ZwZ3BfUlfOHD69Ekb";
        URL url = null;
        try {
            url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("receiveMsg : ", receiveMsg);

                reader.close();
                Log.i("HTTP IS ", "OKAY");
            } else {
                Log.i("HTTP IS ", conn.getResponseCode() + "NO");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}



