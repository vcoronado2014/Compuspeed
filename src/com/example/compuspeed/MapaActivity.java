package com.example.compuspeed;


import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MapaActivity extends Activity {
	protected PowerManager.WakeLock wakelock;
	private GoogleMap mMap;
	private Timer myTimerVelocidadMapa;
	private Timer myTimerBateriaMapa;
	private Timer myTimerActualizarMapa;
	Speedometer speedometerMapa;
	private ImageButton botonCerrar;
	private TextView lblNivelBateriaMapa;
	Configuracion conf;
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
		
		setContentView(R.layout.mapacompleto);
		//bateria
        int nivelBat = SpeedActivity.NIVEL_BATERIA_INT;
        asignaImagenBateria(nivelBat);
		
		speedometerMapa = (Speedometer) findViewById(R.id.speedometerMapaCompleto);
		conf = new Configuracion(this);
        if(conf.getUsaEscala() != null)
        {
        	if (conf.getUsaEscala().contains("true"))
        		speedometerMapa._usaBarra(true);
        	else
        		speedometerMapa._usaBarra(false);
        }
		
		
		botonCerrar = (ImageButton)findViewById(R.id.imageButtonCerrar);
		botonCerrar.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast toast1 =
			            Toast.makeText(getApplicationContext(),
			                    "Cerrando Mapa", Toast.LENGTH_SHORT);
			 
			        toast1.show();
				finish();
				//android.os.Process.killProcess(android.os.Process.myPid());
			
			}});
		
		//contenido acá
 		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragment1)).getMap();
  		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
  		
  		
  		mMap.setMyLocationEnabled(true);
  		mMap.setTrafficEnabled(true);
  		//mMap.animateCamera(CameraUpdateFactory.zoomBy(3));
  		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(SpeedActivity.ULTIMA_LATITUD, SpeedActivity.ULTIMA_LONGITUD));
  		CameraUpdate zoom=CameraUpdateFactory.zoomTo(18);
  		mMap.moveCamera(center);
  		mMap.animateCamera(zoom);

  		Log.i("LocAndroid", "actualizado mapa completo google");
  		myTimerVelocidadMapa = new Timer();
		myTimerVelocidadMapa.schedule(new TimerTask() {			
			@Override
			public void run() {
				TimerMethodVelocidadMapa();
			}
			
		}, 0, 3000);
  		myTimerBateriaMapa = new Timer();
  		myTimerBateriaMapa.schedule(new TimerTask() {			
			@Override
			public void run() {
				TimerMethodBateriaMapa();
			}
			
		}, 0, 60000);
  		myTimerActualizarMapa = new Timer();
  		myTimerActualizarMapa.schedule(new TimerTask() {			
			@Override
			public void run() {
				TimerMethodActualizarMapa();
			}
			
		}, 0, 20000);
		
		final PowerManager pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
		
		this.wakelock=pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "etiqueta");
        wakelock.acquire();
	
	}
	
	private void TimerMethodActualizarMapa()
	{
		this.runOnUiThread(Timer_TickActualizarMapa);
		
	}
	private void TimerMethodVelocidadMapa()
	{
		this.runOnUiThread(Timer_Tick);
		
	}
	private void TimerMethodBateriaMapa()
	{

		this.runOnUiThread(Timer_TickBateria);
		
	}
	private Runnable Timer_TickActualizarMapa = new Runnable() {
		public void run() {
	        actualizarMapa();
		}
	};
	private void actualizarMapa()
	{
 		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
  		
  		
  		mMap.setMyLocationEnabled(true);
  		mMap.setTrafficEnabled(true);
  		//mMap.animateCamera(CameraUpdateFactory.zoomBy(3));
  		double ultimaLat = SpeedActivity.ULTIMA_LATITUD;
  		double ultimaLon = SpeedActivity.ULTIMA_LONGITUD;
  		if (ultimaLat != 0.0 && ultimaLon != 0.0)
  		{
	  		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(ultimaLat, ultimaLon));
	  		CameraUpdate zoom=CameraUpdateFactory.zoomTo(16);
	  		mMap.moveCamera(center);
	  		mMap.animateCamera(zoom);
	  		Log.i("LocAndroid", "actualizado mapa completo google");
  		}
	}
	private Runnable Timer_TickBateria = new Runnable() {
		public void run() {
	        int nivelBat = SpeedActivity.NIVEL_BATERIA_INT;
	        asignaImagenBateria(nivelBat);
		}
	};
	private Runnable Timer_Tick = new Runnable() {
		public void run() {
		
		//This method runs in the same thread as the UI.    	       
		
		//Do something to the UI thread here
			
		//Log.i("LocAndroid", "timer run");
		float km = SpeedActivity.KMHORA_MAPA;
		speedometerMapa.onSpeedChanged(km);
		Log.i("LocAndroid", "Velocidad mapa: " + km);
		//txtNivelBateria.setText(TacometroActivity.NIVEL_BATERIA_STRING);
		//asignaImagenBateria(TacometroActivity.NIVEL_BATERIA_INT);
		}
	};
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
	public void asignaImagenBateria(int carga)
    {
    	lblNivelBateriaMapa = (TextView)findViewById(R.id.txtCargaBateriaMapa);
    	ImageView imagenBat = (ImageView)findViewById(R.id.imgBateriaPrincipalMapa);
    	int imagen = R.drawable.bateria_100;
    	if (carga >= 0 && carga < 20)
    		imagen = R.drawable.bateria_0;
    	if (carga >= 20 && carga < 25)
    		imagen = R.drawable.bateria_20;
    	if (carga >= 25 && carga < 50)
    		imagen = R.drawable.bateria_25;
    	if (carga >= 50 && carga < 60)
    		imagen = R.drawable.bateria_50;
    	if (carga >= 60 && carga < 80)
    		imagen = R.drawable.bateria_60;
    	if (carga >= 80 && carga < 90)
    		imagen = R.drawable.bateria_80;
    	if (carga >= 90)
    		imagen = R.drawable.bateria_100;
    	imagenBat.setImageResource(imagen);
    	lblNivelBateriaMapa.setText(String.valueOf(carga) + "%");
    }
	
}
