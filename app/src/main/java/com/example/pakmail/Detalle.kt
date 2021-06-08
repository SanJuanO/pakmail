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
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_detalle.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class Detalle : AppCompatActivity() {
    var id=""
    var guia=""
    var escaneo=""
    var idestado = ArrayList<String>()
    var nombre = ArrayList<String>()
    var id_estado = ""
    var id_ticket = ""
    var nom = ""
    var tel = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)


         guia = intent.getStringExtra("folio")
        escaneo= intent.getStringExtra("escaneo")


        detalle()
        Titulodetalle.setText("Guia: " + guia)
    }


    fun modificar(view: View) {
        var idu= sproveedor.selectedItemPosition
        id_estado=idestado.get(idu)
  if(id_estado=="1"){

      val intent = Intent(this, Evidencia::class.java)
      intent.putExtra("id_ticket", id_ticket)
      intent.putExtra("id_estado", id_estado)
      intent.putExtra("nom", nom)
      intent.putExtra("tel", tel)
      startActivityForResult(intent,1)


  }else{
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





    fun detalle() {

        val progressDialog = ProgressDialog(
            this,
            R.style.Theme_AppCompat_Light_Dialog
        )
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Cargando datos...")
        progressDialog.show()


        var  URL_API = "https://servicios.pakmailmetepec.com/tickets.php"
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val dat = HashMap<String, String>()
        dat.put("accion", "getByRastreo")
        dat.put("rastreo", guia)


        val request = JsonCustomRequestPHP(
            Request.Method.POST,
            URL_API,
            dat,
            Response.Listener { response ->


                progressDialog?.dismiss()
                val result = response["resultado"] as Int
                if (result == 1) {
                    val preferencias = this.getSharedPreferences(
                        "variables",
                        Context.MODE_PRIVATE
                    )

                    val editor = preferencias.edit()

                    val guias = response.getJSONObject("datos")
                    id_ticket = guias.getString("id")

                    val trastreo = guias.getString("rastreo")
                    val tdireccion = guias.getString("direccion")
                    val ttelefono = guias.getString("telefono")
                    val tpersona_entrega = guias.getString("persona_entrega")
                    val tpersona_recibe = guias.getString("persona_recibe")
                    val tfecha_recoleccion = guias.getString("fecha_recoleccion")
                    val tfecha_recibido = guias.getString("fecha_recibido")
                    val tid_user = guias.getString("id_user")
                    val tid_estado = guias.getString("id_estado")


                    val ttipo_paquete = guias.getString("tipo_paquete")
                    val tdescripcion_paquete = guias.getString("descripcion_paquete")
                    val tnombre_alterno_recibe = guias.getString("nombre_alterno_recibe")
                    val ttelefono_alterno_recibe = guias.getString("telefono_alterno_recibe")
                    val trecolectar = guias.getString("recolectar")
                    val ttipoentrega = guias.getString("tipoentrega")
                    val tdireccionrecolecion = guias.getString("direccionrecolecion")
                    val tpersonarecolecion = guias.getString("personarecolecion")
                    val thora_recoleccion = guias.getString("hora_recoleccion")
                    val trecibe_firma = guias.getString("recibe_firma")
                    val trecibe_nombre = guias.getString("recibe_nombre")
                    val trecibe_telefono = guias.getString("recibe_telefono")


                    val persona_recibe_data = guias.getJSONObject("persona_recibe_data")

                    val tnombrer = persona_recibe_data.getString("nombre")
                    val trepresentanter = persona_recibe_data.getString("representante")
                    val ttelefonor = persona_recibe_data.getString("telefono")

                    val temailr = persona_recibe_data.getString("email")
                    val tdireccionr = persona_recibe_data.getString("calle") + ", " +
                            persona_recibe_data.getString("municipio") + ", " +
                            persona_recibe_data.getString("colonia") + ", " +
                            persona_recibe_data.getString("estado") + ", " +
                            persona_recibe_data.getString("pais") + ", CP: "
                    persona_recibe_data.getString("cp")

                    val persona_entrega_data = guias.getJSONObject("persona_entrega_data")
                    val ttelefonod = persona_recibe_data.getString("telefono")

                    val tnombred = persona_entrega_data.getString("nombre")
                    val trepresentanted = persona_entrega_data.getString("representante")
                    val temaild = persona_entrega_data.getString("email")
                    val tdirecciond = persona_entrega_data.getString("calle") + ", " +
                            persona_entrega_data.getString("municipio") + ", " +
                            persona_entrega_data.getString("colonia") + ", " +
                            persona_entrega_data.getString("estado") + ", " +
                            persona_entrega_data.getString("pais") + ", CP: "
                    persona_entrega_data.getString("cp")
                    val ttipo_rda = persona_entrega_data.getString("tipo_rda")
                    val ttipo_usuario = persona_entrega_data.getString("tipo_usuario")
                    val testatus = persona_entrega_data.getString("estatus")

                    val tcomentarios = persona_entrega_data.getString("comentarios")
                    val dimensiones = guias.getJSONArray("dimensiones")
                    id_estado = tid_estado
                    var tpeso = 0
                    var tpiezas = dimensiones.length()

                    val arr = IntArray(dimensiones.length())


                    for (i in 0 until dimensiones.length()) {
                        val producto = dimensiones.getJSONObject(i)

                        tpeso = tpeso + producto.getString("dp").toInt()


                    }
                    nombrer.text = tnombrer
                    telefonor.text = ttelefonor
                    direccionr.text = tdireccionr

                    nombred.text = tnombred
                    telefonod.text = ttelefonod
                    direcciond.text = tdirecciond

                    tipo.text = ttipo_paquete
                    peso.text = tpeso.toString()
                    piezas.text = tpiezas.toString()
                    descripcion.text = tdescripcion_paquete.toString()
                    nombrealterno.text = tnombre_alterno_recibe
                    telefonoalterno.text = ttelefono_alterno_recibe

                    recolecciondireccion.text = tdireccionrecolecion
                    fecha_recoleccion.text = tfecha_recoleccion + " " + thora_recoleccion
                    persona_recoleccion.text = tpersonarecolecion
                    nom = tnombred

                    tel = ttelefonod
                    if (trecolectar != "1") {

                        l12.setVisibility(LinearLayout.GONE);
                    }

                    if (tnombre_alterno_recibe == "") {


                        ll99.setVisibility(LinearLayout.GONE);
                    } else {
                        nom = tnombre_alterno_recibe
                        tel = ttelefono_alterno_recibe
                    }

                    if (id_estado == "1") {
                        sproveedor.setEnabled(false)
                        button4.setEnabled(false)
                    }
                    estatusenvio()
                } else {
                    _ShowAlert("Error", "guia no encontrada")
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



        var  URL_API = "https://servicios.pakmailmetepec.com/estatus_envio.php"
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val dat = HashMap<String, String>()



        val request = JsonCustomRequestPHP(
            Request.Method.GET,
            URL_API,
            dat,
            Response.Listener { response ->


                val result = response["resultado"] as Int
                if (result == 1) {
                    var nombret = ""
                    val costos = response.getJSONArray("datos")
                    for (i in 0 until costos.length()) {
                        val producto = costos.getJSONObject(i)
                        var idtempo = producto.getString("id").toString()
                        if (idtempo != id_estado) {
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
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        nombrett
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                    sproveedor.setAdapter(adapter)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        detalle()

    }

}