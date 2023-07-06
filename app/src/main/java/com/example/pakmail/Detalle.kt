package com.example.pakmail

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.Volley
import com.example.pakmail.services.Utils
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_detalle.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_buscarguia.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class Detalle : AppCompatActivity() {
    var id= 0
    var guia=""
    var escaneo=""
    var idestado = ArrayList<String>()
    var nombre = ArrayList<String>()
    var id_estado = ""
    var estatus = ""
    var id_ticket = ""
    var nom = ""
    var tel = ""
    var utils: Utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)


         guia = intent.getStringExtra("folio")
        escaneo= intent.getStringExtra("escaneo")
        detalle()
        Titulodetalle.setText("Guia: " + guia)


        button4.setOnClickListener(){
            modificar()
        }
    }

    fun detalle() {
        val progressDialog = ProgressDialog(this,
            R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()

        val json = JSONObject()
        try {
            json.put("accion", "listar")
            json.put("folio", guia)
            utils.WsRequest(
                this,
                Request.Method.POST,
                Endpoint.URL_GUIA,
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


                        val array = JSONArray(result)
                        if (array.length() > 0) {
                            val guias = array.getJSONObject(0)
                        val tfecha_recoleccion = guias.getString("fecha_recoleccion")
                             id = guias.getInt("id")
                            val ttipo_paquete = guias.getString("tipo")
                            val cantidad = guias.getString("cantidad")
                            val tdescripcion_paquete = guias.getString("descripcion")
                         //   val tnombre_alterno_recibe = guias.getString("nombre_alterno_recibe")
                            // val ttelefono_alterno_recibe = guias.getString("telefono_alterno_recibe")
                            val trecolectar = guias.getString("recoleccion")
                            val direccionrecolecion = guias.getString("direccion")
                            val misma_direccion_recoleccion = guias.getString("misma_direccion_recoleccion")
                            val recoleccion = guias.getString("recoleccion")
                            val tnombrer = guias.getString("destinatario")
                            val ttelefonor = guias.getString("destinatario_telefono")
                            val tdireccionr = guias.getString("destinatario_direccion")
                            val ttelefonod = guias.getString("remitente_telefono")
                            val tnombred = guias.getString("remitente")
                            val tdirecciond = guias.getString("remitente_direccion")
                            val ttipo_rda = guias.getString("tipo_entrega")
                            estatus = guias.getString("estatus")
                            val persona_entrega = guias.getString("persona_entrega")
                          //  val tcomentarios = persona_entrega_data.getString("comentarios")
                        /*    val dimensiones = guias.getJSONArray("misma_dimension")
                            id_estado = tid_estado
                            var tpeso = 0
                            var tpiezas = dimensiones.length()
                            val arr = IntArray(dimensiones.length())
                            for (i in 0 until dimensiones.length()) {
                                val producto = dimensiones.getJSONObject(i)
                                tpeso = tpeso + producto.getString("dp").toInt()
                            }*/
                            nombrer.text = tnombrer
                            telefonor.text = ttelefonor
                            direccionr.text = tdireccionr
                            nombred.text = tnombred
                            telefonod.text = ttelefonod
                            direcciond.text = tdirecciond
                            piezas.text = cantidad
                            tipo.text = ttipo_paquete
                         //   peso.text = tpeso.toString()
                          //  piezas.text = tpiezas.toString()
                            descripcion.text = tdescripcion_paquete.toString()
                          //  nombrealterno.text = tnombre_alterno_recibe
                           // telefonoalterno.text = ttelefono_alterno_recibe
                            if (misma_direccion_recoleccion.equals("1")) {
                                recolecciondireccion.text = tdireccionr
                            } else {
                                recolecciondireccion.text = direccionrecolecion
                            }
                            fecha_recoleccion.text = tfecha_recoleccion
                            persona_recoleccion.text = persona_entrega
                            nom = tnombred
                            tel = ttelefonod
                            if (trecolectar.equals("1")) {
                                l12.setVisibility(LinearLayout.VISIBLE);
                            } else{
                                l12.setVisibility(LinearLayout.INVISIBLE);
                            }


                            if (id_estado == "1") {
                              //  sproveedor.setEnabled(false)
                               // button4.setEnabled(false)
                            }
                            estatusenvio()
                        } else {

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


    fun modificar() {

        var idu = sproveedor.selectedItemPosition
        id_estado = idestado.get(idu)
        if (id_estado == "1") {

            val intent = Intent(this, Evidencia::class.java)
            intent.putExtra("id_ticket", id.toString())
            intent.putExtra("estatus", nombre.get(idu))
            intent.putExtra("nom", nom)
            intent.putExtra("tel", tel)
            startActivityForResult(intent, 1)
        } else {

            val preferencias = this.getSharedPreferences(
                "variables",
                Context.MODE_PRIVATE
            )
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
                json.put("estatus", nombre.get(idu))
                json.put("id", id)
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
    }

    fun modificar2(view: View) {
        var idu= sproveedor.selectedItemPosition
        id_estado=idestado.get(idu)
  if(id_estado=="1"){

      val intent = Intent(this, Evidencia::class.java)
      intent.putExtra("id_ticket", id_ticket)
      intent.putExtra("id_estado", id_estado)
      intent.putExtra("nom", nom)
      intent.putExtra("tel", tel)
      startActivityForResult(intent,1)
   } else {
      val progressDialog = ProgressDialog(
          this,


          R.style.Theme_AppCompat_Light_Dialog
      )
      progressDialog.isIndeterminate = true
      progressDialog.setMessage("Cargando datos...")
      progressDialog.show()
      val preferencias = this.getSharedPreferences(
              "variables",
              Context.MODE_PRIVATE
      )
      var id=preferencias.getString("id", "").toString()


      var  URL_API = "https://servicios.pakmailmetepec.com/tickets.php"
      val requestQueue: RequestQueue = Volley.newRequestQueue(this)
      val dat = HashMap<String, String>()
      dat.put("accion", "updateEstado")
      dat.put("id", id_ticket)
      dat.put("id_estado", id_estado)
      dat.put("id_user", id)
      val request = JsonCustomRequestPHP(
          Request.Method.POST,
          URL_API,
          dat,
          Response.Listener { response ->


              progressDialog?.dismiss()
              val result = response["resultado"] as Int
              if (result == 1) {
                  _ShowAlert2("Estatus actualizado", "Bien echo")

              } else {
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
    private fun _ShowAlert(title: String, mensaje: String) {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(mensaje)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, "Aceptar"
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
            AlertDialog.BUTTON_NEUTRAL, "Aceptar"
        ) { dialog, which ->
            dialog.dismiss()
        }
        alertDialog.show()
    }




    fun estatusenvio() {
        val progressDialog = ProgressDialog(this,


            R.style.Theme_AppCompat_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()

        val json = JSONObject()
        try {
           utils.WsRequest(
                applicationContext,
                Request.Method.GET,
                Endpoint.URL_ESTATUS,
                json,
                null,
                null,
                object : Utils.ServerVolleyCallback {
                    override fun onSucces(result: String?) {
                        progressDialog?.dismiss()
                        val objectMapper = ObjectMapper()
                        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                        val resp: JsonObject = JsonParser().parse(result).asJsonObject
                        val datos = resp["datos"]
                        val resultado = (resp as JsonObject).get("resultado").toString()
                        if (resultado == "1") {
                            var nombret = ""
                            val array = JSONArray(datos.toString())
                            if ((datos as JsonArray).size() > 0) {
                                for( i in 0 until array.length()) {
                                    val producto = array.getJSONObject(i)
                                    var idtempo = producto.getString("nombre").toString()
                                    if (idtempo != estatus) {
                                        idestado.add(producto.getString("id"))
                                        nombre.add(producto.getString("nombre"))
                                    } else {
                                        nombret = producto.getString("nombre")
                                    }
                                }
                                var idestadot = ArrayList<String>()
                                var nombrett = ArrayList<String>()
                                idestadot.add(id_estado)
                                nombrett.add(nombret)

                                for (a in 0 until nombre.count()) {
                                    idestadot.add(idestado.get(a))
                                    nombrett.add(nombre.get(a))
                                }
                                idestado = idestadot
                                nombre = nombrett


                                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                    this@Detalle,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    nombrett
                                )
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                                sproveedor.setAdapter(adapter)
                            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        detalle()
    }

}