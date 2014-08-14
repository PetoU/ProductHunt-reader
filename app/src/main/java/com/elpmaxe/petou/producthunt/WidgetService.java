package com.elpmaxe.petou.producthunt;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

/**
 * Created by PetoU on 04/08/14.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        String result = intent.getStringExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS);
        return new ViewFactory(result, getApplicationContext());
    }
}
