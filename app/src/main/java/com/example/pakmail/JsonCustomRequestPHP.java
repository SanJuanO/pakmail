package com.example.pakmail;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/*-------------------------------------------------------------SCH EJEMPLO COMO CONSUMIR ABAJO-------------*/
public class JsonCustomRequestPHP extends Request<JSONObject> {

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public JsonCustomRequestPHP(String url, Map<String, String> params, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = responseListener;
        this.params = params;
    }

    public JsonCustomRequestPHP(int method, String url,Map<String, String> params, Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }
}

/*--------------------------EJEMPLO COMO CONSUMIR-----------------------*/
/*

    public void login() {

        URL_API="https://servicios.pakmailmetepec.com/mensajeros1.php";
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        Map<String,String>dat=new HashMap<>();
        dat.put("accion","login");
        dat.put("usuario","oscar");
        dat.put("password","oscar");

        JsonCustomRequestPHP request=new JsonCustomRequestPHP(Request.Method.POST,URL_API, dat, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("SCH", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("SCH", error.toString());
            }
        });

        int MY_SOCKET_TIMEOUT_MS = 15000;
        int maxRetries = 2;
        request.setRetryPolicy(new
                        DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        maxRetries,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        requestQueue.add(request);

    }
 */
