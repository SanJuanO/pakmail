package com.example.pakmail

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.*
import com.android.volley.toolbox.Volley
import com.example.pakmail.permission.PermissionsActivity
import com.example.pakmail.permission.PermissionsChecker
import com.example.pakmail.services.Utils
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class Login : AppCompatActivity() {
    var linea = ArrayList<String>()
    var REQUEST_LOCATION = 1
    var lineapk = ArrayList<String>()
    var utils: Utils = Utils()

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
            _cellText.setText(preferences.getString("email", "")?.replace("\"","") ?: "")
            _passwordText.setText(preferences.getString("password", "")?.replace("\"","") ?: "")
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
            AlertDialog.BUTTON_NEUTRAL, "Aceptar"
        ) { dialog, which ->
            dialog.dismiss()
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

        val json = JSONObject()
        try {
            json.put("accion", "login")
            json.put("correo", _cellText.text.toString())
            json.put("password", _passwordText.text.toString())
            utils.WsRequest(
                applicationContext,
                Request.Method.POST,
                Endpoint.URL_MENSAJERO,
                json,
                null,
                null,
                object : Utils.ServerVolleyCallback {
                    override fun onSucces(result: String?) {
                        progressDialog?.dismiss()
                        val objectMapper = ObjectMapper()
                        val preferences = getSharedPreferences("variables", Context.MODE_PRIVATE)
                        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        val editor = preferences.edit()

                        val resp: JsonObject = JsonParser().parse(result).asJsonObject
                        val datos = resp["datos"]
                        val success = (resp as JsonObject).get("success").toString()
                        if (success == "true") {
                            editor.putString("id", (datos as JsonObject).get("id").toString())
                            editor.putString(
                                "nombre",
                                (datos as JsonObject).get("nombre").toString()
                            )
                            editor.putString(
                                "telefono",
                                (datos as JsonObject).get("telefono").toString()
                            )
                            editor.putString(
                                "email",
                                (datos as JsonObject).get("correo").toString()
                            )
                            editor.putString(
                                "password",
                                _passwordText.text.toString()
                            )
                            if (checkBox.isChecked) {
                                editor.putString("sesion", "si")
                            }



                            editor.commit()
                            val intent = Intent(
                                this@Login,
                                MainActivity::class.java
                            )
                            startActivity(intent)
                            finish()
                        } else {
                            _ShowAlert("Error", (resp as JsonObject).get("message").toString())
                        }

                    }

                    override fun Error(error: VolleyError?) {
                        progressDialog?.dismiss()

                    }
                })
        } catch (e: JSONException) {
            progressDialog?.dismiss()
            e.printStackTrace()
        }
    }

}
