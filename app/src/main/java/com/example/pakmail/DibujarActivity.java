package com.example.pakmail;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kyanogen.signatureview.SignatureView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DibujarActivity extends AppCompatActivity {

    private static final int COD_PERMISOS = 426;
    private LinearLayout layoutProgresoImagen;
    private SignatureView signatureView;
    private String ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dibujar);

        layoutProgresoImagen = (LinearLayout) findViewById(R.id.layout_progreso_firma);
        signatureView = (SignatureView) findViewById(R.id.firma);
    }
    public void ok(View view) {
        if(verificarPermisos()) {
            layoutProgresoImagen.setVisibility(View.VISIBLE);// Hacemos visible el progreso

            new Thread(new Runnable() {
                @Override
                public void run() {

                    FileOutputStream out = null;
                    try {
                        SimpleDateFormat hor = new SimpleDateFormat("hhmmss");

                        String hora = hor.format(new Date());
                        ruta = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "dibujo"+hora;


                        out = new FileOutputStream(ruta);
                        // Comprimimos el bitmap en el Stream
                        signatureView.getSignatureBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intentAtras = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("rutaImg", ruta);
                            intentAtras.putExtras(bundle);
                            setResult(Activity.RESULT_OK, intentAtras);

                            finish();
                            layoutProgresoImagen.setVisibility(View.GONE);// ocultamos el progreso
                        }
                    });

                }
            }).start();
        }

    }    public void refres(View view) {
        signatureView.clearCanvas();

    }

    private boolean verificarPermisos(){
        ArrayList<String> permisosFaltantes = new ArrayList<>();

        boolean permisoEscrituraSD = ( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);

        boolean permisoLecturaSD = ( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);


        if(!permisoEscrituraSD){
            permisosFaltantes.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(!permisoLecturaSD){
            permisosFaltantes.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if(permisosFaltantes.size() > 0){
            String[] permisos = new String[permisosFaltantes.size()];
            permisos = permisosFaltantes.toArray(permisos);

            ActivityCompat.requestPermissions(this, permisos, COD_PERMISOS);

            return false;
        }else{
            return true;
        }
    }
}
