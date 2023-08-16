package com.example.pakmail

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.pakmail.R.layout.fragment_perfil
import kotlinx.android.synthetic.main.fragment_perfil.*


class Perfil : Fragment() {
    companion object {
        fun newInstance(): Perfil = Perfil()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(fragment_perfil, container, false)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(state: Bundle?)
    {
        super.onActivityCreated(state)
        val preferencias = this.requireActivity().getSharedPreferences("variables", Context.MODE_PRIVATE)

        var role= "Mensajero"

        var nombre = preferencias.getString("nombre", "").toString().replace("\"", "")

        var  telefono= preferencias.getString("telefono", "").toString().replace("\"", "")
        var email= preferencias.getString("email", "").toString().replace("\"", "")


        //val decodedString1 = Base64.decode(imag, Base64.DEFAULT)
        //val decodedByte = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.size)
        // val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, decodedByte)
        // roundedBitmapDrawable.isCircular = true
        //fotouser.setImageDrawable(roundedBitmapDrawable)
      nombretext.text=nombre
        telefonotext.text=telefono
        emailtext.text=email


//       telefonotext.text=telefono
  //      correotext.text =correo

cerrarcesion.setOnClickListener(){
    val preferencias = this.requireActivity().getSharedPreferences("variables", Context.MODE_PRIVATE)

    val editor = preferencias.edit()
    editor.putString("sesion", "no")
    editor.commit()
    val intent = Intent(activity, Login::class.java)

    // start your next activity
    startActivity(intent)
    this.requireActivity().finish();
}
    }



}
