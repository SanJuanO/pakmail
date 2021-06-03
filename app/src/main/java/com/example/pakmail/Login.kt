package com.example.pakmail

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pakmail.permission.PermissionsActivity
import com.example.pakmail.permission.PermissionsChecker
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class Login : AppCompatActivity() {
    var linea = ArrayList<String>()
    var REQUEST_LOCATION = 1
    var lineapk = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        pedirPermisos()
        val preferences = getSharedPreferences("variables", Context.MODE_PRIVATE)
        val idt = preferences.getString("sesion", "")

        var checker: PermissionsChecker? = null

        checker = PermissionsChecker(this)


        if (checker!!.lacksPermissions(*PermissionsChecker.REQUIRED_PERMISSION)) {
            PermissionsActivity.startActivityForResult(
                this,
                PermissionsActivity.PERMISSION_REQUEST_CODE,
                *PermissionsChecker.REQUIRED_PERMISSION
            )
        }

        if (idt.equals("si")) {


            buttoningresar.isEnabled = false
            _cellText.setText(preferences.getString("usuario", ""))
            _passwordText.setText(preferences.getString("password", ""))
            login2()

        }


    }
    fun entrar(v: View?) {

        var nom=_cellText.text.toString()
        var pass=_passwordText.text.toString()

        if(nom==""){
            Toast.makeText(this, "Ingrese usuario", Toast.LENGTH_SHORT).show()

        }

        else if(pass==""){
            Toast.makeText(this, "Ingrese password", Toast.LENGTH_SHORT).show()

        }
        else {
            login2()
        }

    }


    private fun _ShowAlert(title: String, mensaje: String) {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(mensaje)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, "OK"
        ) { dialog, which ->
            dialog.dismiss()
            finish()
        }
        alertDialog.show()
    }

    fun pedirPermisos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA),
                    REQUEST_LOCATION)
            }
        }
    }
    fun login2() {
        val progressDialog = ProgressDialog(this,


            R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()


       var  URL_API = "https://servicios.pakmailmetepec.com/mensajeros.php"
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val dat = HashMap<String, String>()
        dat.put("accion", "login")
        dat.put("usuario", _cellText.text.toString())
        dat.put("password", _passwordText.text.toString())


        val request = JsonCustomRequestPHP(
            Request.Method.POST,
            URL_API,
            dat,
            Response.Listener { response ->


                progressDialog?.dismiss()
                val result = response["resultado"] as Int
                if (result == 1) {
                    val preferencias = this.getSharedPreferences("variables", Context.MODE_PRIVATE)

                    val editor = preferencias.edit()

                    val guias = response.getJSONObject("datos")
                    val id = guias.getString("id")
                    val usuario = guias.getString("usuario")
                    val password = _passwordText.text.toString()
                    val nombre = guias.getString("nombre")
                    val email = guias.getString("email")
                    val telefono = guias.getString("telefono")
                    val activo = guias.getString("activo")
                    val token = guias.getString("token")
                    val borrado = guias.getString("borrado")


                    editor.putString("id", id)
                    editor.putString("usuario", usuario)
                    editor.putString("nombre", nombre)
                    editor.putString("telefono", telefono)
                    editor.putString("email", email)
                    editor.putString("password", password)
                    if (activo == "1" && borrado == "0") {


                    if (checkBox.isChecked) {
                        editor.putString("sesion", "si")
                    }



                    editor.commit()
                    val sendMailIntent = Intent(this, MainActivity::class.java)
                    startActivity(sendMailIntent)
                    finish()
                } else {

                    val error = response.getString("mensaje")
                    _ShowAlert("Error", error)
                }
            }
            else {
                    _ShowAlert("Error", "intente mas tarde")
                }

            },
            Response.ErrorListener { error ->
                _ShowAlert("Error", "intente mas tarde")

            })
        val MY_SOCKET_TIMEOUT_MS = 15000
        val maxRetries = 2
        request.retryPolicy = DefaultRetryPolicy(
            MY_SOCKET_TIMEOUT_MS,
            maxRetries,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(request)
    }

}
