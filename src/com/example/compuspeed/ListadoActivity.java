package com.example.compuspeed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListadoActivity extends Activity {
	protected PowerManager.WakeLock wakelock;
    private ListView lvRecorridos;
    private static Estadistica dataSource;
    
    ListView listView;
    List<Recorrido> rowItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ********** pantalla completa **************************
		//esto quita el título de la activity en la parte superior
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//y esto para pantalla completa (oculta incluso la barra de estado)
		this.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN
				);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.listadorecorrido);
		

		rowItems = new ArrayList<Recorrido>();
		List<Recorrido> listaRec = Estadistica.getAllRecorrido(getApplicationContext());
		for(int i = 0; i < listaRec.size(); i++)
		{		
			Recorrido item = new Recorrido(
					listaRec.get(i).codigo, 
					listaRec.get(i).fecha, 
					listaRec.get(i).direccionInicio, 
					listaRec.get(i).direccionTermino, 
					listaRec.get(i).velocidadMaxima, 
					listaRec.get(i).distanciaRecorrida, 
					listaRec.get(i).tiempoTranscurrido,
					listaRec.get(i).horaInicio,
					listaRec.get(i).horaTermino
					);
			rowItems.add(item);
		}
 
        listView = (ListView) findViewById(R.id.lvRecorridosMostrar);
        CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                R.layout.list_item, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	   public void onItemClick(AdapterView<?> listView, View view, 
        	     int position, long id) {
        		
        		Estadistica.deletOneRecord(getApplicationContext(), rowItems.get(position));
        		refrescarLista();
        	   // Get the cursor, positioned to the corresponding row in the result set
        		Toast toast = Toast.makeText(getApplicationContext(),
        	            "Item " + (position + 1) + ": " + rowItems.get(position).fecha + " " + rowItems.get(position).direccionInicio,
        	            Toast.LENGTH_SHORT);
        	        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        	        toast.show();
        	   }
		});
        //listView.setOnItemClickListener(this);
        //listView.setOnClickListener(this);
		
		
		final PowerManager pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
		
		this.wakelock=pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "etiqueta");
        wakelock.acquire();
	
	}
	private void refrescarLista() {
		rowItems = new ArrayList<Recorrido>();
		List<Recorrido> listaRec = Estadistica.getAllRecorrido(getApplicationContext());
		for(int i = 0; i < listaRec.size(); i++)
		{		
			Recorrido item = new Recorrido(
					listaRec.get(i).codigo, 
					listaRec.get(i).fecha, 
					listaRec.get(i).direccionInicio, 
					listaRec.get(i).direccionTermino, 
					//String.format("%.2f", listaRec.get(i).velocidadMaxima),
					listaRec.get(i).velocidadMaxima, 
					listaRec.get(i).distanciaRecorrida, 
					listaRec.get(i).tiempoTranscurrido,
					listaRec.get(i).horaInicio,
					listaRec.get(i).horaTermino);
			rowItems.add(item);
		}
 
        listView = (ListView) findViewById(R.id.lvRecorridosMostrar);
        CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                R.layout.list_item, rowItems);
        listView.setAdapter(adapter);
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast toast = Toast.makeText(getApplicationContext(),
            "Item " + (position + 1) + ": " + rowItems.get(position),
            Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

	//no apagado de la pantalla ***********
	@Override
	protected void onDestroy(){
        super.onDestroy();
        this.wakelock.release();
        
    }
	protected void onResume(){
        super.onResume();
        wakelock.acquire();
        
    }
	public void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        this.wakelock.release();
        
    }
	//*************************************
	 @Override
	    protected void onPause() {
	        // TODO Auto-generated method stub
	        //dataSource.close();
	        super.onPause();
	    }
	 
	 public static String getHoraActual() {
		  Date ahora = new Date();
		  SimpleDateFormat formateador = new SimpleDateFormat("hh:mm:ss");
		  return formateador.format(ahora);
		 }
	
	
}
