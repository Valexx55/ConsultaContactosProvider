package edu.val.consultacontactosprovider;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pedirPermisosAccesoAContactos ();
    }

    //CUANDO UNA ACTIVIDAD PIDE PERMISOS PELIGROSOS, EL MÉTODO onRequestPermissionsResult
    //se convierte en el onCreate ();
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("ETIQUETA_LOG", "vuelta de pedir permisos reqcode = " + requestCode);

        if (grantResults[1] == PackageManager.PERMISSION_GRANTED)//0
        {
            Log.d("ETIQUETA_LOG", "Me ha dado permiso para enviar SMS " );
        } else {
            Log.d("ETIQUETA_LOG", "Me ha denegado permiso el usuario para ENVIAR SMS ");
            Toast.makeText(this, "NO SE PUEDE ENVIAR SMS, PERMISO DENEGADO", Toast.LENGTH_LONG).show();
            //finish();
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)//0
        {
            Log.d("ETIQUETA_LOG", "Me ha dado permiso el usuario para leer los contactos " );
            mostrarTelefonos();

        } else {
            Log.d("ETIQUETA_LOG", "Me ha denegado permiso el usuario para leer los contactos ");
            Toast.makeText(this, "NO SE PUEDE LEER LOS CONTACTOS, PERMISO DENEGADO", Toast.LENGTH_LONG).show();
            //finish();
        }



    }

    private void mostrarTelefonos ()
    {
        Log.d("ETIQUETA_LOG", "Mostrando teléfonos ...");
        Uri uri_contactos = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Log.d("ETIQUETA_LOG", "uri_contactos = " + uri_contactos);

        ContentResolver contentResolver = getContentResolver();//la clase que me permite leer de un content provider

        //seleccionamos TODOS los contactos
        Cursor cursor = contentResolver.query(uri_contactos, null, null, null, null);

        if (cursor.moveToFirst())
        {
            Log.d("ETIQUETA_LOG", "AL menos tengo un resultado ");

            int numcolumna_telf = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);//saber que cólumna es la que tiene los teléfonos
            String num_telf_actual = null;
            int i = 1;
            do {
                num_telf_actual = cursor.getString(numcolumna_telf);//accedo al valor del telefono
                Log.d("ETIQUETA_LOG", "NUM TELF" + i +" = " + num_telf_actual);
                i++;
            }while (cursor.moveToNext());

            cursor.close();
        }

    }

    private void pedirPermisosAccesoAContactos ()
    {
        String[] array_permisos = {READ_CONTACTS, SEND_SMS};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //si tengo permiso concedido, no lo pido
            //si no, lo pido
            //TRUCO PRÁCTICO: PEDIRLO SIEMPRE: Si ya está concedido, Android no me lo pide
            Log.d("ETIQUETA_LOG", "PIDO PERMISOS PELIGROSOS pues estoy versión >= 6 ");
            requestPermissions(array_permisos, 999);

        }

    }
}