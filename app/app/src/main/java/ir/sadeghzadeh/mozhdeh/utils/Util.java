package ir.sadeghzadeh.mozhdeh.utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build.VERSION;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import ir.sadeghzadeh.mozhdeh.BuildConfig;
import ir.sadeghzadeh.mozhdeh.Const;
import ir.sadeghzadeh.mozhdeh.MainActivity;
import ir.sadeghzadeh.mozhdeh.entity.Item;

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
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean fetchBooleanFromPreferences(String key, boolean defaultValue) {
        String str = Const.APP_CONFIG;
        return context.getSharedPreferences(str, 0).getBoolean(key, defaultValue);
    }

    public static void saveBooleanInPreferences(String key, boolean value) {
        String str = Const.APP_CONFIG;
        Editor editor = context.getSharedPreferences(str, 0).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    public static void login() {
/*
        if (fetchFromPreferences(Const.USERNAME) != null) {
            ApplicationController.getInstance().addToRequestQueue(new LoginRequest(1, URL.LOGIN, new LoginResponseListener(), new LoginErrorResponseListener()));
        }
*/
    }

    public static void moveFile(String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputFile);
            out = new FileOutputStream(inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputFile).delete();


        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    public static Location getLocation() {
        Location location=null;
        try {
            double latitude; // latitude
            double longitude; // longitude

            LocationManager locationManager = (LocationManager) context
                    .getSystemService(context.LOCATION_SERVICE);

            // getting GPS Status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network Status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                // First get location from Network Provider
                if (isNetworkEnabled) {

                    if (locationManager != null) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return null;
                        }
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

         return location;
    }

    public static boolean isUserLogged(){
        String token = fetchFromPreferences(Const.TOKEN);
        if(token  ==  null || token.isEmpty()){
            return false;
        }
        return true;
    }

    public static String imageUrlMaker(boolean thumbnail, Item item){
        String  url  = "";
        if(thumbnail){
            url = Const.SERVER_URL + Const.THUMBNAIL_URL + "/" + item.id + item.ImageExt;
        }else {
            url = Const.SERVER_URL + Const.FULL_IMAGE_URL + "/" + item.id + item.ImageExt;

        }
        return url;
    }

    public static String buildCommaSeperate(List<String> list){
        String result  =  "";
        if(list.size() == 0){
            return result;
        }

        for (String row : list){
            result += row + ",";
        }
        result  =  result.substring(0,result.length()-1); //remove  last comma
        return result;
    }

}

