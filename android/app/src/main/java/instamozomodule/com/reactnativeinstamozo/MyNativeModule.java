package instamozomodule.com.reactnativeinstamozo;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.instamojo.android.Instamojo;
import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Order;
import com.instamojo.android.network.Request;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;


/**
 * Created by rizwan on 28/12/17.
 */

public class MyNativeModule extends ReactContextBaseJavaModule {

    private static final HashMap<String, String> env_options = new HashMap<>();

    static {
        env_options.put("Test", "https://test.instamojo.com/");
        env_options.put("Production", "https://api.instamojo.com/");
    }

    private String currentEnv = null;
    private String accessToken = null;

    public MyNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }
    @ReactMethod
    public void fetchTokenAndTransactionID(){
        OkHttpClient client = new OkHttpClient();

        HttpUrl url = getHttpURLBuilder()
                .addPathSegment("oauth2")
                .addPathSegment("token")
                .build();

        RequestBody body = new FormBody.Builder()
                .add("env", currentEnv.toLowerCase())
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        showToast("Failed to fetch the Order Tokens");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString;
                String errorMessage = null;
                String transactionID = null;
                responseString = response.body().string();
                response.body().close();
                try {
                    JSONObject responseObject = new JSONObject(responseString);
                    if (responseObject.has("error")) {
                        errorMessage = responseObject.getString("error");
                    } else {
                        accessToken = responseObject.getString("access_token");
                        transactionID = responseObject.getString("transaction_id");
                    }
                } catch (JSONException e) {
                    errorMessage = "Failed to fetch Order tokens";
                }

                final String finalErrorMessage = errorMessage;
                final String finalTransactionID = transactionID;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (finalErrorMessage != null) {
                            showToast(finalErrorMessage);
                            return;
                        }

                    }
                });

            }
        });

    }

    private HttpUrl.Builder getHttpURLBuilder() {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("test.instamojo.com");
    }

    public void showToast(String name){
        Toast.makeText(getCurrentActivity(),name,Toast.LENGTH_SHORT).show();
    }



    @Override
    public String getName() {
        return "ReactNativeInstamozo";
    }
}
