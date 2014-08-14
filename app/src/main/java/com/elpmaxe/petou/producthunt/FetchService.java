package com.elpmaxe.petou.producthunt;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by PetoU on 05/08/14.
 */
public class FetchService extends IntentService {

    private static final String TAG = "FetchService";
    public static final String APPWIDGET_CALLBACK = "appwidget_callback";
    public static final String ACTIVITY_CALLBACK = "activity_callback";

    private int[] mAppWidgetIds;
    private String mCallback;

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
        mAppWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        mCallback = intent.getAction();
        fetchData();
    }

    private void fetchData() {

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(Consts.API);
        HttpResponse response;
        StringBuffer result = null;

        try {

            response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            result = new StringBuffer();
            String line = "";

            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            sendData(result.toString(), mCallback);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendData(String result, String callback) {

        if (result != null && callback.equals(FetchService.APPWIDGET_CALLBACK)) {
            Intent intent = new Intent(getApplicationContext(), AppWidget.class);
            intent.setAction(FetchService.APPWIDGET_CALLBACK);
            intent.putExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, result);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, mAppWidgetIds);

            sendBroadcast(intent);
        }

        if (result != null && callback.equals(FetchService.ACTIVITY_CALLBACK)) {
            Log.d(TAG, "activity callback" );
            Intent intent = new Intent(Intent.ACTION_PROVIDER_CHANGED);
            intent.putExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, result);

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
