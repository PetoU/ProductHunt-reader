package com.elpmaxe.petou.producthunt;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Created by PetoU on 04/08/14.
 */
public class AppWidget extends AppWidgetProvider {

    private static final String TAG = "AppWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // start data download service
        Intent fetchIntent = new Intent(context, FetchService.class);
        fetchIntent.setAction(FetchService.APPWIDGET_CALLBACK);
        fetchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.startService(fetchIntent);
        Log.d(TAG, "FetchService called");

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        String action = intent.getAction();
        String result = intent.getStringExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS);

        if (action.equals(FetchService.APPWIDGET_CALLBACK) && result != null) {
            int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            if (appWidgetIds != null && appWidgetIds.length > 0) {
                for (int appWidgetId : appWidgetIds) {
                    updateWidget(context, appWidgetManager, appWidgetId, result);
                }
            }
        }
        super.onReceive(context, intent);
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String result) {
        Log.d(TAG, "update appWidgetId: " + appWidgetId);

        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, result);

        hackListview(serviceIntent);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);

        // pending intent template to fill in listview cells
        Intent browserIntent = new Intent();
        PendingIntent pi = PendingIntent.getActivity(context, 0, browserIntent, 0);

        remoteViews.setPendingIntentTemplate(R.id.listview, pi);
        remoteViews.setRemoteAdapter(appWidgetId, R.id.listview, serviceIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    /* set RandomNumber to intent, to hack non-refreshing listview due to caching old data */
    private void hackListview(Intent intent) {
        String randomNumber = String.valueOf(new Random().nextInt());
        intent.setData(Uri.fromParts("content", randomNumber, null));
    }

}
