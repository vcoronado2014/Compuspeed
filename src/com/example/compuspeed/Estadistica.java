package com.example.compuspeed;

import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class Estadistica extends SQLiteOpenHelper {
	
	private static String name = "bdCompuSpeed";
	private static int version = 1;
	private static CursorFactory cursorFactory = null;
	protected static String TableEstadistica = "Estadistica";
	private static Context _context;
	
	private String sqlCreate = "CREATE TABLE " + TableEstadistica +  " (Codigo INT, Fecha VARCHAR(255), DireccionInicio VARCHAR(500), " +
			"LatitudInicio VARCHAR(255), LongitudInicio VARCHAR(255), DireccionTermino VARCHAR(500), LatitudTermino VARCHAR(255), " +
			"LongitudTermino VARCHAR(255), DistanciaRecorrida VARCHAR(255), VelocidadMaxima VARCHAR(255), VelocidadPromedio VARCHAR(255), TiempoTranscurrido VARCHAR(255), " + 
			"HoraInicio varchar(10), HoraTermino VARCHAR(10)) ";

	
	public Estadistica(Context context) {
		super(context, name, cursorFactory, version);
		// TODO Auto-generated constructor stub
		//_context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente 
        //      la opción de eliminar la tabla anterior y crearla de nuevo 
        //      vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la 
        //      tabla antigua a la nueva, por lo que este método debería 
        //      ser más elaborado.
 
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS " + TableEstadistica);
 
        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
	}
	
	public static int InsertarRegistro(ContentValues registro, Context context)
	{
		List<Recorrido> lis = getAllRecorrido(context);
		int id = 1;
		if (lis != null && lis.size() > 0)
			id = lis.size() + 1;
		
		int retorno = 0;
	    Estadistica usdbh =
	              new Estadistica(context);
	   
	    SQLiteDatabase db = usdbh.getWritableDatabase();
	    try {
	    	registro.put("codigo", String.valueOf(id));
	    	db.insert(TableEstadistica, null, registro);
	    	Toast.makeText(context, "insertado", Toast.LENGTH_SHORT).show();
	    }
	    catch(Exception ex)
	    {
	    	Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
	    }
		return retorno;
	}
	
	public static List<Recorrido> getAllRecorrido(Context context) {
		    List<Recorrido> estadisticaList = new ArrayList<Recorrido>();
		    // Select All Query
		    String selectQuery = "SELECT  * FROM " + TableEstadistica;
		    Estadistica usdbh =
		              new Estadistica(context);
		    SQLiteDatabase db = usdbh.getWritableDatabase();
		    Cursor cursor = db.rawQuery(selectQuery, null);
		 
		    // looping through all rows and adding to list
		    if (cursor.moveToFirst()) {
		        do {
		        	Recorrido rec = new Recorrido();
		        	rec.setCODIGO(cursor.getString(0));
		        	rec.setFECHA(cursor.getString(1));
		        	rec.setDIRECCION_INICIO(cursor.getString(2));
		        	rec.setLATITUD_INICIO(cursor.getString(3));
		        	rec.setLONGITUD_INICIO(cursor.getString(4));
		        	rec.setDIRECCION_TERMINO(cursor.getString(5));
		        	rec.setLATITUD_TERMINO(cursor.getString(6));
		        	rec.setLONGITUD_TERMINO(cursor.getString(7));
		        	rec.setDISTANCIA_RECORRIDA(cursor.getString(8));
		        	rec.setVELOCIDAD_MAXIMA(cursor.getString(9));
		        	rec.setVELOCIDAD_PROMEDIO(cursor.getString(10));
		        	rec.setTIEMPO_TRANSCURRIDO(cursor.getString(11));
		        	rec.setHORA_INICIO(cursor.getString(12));
		        	rec.setHORA_TERMINO(cursor.getString(13));
		        	estadisticaList.add(rec);
		        	//fecha
		        	
		        	
		        } while (cursor.moveToNext());
		    }
		 
		    // return contact list
		    return estadisticaList;
		}

	public static ArrayList<String> getAllRecorridoStr(Context context) {
		ArrayList<String> estadisticaList = new ArrayList<String>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TableEstadistica;
	    Estadistica usdbh =
	              new Estadistica(context);
	    SQLiteDatabase db = usdbh.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	Recorrido rec = new Recorrido();
	        	rec.setCODIGO(cursor.getString(0));
	        	rec.setFECHA(cursor.getString(1));
	        	rec.setDIRECCION_INICIO(cursor.getString(2));
	        	rec.setLATITUD_INICIO(cursor.getString(3));
	        	rec.setLONGITUD_INICIO(cursor.getString(4));
	        	rec.setDIRECCION_TERMINO(cursor.getString(5));
	        	rec.setLATITUD_TERMINO(cursor.getString(6));
	        	rec.setLONGITUD_TERMINO(cursor.getString(7));
	        	rec.setDISTANCIA_RECORRIDA(cursor.getString(8));
	        	rec.setVELOCIDAD_MAXIMA(cursor.getString(9));
	        	rec.setVELOCIDAD_PROMEDIO(cursor.getString(10));
	        	rec.setTIEMPO_TRANSCURRIDO(cursor.getString(11));
	        	rec.setHORA_INICIO(cursor.getString(12));
	        	rec.setHORA_TERMINO(cursor.getString(13));
	        	estadisticaList.add(rec.getCODIGO() + " - " + rec.getDIRECCION_INICIO());
	        	//fecha
	        	
	        	
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return estadisticaList;
	}

	public static int deletOneRecord(Context context, Recorrido recorrido) {
        // TODO Auto-generated method stub
		String id = recorrido.getCODIGO();
	    Estadistica usdbh =
	              new Estadistica(context);
	    SQLiteDatabase db = usdbh.getWritableDatabase();
	    db.delete(TableEstadistica, "Codigo = " + id, null);

        return 0;
    }
	
	
	//el formato para mostrar los resumenes de totales sería
	//"Fecha Hoy", "Total Kilometros Rec", "Total Velocidad Promedio", "Total Tiempo Transcurrido", "Velocidad Max Registrada"
	//"Fecha Ayer", "Fecha Semana (suma de 7 dias y en caso que sean menos la cantidad de dias)"
	//"Total Macro"
	
	
}
