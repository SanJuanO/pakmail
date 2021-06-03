package com.example.pakmail

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.pakmail.R.layout.fragment_buscarguia
import kotlinx.android.synthetic.main.fragment_buscarguia.*
import kotlinx.android.synthetic.main.fragment_perfil.*


class Buscarguia : Fragment() {
    companion object {
        fun newInstance(): Buscarguia = Buscarguia()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(fragment_buscarguia, container, false)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(state: Bundle?)
    {
        super.onActivityCreated(state)

        buscarguia.setOnClickListener(){
            buscarguia()
        }

    }





    fun buscarguia(){
        var guia= textoguia.text.toString()
if(guia != ""  && guia.count()!=10) {
    val intent = Intent(activity, Detalle::class.java)
    // start your next activity
    val preferencias = requireActivity().getSharedPreferences("variables", Context.MODE_PRIVATE)

    val editor = preferencias.edit()
    editor.putString("escaneo", "true")
    editor.commit()
    intent.putExtra("escaneo", "true")

    intent.putExtra("folio", guia)
    startActivity(intent)

}else{
    _ShowAlert("Error", "guia no valida")

}
    }


    private fun _ShowAlert(title: String, mensaje: String) {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(mensaje)
        alertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL, "Aceptar"
        ) { dialog, which ->
            dialog.dismiss()
        }
        alertDialog.show()
    }


}
