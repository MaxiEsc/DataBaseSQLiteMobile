package com.maxescobar.basededatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etNombre, etCodigo, etPrecio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre = (EditText) findViewById(R.id.ettNombre);
        etCodigo = (EditText) findViewById(R.id.etnCodigo);
        etPrecio = (EditText) findViewById(R.id.etnPrecio);

    }

    //Metodo para insertar los productos
    public void Insertar(View vista){
        //Con es ta linea utilizamos la base de datos SQLite mediante la clase que creamos y que extendimos de SQLiteOpenHelper
        AdminiSQLite admin = new AdminiSQLite(this,"administracion", null,1);
        //Crea un objeto de tipoSQLiteDatabase para que este se escribible
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        //Lo de siempre
        String codigo = etCodigo.getText().toString();
        String descripcion = etNombre.getText().toString();
        String precio = etPrecio.getText().toString();

        if (!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){
            //Permite crear una variable que permita la carga de datos en la DB
            ContentValues registro = new ContentValues();
            //Registramos el codigo
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            //Guardamos los regiistros en la tabla articulos
            BaseDeDatos.insert("articulos", null, registro);

            //Cerrar la base de datos
            BaseDeDatos.close();
            //Limpiamos los campos
            etPrecio.setText("");
            etNombre.setText("");
            etCodigo.setText("");

            //Avisar al usuario de que todo se realizo correctamente
            Toast.makeText(this,"Datos registrados correctamente", Toast.LENGTH_SHORT).show();
        }else {
            //Avisar al usuario de este caso
            Toast.makeText(this,"Debes completar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}