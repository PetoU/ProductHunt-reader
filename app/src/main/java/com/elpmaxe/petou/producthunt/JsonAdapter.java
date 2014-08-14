package com.elpmaxe.petou.producthunt;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PetoU on 13/08/14.
 */
public class JsonAdapter extends BaseAdapter {

    public static final String TAG = "JsonAdapter";

    private JSONArray mArray;
    private Context mContext;

    public JsonAdapter(String jsonData, Context context){

        mContext = context;

        try {
            JSONObject data = new JSONObject(jsonData);
            mArray = data.getJSONArray(Consts.HUNTS);

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public int getCount() {
        if (mArray != null) {
            return mArray.length();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        try {
            return mArray.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_cell, null);
        }

        JSONObject cellData = null;
        try {

            cellData = mArray.getJSONObject(position);
            final String url = cellData.getString(Consts.HUNTS_URL);
            String name = cellData.getString(Consts.HUNTS_TITLE);
            String description = cellData.getString(Consts.HUNTS_TAGLINE);
            String votesCount = cellData.getString(Consts.HUNTS_VOTES);

            TextView votes = (TextView) view.findViewById(R.id.votes);
            TextView names = (TextView) view.findViewById(R.id.name);
            TextView descriptions = (TextView) view.findViewById(R.id.description);

            votes.setText(votesCount);
            names.setText(name);
            descriptions.setText(description);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mContext.startActivity(intent);
                }
            });


            return view;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
