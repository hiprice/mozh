package ir.sadeghzadeh.mozhdegani.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import ir.sadeghzadeh.mozhdegani.BuildConfig;
import ir.sadeghzadeh.mozhdegani.Const;
import ir.sadeghzadeh.mozhdegani.MainActivity;

public class Util {
    private static final Pattern DIR_SEPORATOR;
    public static Context context;
    public static MainActivity mainActivity;

    static class LoginResponseListener implements Response.Listener<String> {
        LoginResponseListener() {
        }

        public void onResponse(String response) {
        }
    }

    static class LoginErrorResponseListener implements Response.ErrorListener {
        LoginErrorResponseListener() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.d("Login", error.toString());
        }
    }

    static class LoginRequest extends StringRequest {
        LoginRequest(int method, String url, Response.Listener listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap();
/*
            params.put(Const.USERNAME, Util.fetchFromPreferences(Const.USERNAME));
            params.put(Const.PASSWORD, Util.fetchFromPreferences(Const.PASSWORD));
*/
            return params;
        }
    }

    public static void writeToLogFile(String inputText, Object... objects) {

        String log = new Date().toString() + " : " + String.format(inputText, objects);
        File logFile = new File(getDownloadDirectoryPath() + "/mozhdegani.log");
        if (!logFile.exists()) {
            logFile.getParentFile().mkdirs();
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (logFile.length() > 500000) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(logFile);
                fileOutputStream.write(BuildConfig.FLAVOR.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(logFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(log);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        } catch (FileNotFoundException e22) {
            e22.printStackTrace();
        } catch (IOException e32) {
            e32.printStackTrace();
        }
    }

    public static String getDownloadDirectoryPath() {
        String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File dir = new File(downloadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return downloadPath;
    }



    static {
        DIR_SEPORATOR = Pattern.compile("/");
    }

    public static String[] getStorageDirectories() {
        Set<String> rv = new HashSet();
        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
        String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
        if (!TextUtils.isEmpty(rawEmulatedStorageTarget)) {
            String rawUserId;
            if (VERSION.SDK_INT < 17) {
                rawUserId = BuildConfig.FLAVOR;
            } else {
                String[] folders = DIR_SEPORATOR.split(Environment.getExternalStorageDirectory().getAbsolutePath());
                String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                } catch (NumberFormatException e) {
                }
                rawUserId = isDigit ? lastFolder : BuildConfig.FLAVOR;
            }
            if (TextUtils.isEmpty(rawUserId)) {
                rv.add(rawEmulatedStorageTarget);
            } else {
                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        } else if (TextUtils.isEmpty(rawExternalStorage)) {
            rv.add("/storage/sdcard0");
        } else {
            rv.add(rawExternalStorage);
        }
        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
            Collections.addAll(rv, rawSecondaryStoragesStr.split(File.pathSeparator));
        }
        return (String[]) rv.toArray(new String[rv.size()]);
    }

    public static String fetchFromPreferences(String key) {
        String str = Const.APP_CONFIG;
        return context.getSharedPreferences(str, 0).getString(key, null);
    }

    public static void saveInPreferences(String key, String value) {
        String str = Const.APP_CONFIG;
        Editor editor = context.getSharedPreferences(str, 0).edit();
        editor.putString(key, value.toLowerCase());
        editor.commit();
    }

    public static void login() {
/*
        if (fetchFromPreferences(Const.USERNAME) != null) {
            ApplicationController.getInstance().addToRequestQueue(new LoginRequest(1, URL.LOGIN, new LoginResponseListener(), new LoginErrorResponseListener()));
        }
*/
    }

}