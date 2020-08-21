package pmru.covid19.qrCodes.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class App_SharedPreferences {

    public Context appContext;
    public static final String MyPREFERENCES = "pmruQrCode" +
            "ShareReferences";
    public static SharedPreferences.Editor editor;
    private SharedPreferences sharedpreferences;

    public App_SharedPreferences(Context appContext) {
        this.appContext = appContext;
        sharedpreferences=appContext.getSharedPreferences(MyPREFERENCES,appContext.MODE_MULTI_PROCESS);
    }

    public App_SharedPreferences() {
    }

    public boolean isOpenFirstTime(Context appContext){
        boolean checkOpenFirstTime=sharedpreferences.getBoolean("isOpenFirstTime",true);
        return checkOpenFirstTime;
    }
    public void saveFirstTimeOpenStatus(Context appContext,boolean status){

        editor=sharedpreferences.edit();
        editor.putBoolean("isOpenFirstTime",status);
        editor.commit();

    }
}
