package com.maxescobar.basededatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
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

        //Lo de siempre
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

    //Metodo para la consulta de un articulo
    public void Buscar(View vista){
        //Como arriba utilizamos el objeto que hemos creado con el fin de utilizar los metodos de la base de datos de SQLiteOpenHelper
        AdminiSQLite admin = new AdminiSQLite(this,"administracion",null,1);
        //Vamos traemos como objeto SQLiteDatabase para que podamos trabajar con sus metodos para facilitarnos la vida
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        //extraemos el codigo que vamos a buscar
        String codigo = etCodigo.getText().toString();

        if (!codigo.isEmpty()) {
            //Con la variable Cursor fingimos ser una operacion DDL de base de datos que nos permite utilizar la sentencia SELECT en la base de datos
            Cursor fila = basededatos.rawQuery
                    ("SELECT descripcion, precio FROM articulo WHERE codigo =" + codigo, null);
            //Si lo encuentra entonces... moveToFirst() disparar un true
            if (fila.moveToFirst()){
                //Bueno como se base de datos no importa pero si me golpeo la cabeza...
                //Es un select... este nos va a traer 3 columnas segun como lo hayamos pedido en el sql pedimos primero 'SELECT descripcion, precio'
                //entonces como en Java este nos va a traer una tabla resulSet en ese mapeo de resultados en el primero estara la descripcion y en el segundo el precio
                etNombre.setText(fila.getString(0));
                etPrecio.setText(fila.getString(1));

                //Cerrar la base de datos
                basededatos.close();
                //Mensaje de que encontro el resultado de la busqueda
                Toast.makeText(this,"Se encontro el articulo",Toast.LENGTH_SHORT).show();
            }else{
                //Mensaje de que no encontro ni nada
                Toast.makeText(this,"No se ha encontrado un articulo que responde a ese codigo", Toast.LENGTH_SHORT).show();
                //Cerramos la base de datos
                basededatos.close();
            }
        }else{
            //si no esta compleo el codigo... pues que lo coloque
            Toast.makeText(this,"Debes escribir un codigo que desee buscar", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para Eliminar un producto
    public void Eliminar(View vista) {
        //Como arriba creamos un objeto de tipo AdminiSQLite que hereda de SQLiteOpenHelper que implementa metodos de base dedatos
        AdminiSQLite admin = new AdminiSQLite(this, "administracion", null, 1);
        //Aqui implementamos un objeto BasedeDatos(Objeto que actua como una base de datos simple) con el que implementaremos los metodos de la sqlite
        SQLiteDatabase basededatos = admin.getWritableDatabase();

        //Extraemos el codigo que necesitamos
        String codigo = etCodigo.getText().toString();
        //estructura condicional para controlar si el usuario ingresa codigo o no
        if (!codigo.isEmpty()){
            //En este caso este metodo devuelve un entero representando la cantidad de elementos borrados como JDBC.
            int cantidad = basededatos.delete("articulos","codigo=" + codigo,null);
            //Cerramos la base de datos
            basededatos.close();
            //limpiamos os campos
            etPrecio.setText("");
            etNombre.setText("");
            etCodigo.setText("");
            //Estructura condicional por si se ha elimnado el articulo o no
            if (cantidad == 0){
                //Mensaje para el usuario por si el objeto se elimino como corresponde
                Toast.makeText(this,"El elemento ha sido eliminado correctamente",Toast.LENGTH_SHORT).show();
            }else{
                //Mensaje para el usuario por si el objeto no se ha eliminado por alguna razon
                Toast.makeText(this,"El elemento no ha podido ser eliminado por que no existe",Toast.LENGTH_SHORT).show();
            }
        }else{
            //si no esta completo el codigo... pues que lo coloque
            Toast.makeText(this,"Debes escribir un codigo que desee eliminar", Toast.LENGTH_SHORT).show();
        }
    }
}