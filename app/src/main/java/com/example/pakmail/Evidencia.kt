package com.example.pakmail

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.pakmail.services.Utils
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_detalle.*
import kotlinx.android.synthetic.main.activity_evidencia.*
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.HashMap


class Evidencia : AppCompatActivity() {
   var id_ticket = ""
    private val IR_A_DIBUJAR = 632
    private var rutaImgDibujo: String? = null
    private  var rutaFoto: String? = null
    var id_estado= ""
    var Imgfirma: String? = ""
    var nombre= ""
    var telefono= ""
    var utils: Utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evidencia)


        id_ticket = intent.getStringExtra("id_ticket")
        id_estado= intent.getStringExtra("estatus")
        nombre= intent.getStringExtra("nom")
        telefono= intent.getStringExtra("tel")
nombret.setText(nombre)
        telefonot.setText(telefono)



        btn_dibujar.setOnClickListener(View.OnClickListener {


                val dib = Intent(this, DibujarActivity::class.java)

                startActivityForResult(dib,IR_A_DIBUJAR)


        })

        btn_dibujar.setOnClickListener(View.OnClickListener {


            val dib = Intent(this, DibujarActivity::class.java)

            startActivityForResult(dib,IR_A_DIBUJAR)


        })
        modificarevidencia.setOnClickListener(View.OnClickListener {


            modificar()


        })

    }


    fun modificar() {
            val progressDialog = ProgressDialog(
                this,
                R.style.Theme_AppCompat_Light_Dialog
            )
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Cargando datos...")
            progressDialog.show()
            val json = JSONObject()
            try {
                json.put("accion", "actualizar")
                json.put("estatus", id_estado)
                json.put("id", id_ticket)
                json.put("recibe_firma", Imgfirma.toString())
                json.put("recibe_nombre", nombre)
                json.put("recibe_telefono", telefono)
                utils.WsRequest(
                    applicationContext,
                    Request.Method.POST,
                    Endpoint.URL_GUIA,
                    json,
                    null,
                    null,
                    object : Utils.ServerVolleyCallback {
                        override fun onSucces(result: String?) {
                            progressDialog?.dismiss()
                            val objectMapper = ObjectMapper()
                            val resp: JsonObject = JsonParser().parse(result).asJsonObject
                            val success = (resp as JsonObject).get("success").toString()
                            if (success == "true") {
                                _ShowAlert2("Estatus actualizado", "Bien echo")
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
    private fun _ShowAlert2(title: String, mensaje: String) {
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



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== IR_A_DIBUJAR) {
            try {
                if (data != null) {
                    rutaImgDibujo = data.extras!!.getString("rutaImg")

                    Glide.with(this)
                            .asBitmap()
                            .load(rutaImgDibujo)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(

                                        resource: Bitmap,
                                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                                ) {
                                    img_dibujo.setImageBitmap(resource)
                                    Imgfirma = encodeImage(resource)

                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                    // this is called when imageView is cleared on lifecycle call or for
                                    // some other reason.
                                    // if you are referencing the bitmap somewhere else too other than this imageView
                                    // clear it here as you can no longer have the bitmap
                                }
                            })
                }
            }  catch (e: IOException) {
                Toast.makeText(
                        this,
                        e.toString(),
                        Toast.LENGTH_SHORT
                ).show()
            }

        }


    }
    private fun encodeImage(bm: Bitmap?): String {
        val baos = ByteArrayOutputStream()
        bm!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
    }
