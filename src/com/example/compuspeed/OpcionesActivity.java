package com.example.compuspeed;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class OpcionesActivity extends Activity {
	protected PowerManager.WakeLock wakelock;
	 private Button btnEmail;
	 private TextView lblEmail;
	 private EditText txtEmail;
	 private Configuracion conf;
	 private CheckBox chkUsaBarra;
	 private CheckBox chkUsaAnalogo;
	 private CheckBox chkMuestraLLamadasP;
	 private CheckBox chkMuestraSms;

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
		
		setContentView(R.layout.configuraciones);
		//bateria
        //Instanciamos los controles y el objeto configuración
        btnEmail = (Button) findViewById(R.id.btnEmail);
        chkUsaBarra = (CheckBox)findViewById(R.id.checkBox1);
        chkUsaAnalogo = (CheckBox)findViewById(R.id.chkUsaAnalogo);
        chkMuestraLLamadasP = (CheckBox)findViewById(R.id.chkMuestraLlamadas);
        chkMuestraSms = (CheckBox)findViewById(R.id.chkMuestraSMS);
        conf = new Configuracion(this);
       
        //Escribimos el valor del email guardado anteriormente, si existe dicho valor
        if(conf.getUsaEscala() != null)
        {
        	if (conf.getUsaEscala().contains("true"))
        		chkUsaBarra.setChecked(true);
        	else
        		chkUsaBarra.setChecked(false);
        }
        if(conf.getUsaAnalogo() != null)
        {
        	if (conf.getUsaAnalogo().contains("true"))
        		chkUsaAnalogo.setChecked(true);
        	else
        		chkUsaAnalogo.setChecked(false);
        }
        if(conf.getMuestraLLamadasPerdidas() != null)
        {
        	if (conf.getMuestraLLamadasPerdidas().contains("true"))
        		chkMuestraLLamadasP.setChecked(true);
        	else
        		chkMuestraLLamadasP.setChecked(false);
        }
        if(conf.getMuestraMensajes() != null)
        {
        	if (conf.getMuestraMensajes().contains("true"))
        		chkMuestraSms.setChecked(true);
        	else
        		chkMuestraSms.setChecked(false);
        }
        //Definimos el evento click del botón para poder guardar el nuevo email
        btnEmail.setOnClickListener(new OnClickListener() {   
        	public void onClick(View v) {  
        		if (chkUsaBarra.isChecked())
        			conf.setUsaEscala("true");
        		else
        			conf.setUsaEscala("false");
        		
        		if (chkUsaAnalogo.isChecked())
        			conf.setUsaAnalogo("true");
        		else
        			conf.setUsaAnalogo("false");
        		
        		if (chkMuestraLLamadasP.isChecked())
        			conf.setMuestraLlamadasPerdidas("true");
        		else
        			conf.setMuestraLlamadasPerdidas("false");
        		
        		if (chkMuestraSms.isChecked())
        			conf.setMuestraMensajes("true");
        		else
        			conf.setMuestraMensajes("false");
        			
		   		//if (txtEmail.getText().toString().trim() == "true")
        		if (chkUsaBarra.isChecked())
		   		{
		   			SpeedActivity.USA_ESCALA = true;
		   			//SpeedActivity.speedometer._usaBarra(true);
		   		}
		   		else
		   		{
		   			SpeedActivity.USA_ESCALA = false;
		   			//SpeedActivity.speedometer._usaBarra(false);		   			
		   		}
        		if (chkUsaAnalogo.isChecked())
		   		{
		   			SpeedActivity.USA_ANALOGO = true;
		   		}
		   		else
		   		{
		   			SpeedActivity.USA_ANALOGO = false;
   			
		   		}
        		if (chkMuestraLLamadasP.isChecked())
		   		{
		   			SpeedActivity.MUESTRA_LLAMADAS_PERDIDAS = true;
		   		}
		   		else
		   		{
		   			SpeedActivity.MUESTRA_LLAMADAS_PERDIDAS = false;
   			
		   		}
        		if (chkMuestraSms.isChecked())
		   		{
		   			SpeedActivity.MUESTRA_MENSAJES_NOLEIDOS = true;
		   		}
		   		else
		   		{
		   			SpeedActivity.MUESTRA_MENSAJES_NOLEIDOS = false;
   			
		   		}
        		Toast.makeText(getApplicationContext(), "Configuración guardada", Toast.LENGTH_SHORT).show();
        		Toast.makeText(getApplicationContext(), "Espere a que vuelva la señal de GPS para que los cambios surtan efecto", Toast.LENGTH_LONG).show();
        		//SpeedActivity.cha
        		
        	}
        });
		
		final PowerManager pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
		
		this.wakelock=pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "etiqueta");
        wakelock.acquire();
	
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
	
	
	
}
