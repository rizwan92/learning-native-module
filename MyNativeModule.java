package instamozomodule.com.mynativemoduleinstamozo;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by rizwan on 28/12/17.
 */

public class MyNativeModule extends ReactContextBaseJavaModule {
    public MyNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }
    @ReactMethod
    public void showToast(String name){
        Toast.makeText(getCurrentActivity(),name,Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getName() {
        return "ReactNativeInstamozo";
    }
}
