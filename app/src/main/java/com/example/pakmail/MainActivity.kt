package com.example.pakmail


import Codigoqr
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

import kotlinx.android.synthetic.main.activity_detalle.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity() {
    var TOKEN: String=""
    var id: String=""
    var correo= String()
    var toque:Boolean= false
    var permisos = ArrayList<String>()

    var URL:String="https://appis-apizaco.gesdesapplication.com/api/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val preferences = getSharedPreferences("VARIABLES", Context.MODE_PRIVATE)
        val temp = preferences.getString("correo", "")
        val t = preferences.getString("iduser", "")!!
        id=t
        correo = temp.toString()
        URL +="EditTokenUsuarios"

        registraToken()
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationViewinicio)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val vent = Codigoqr.newInstance()
        openFragment(vent)

    }
    override fun onBackPressed() { // Añade más funciones si fuese necesario
        finish()
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.Codigoqr -> {

                val inci = Codigoqr.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }
            R.id.perfil -> {
                //alertas.setBackgroundColor(R.drawable.btn_orange_light)
                val inci = Perfil.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }

            R.id.buscarguia -> {
                //alertas.setBackgroundColor(R.drawable.btn_orange_light)
                val inci = Buscarguia.newInstance()
                openFragment(inci)
                return@OnNavigationItemSelectedListener true
            }



        }
        false
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    fun permisosobtener() {


        val datos = JSONObject()
        try {
            datos.put("Correo", correo)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val requstQueue = Volley.newRequestQueue(this)
        val progressDialog = ProgressDialog(
            this,
            R.style.Theme_AppCompat_Light_Dialog
        )
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,
            "https://inventarios.gesdesapplication.com/api/UsuariosApi/getUsuarioByCorreo",
            datos,
            Response.Listener<JSONObject> { response ->
                try {
                    progressDialog?.dismiss()
                    val result = response.get("resultado") as Int
                    progressDialog?.dismiss()
                    if (result == 1) {
                        try {


                            val guias = response.getJSONObject("data")
                            id = guias.getString("id")


                            val preferencias = this.getSharedPreferences(
                                "variables",
                                Context.MODE_PRIVATE
                            )

                            val editor = preferencias.edit()
                            val costos = guias.getJSONArray("listaPermisos")
                            for (i in 0 until costos.length()) {
                                val producto = costos.getJSONObject(i)
                            }


                        } catch (es: Exception) {
                            Log.d("sergio1", "" + es.toString())
                            finish()
                            progressDialog?.dismiss()
                        }

                    } else {
                        Toast.makeText(this, "Articulo no encotrado", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    progressDialog?.dismiss()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {

                }
            }
        ) {

            //here I want to post data to sever
        }

        val MY_SOCKET_TIMEOUT_MS = 15000
        val maxRetries = 2
        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                maxRetries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )

        requstQueue.add(jsonObjectRequest)


    }

    fun registraToken() {
        FirebaseApp.initializeApp(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            val preferencias = getSharedPreferences("variables", Context.MODE_PRIVATE)

            var id=preferencias.getString("id", "").toString()

            val deviceToken: String = token.toString()
            var  URL_API = "https://servicios.pakmailmetepec.com/mensajeros.php"
            val requestQueue: RequestQueue = Volley.newRequestQueue(this@MainActivity)
            val dat = HashMap<String, String>()
            dat.put("accion", "updateToken")
            dat.put("token", deviceToken)
            dat.put("id",id )


            val request = JsonCustomRequestPHP(
                Request.Method.POST,
                URL_API,
                dat,
                Response.Listener { response ->


                    val result = response["resultado"] as Int
                    if (result == 1) {


                    }
                    else {
                    }

                },
                Response.ErrorListener { error ->

                })
            val MY_SOCKET_TIMEOUT_MS = 15000
            val maxRetries = 2
            request.retryPolicy = DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                maxRetries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            requestQueue.add(request)

        })

    }


}
