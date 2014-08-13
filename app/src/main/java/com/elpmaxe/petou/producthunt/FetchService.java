package com.elpmaxe.petou.producthunt;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by PetoU on 05/08/14.
 */
public class FetchService extends IntentService {

    private static final String TAG = "FetchService";
    public static final String CALLBACK = "callback";

    private int[] mAppWidgetIds;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FetchService(String name) {
        super(name);
    }

    public FetchService() {
        super("fetchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        mAppWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        fetchData();
    }

    private void fetchData() {

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(Consts.API);
        HttpResponse response;
        StringBuffer result = null;

        try {

            BufferedReader rd = null;
            response = client.execute(request);
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            result = new StringBuffer();
            String line = "";

            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            callback(result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void callback(String result) {

        if (result != null) {
            Intent intent = new Intent(getApplicationContext(), AppWidget.class);
            intent.setAction(FetchService.CALLBACK);
            intent.putExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, result);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, mAppWidgetIds);

            sendBroadcast(intent);
        }
    }


//    private void fetchData() {
//
//        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
//        Document doc = null;
//
//        try {
//            doc = Jsoup.connect(Consts.MAINPAGE).get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        if (doc != null) {
//            Elements post = doc.getElementsByClass("post");
//
//            // temporarily hardcoded only 15 items, although data could be fetched more precisely
//            for (int i = 0; i < 15; i++) {
//                Element e = post.get(i);
//
//                Elements href = e.getElementsByAttribute("class");
//                String url = href.attr("href");
//                String name = href.text();
//
//                Elements b = e.getElementsByClass("post-tagline");
//                String description = b.text();
//
//                Elements votes = e.getElementsByClass("vote-count");
//                String voteCount = votes.text();
//
//                ArrayList<String> temp = new ArrayList<String>();
//                temp.add(url);
//                temp.add(name);
//                temp.add(description);
//                temp.add(voteCount);
//
//                data.add(temp);
//            }
//        }
//
//        callback(data);
//    }

}
