package com.elpmaxe.petou.producthunt;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.support.v4.widget.SwipeRefreshLayout;


public class StartActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "StartActivity";

    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mListView = (ListView) findViewById(R.id.main_listview);

        // set swipe layout
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.darker_gray, android.R.color.white, android.R.color.darker_gray, android.R.color.white);


        MyReceiver mReceiver = new MyReceiver();

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PROVIDER_CHANGED);
        registerReceiver(mReceiver, intentFilter);

        fetchData();

    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    /* start fetching data service*/
    private void fetchData(){
        Intent fetchIntent = new Intent(this, FetchService.class);
        fetchIntent.setAction(FetchService.ACTIVITY_CALLBACK);
        startService(fetchIntent);
    }

    //receiver
    private class MyReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            mSwipeLayout.setRefreshing(false);

            String action = intent.getAction();
            if(action.equals(Intent.ACTION_PROVIDER_CHANGED)){
                String result = intent.getStringExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS);
                final JsonAdapter adapter = new JsonAdapter(result, StartActivity.this);
                mListView.setAdapter(adapter);
            }
        }
    }

}

