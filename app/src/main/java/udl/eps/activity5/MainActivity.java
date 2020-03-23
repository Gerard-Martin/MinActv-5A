package udl.eps.activity5;

import android.annotation.SuppressLint;
import android.content.Context;
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

    NetworkInfo networkInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView1 = findViewById(R.id.textView1);
        textView1.setBackgroundColor(Color.YELLOW);
        textView2 = findViewById(R.id.textView2);
        textView2.setBackgroundColor(Color.GREEN);

        checkConnectivity();
    }

    private void checkConnectivity() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        String base = " Information:\n \tType: %s \n \tAvailable: %s\n \tConnected: %s\n \tRoaming: %s\n";
        String result = "";
        if(networkInfo != null) {
            result = String.format(base, networkInfo.getType(), networkInfo.isAvailable(), networkInfo.isConnected(), networkInfo.isRoaming());
        }else{
            result = "There is no network connected!\n";
            result += String.format(base, "-", "-", "-", "-");
        }
        textView1.setText(result);
        new NetworkAsyncTask().execute();
    }

    private class NetworkAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            boolean wifiConnected;
            boolean mobileConnected;
            if (networkInfo != null && networkInfo.isConnected()) {
                wifiConnected = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
                mobileConnected = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            } else {
                wifiConnected = false;
                mobileConnected = false;
            }
            if (wifiConnected) {
                return getString(R.string.wifi);
            } else if(mobileConnected) {
                return getString(R.string.mobile);
            } else {
                return getString(R.string.noNetwork);
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView2.setText(s);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkConnectivity();
    }
}