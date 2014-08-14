package com.elpmaxe.petou.producthunt;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by PetoU on 05/08/14.
 */
public class ViewFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String TAG = "ViewFactory";

    private JSONArray mArray;
    private Context mContext;

    public ViewFactory(String result, Context context){
        mContext = context;

        try {
            JSONObject data = new JSONObject(result);
            mArray = data.getJSONArray(Consts.HUNTS);

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mArray != null) {
            return mArray.length();
        }
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        JSONObject cellData = null;
        try {
            cellData = mArray.getJSONObject(position);
            String url = cellData.getString(Consts.HUNTS_URL);
            String name = cellData.getString(Consts.HUNTS_TITLE);
            String description = cellData.getString(Consts.HUNTS_TAGLINE);
            String votesCount = cellData.getString(Consts.HUNTS_VOTES);

            RemoteViews cell = new RemoteViews(mContext.getPackageName(), R.layout.listview_cell);

            cell.setTextViewText(R.id.name, name);
            cell.setTextViewText(R.id.description, description);
            cell.setTextViewText(R.id.votes, votesCount);

            // set onClick listener

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            cell.setOnClickFillInIntent(R.id.layout, browserIntent);
            return cell;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
