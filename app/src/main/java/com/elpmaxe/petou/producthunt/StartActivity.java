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


public class StartActivity extends Activity {

    public static final String TAG = "StartActivity";

    private MyReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Log.d(TAG, "onCreate");
        ListView listView = (ListView) findViewById(R.id.main_listview);

        mReceiver = new MyReceiver(listView, this);

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PROVIDER_CHANGED);
        registerReceiver(mReceiver, intentFilter);

        // start fetchService
        Intent fetchIntent = new Intent(this, FetchService.class);
        fetchIntent.setAction(FetchService.ACTIVITY_CALLBACK);
        startService(fetchIntent);

    }

    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }

    //receiver
    private static class MyReceiver extends BroadcastReceiver
    {
        private Context mContext;
        private ListView mListView;

        public MyReceiver(ListView listView, Context context) {
            mContext = context;
            mListView = listView;
        }

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_PROVIDER_CHANGED)){
                String result = intent.getStringExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS);
                final JsonAdapter adapter = new JsonAdapter(result, mContext);
                mListView.setAdapter(adapter);
            }
        }
    }

}

