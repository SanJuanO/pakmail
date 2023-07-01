package com.example.pakmail.services;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public interface ServerVolleyCallback{
        void onSucces(String result);
        void Error(VolleyError error);
    }


    public static void Cronometro(int duracion, final Activity activity, final Class activityb){
        new Handler().postDelayed(new Runnable(){
            public void run(){
                // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                Intent intent = new Intent(activity, activityb);
                activity.startActivity(intent);
                activity.finish();
            };
        }, duracion);
    }

    public void ChangeColor(Activity activity,String Codcolor){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(Codcolor));
        }
    }


    public SharedPreferences Objeto(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("volvo", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public SharedPreferences.Editor Editor(Context context){
        SharedPreferences.Editor editor = Objeto(context).edit();
        return editor;
    }

    public static String getMessageErrorVolley(VolleyError error){
        String message = null;
        try {
            message = new String(error.networkResponse.data, "UTF-8");
            System.out.println(message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return message;
    }


    public void WsRequest(final Context context, int method, String URL, JSONObject jsonObject,
                          final String token, final Dialog progressDialog, final ServerVolleyCallback callback) {

        if(progressDialog != null){
            progressDialog.show();
        }
        System.out.println("URL :"+URL);
        System.out.println("Params:"+jsonObject);
        String mRequestBody = "";
        if(jsonObject != null){
            mRequestBody = jsonObject.toString();
        }
        String version="";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final String finalVersion = version;
        final String finalMRequestBody = mRequestBody;
        StringRequest stringRequest = new StringRequest(method, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response);
                        callback.onSucces(response);
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.Error(error);
                Boolean checkHost = true;
                CharSequence msj = "";
                callback.Error(error);
                System.out.println("Error: "+error);

                if (error instanceof NoConnectionError) {
                    checkHost = false;
                    msj = "Error con el Host";
                    Toast.makeText(context.getApplicationContext(), msj, Toast.LENGTH_SHORT).show();
                }
                if (error instanceof TimeoutError) {
                    checkHost = false;
                    msj = "Error de conexion";
                    Toast.makeText(context.getApplicationContext(), msj, Toast.LENGTH_SHORT).show();
                }

                if (checkHost) {
                    String errorJson = getMessageErrorVolley(error);
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

                    try {
                        JSONObject jsonObject = new JSONObject(errorJson);
                        Toast.makeText(context.getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                //headers.put("User-Agent","GanaMasExecutiveAndroid/"+finalVersion);
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                System.out.println("Headers : " + headers);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return finalMRequestBody == null ? null : finalMRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {

                    return null;
                }
            }

        };

        /* stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        //RequestQueueSingleton.getInstance(context.getApplicationContext()).getRequestQueue().add(stringRequest);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }


}
