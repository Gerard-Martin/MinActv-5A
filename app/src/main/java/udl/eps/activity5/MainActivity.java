package udl.eps.activity5;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView textView1;
    private TextView textView2;

    boolean wifiConnected;
    boolean mobileConnected;

    NetworkInfo networkInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView1 = findViewById(R.id.textView1);
        textView1.setBackgroundColor(Color.YELLOW);
        textView2 = findViewById(R.id.textView2);
        textView2.setBackgroundColor(Color.GREEN);
        BroadcastReceiver br = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(br, filter);
        checkConnectivity();
    }

    private void checkConnectivity() {
        new NetworkAsyncTask().execute();

    }

    private class NetworkAsyncTask extends AsyncTask<String, Void, String> {
        String result = "";

        @Override
        protected String doInBackground(String... strings) {
            ConnectivityManager connMgr =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                wifiConnected = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
                mobileConnected = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            } else {
                wifiConnected = false;
                mobileConnected = false;
            }
            String base = getString(R.string.base);
            if(networkInfo != null) {
                result = String.format(base, networkInfo.getType(), networkInfo.isAvailable(), networkInfo.isConnected(), networkInfo.isRoaming());
            }else{
                result = getString(R.string.no);
                result += String.format(base, -1, "-", "-", "-");
            }
            if (wifiConnected) {
                return getString(R.string.wifi);
            } else if (mobileConnected) {
                return getString(R.string.mobile);
            } else {
                return getString(R.string.noNetwork);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView1.setText(result);
            textView2.setText(s);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkConnectivity();
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            checkConnectivity();
        }
    }
}