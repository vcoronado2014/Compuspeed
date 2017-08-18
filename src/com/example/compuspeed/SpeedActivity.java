package com.example.compuspeed;



import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



import android.R.drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.CallLog.Calls;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DigitalClock;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "ShowToast", "Wakelock", "SetJavaScriptEnabled", "NewApi" })
public class SpeedActivity extends Activity {

	private static final String SMS_INBOX_CONTENT_URI = "content://sms";
	protected PowerManager.WakeLock wakelock;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private String provider;
	public static float mayorVel = 0;
	public static String KMHORA_STRING = "0.0";
	public static Location lugarInicio;
	public static Location lugarTermino;
	public static String DireccionInicio = "";
	public static String DireccionTermino = "";
	
	public static float distanciaRecorrida;
	//static Speedometer speedometer;
	private Chronometer crono1;
	private TextView lblDireccion;
	private TextView lblDireccion2;
	public static int NIVEL_BATERIA_INT = 0;
	private Timer myTimer;
	private Timer myTimerDireccion;
	private Timer myTimerLlamadas;
	private Timer myTimerSms;
	private Timer myTimerLateral;
	private Timer myTimerPrueba;
	private TextView lblNivelBateria;
	public static double ULTIMA_LATITUD=0;
	public static double ULTIMA_LONGITUD=0;
	//SE EJECUTARA EN UN TIMER CADA 2 MINUTOS
	//AL MOMENTO QUE SE ACTUALIZA LA BATERIA
	//ALMACENAREMOS ESTOS VALORES PARA IR
	//SACANDO LA DISTANCIA RECORRIDA.
	public static double ANTERIOR_LATITUD=0;
	public static double ANTERIOR_LONGITUD=0;
	private TextView lblFecha;
	//manejo de hilos
	final Handler mHandler = new Handler();
	//private MiTareaAsincrona tarea1;
	public Geocoder geocoder;
	public static int CONTADOR_ERRORES_GEOCODER = 0;
	public static int MILISEGUNDOS_ESPERA = 10000;
	public Thread t;
	
	public ImageView imagenTiempo;
	public ImageView imagenTiempoManana;
	public static WebView mWebViewManana;
	private static WebView mWebView;
	public static String HoraNoche = "19";
	public static int CONTADOR_LATITUD = 0;
	public static double SUMADOR_DISTANCIA = 0;
	public static double ALTITUD_ACTUAL = 0;
	public static double VELOCIDAD_PROMEDIO_ACTUAL = 0;
	public static float PRECISION_ACTUAL = 0;
	public static WebView mWebViewLateral;
	public static String WOEID ="349859";
	public static String PAIS_MOSTRAR = "Chile";
	public static String DIRECCION_MOSTRAR = "Santiago";
	public static float KMHORA_MAPA = 0;
	private ImageButton imageButtonMapa;
	private ImageButton imageButtonFono;
	public static float HORAS_ACTUALES = 0;
	private TextView txtVelocidadPromedioMostrar;
	public static String HORA_INICIO = "";
	public static String HORA_TERMINO = "";
	//para menjar la ultima locación
	public static Location ultimaLocation;
	private TextView txtTextoProvider;
	private ImageView imgGpsMostrar;
	public static boolean USA_ESCALA = true;
	Configuracion conf;
	private static Context _CONTEXT;
	public static double TAMANO_PANTALLA = 3.0;
	TextView txtAct;
	public static WebView mWebViewVel;
	public static boolean USA_ANALOGO = true;
	public static boolean MUESTRA_LLAMADAS_PERDIDAS = false;
	public static boolean MUESTRA_MENSAJES_NOLEIDOS = false;
	ImageView imagenBat;
	public TextView txtvelDig;
	String colName;
	
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
		
		//setContentView(R.layout.activity_speed);
		setContentView(R.layout.activity_speed_2);
		conf = new Configuracion(this);
        if(conf.getUsaAnalogo() != null)
        {
        	if (conf.getUsaAnalogo().contains("true"))
        		USA_ANALOGO = true;
        	else
        		USA_ANALOGO = false;
        }
        if(conf.getMuestraLLamadasPerdidas() != null)
        {
        	if (conf.getMuestraLLamadasPerdidas().contains("rue"))
        		MUESTRA_LLAMADAS_PERDIDAS = true;
        	else
        		MUESTRA_LLAMADAS_PERDIDAS = false;
        }
        if(conf.getMuestraMensajes() != null)
        {
        	if (conf.getMuestraMensajes().contains("rue"))
        		MUESTRA_MENSAJES_NOLEIDOS = true;
        	else
        		MUESTRA_MENSAJES_NOLEIDOS = false;
        }
		//vel digital
        txtvelDig = (TextView)findViewById(R.id.txtMostrarVelDig);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "digital-7.ttf");
        txtvelDig.setTypeface(myTypeface);
        
		//cronometro
		crono1 = (Chronometer)findViewById(R.id.chronometer1);
		//fecha
		lblFecha = (TextView)findViewById(R.id.txtFecha);
		lblFecha.setText(ObtenerFecha());
		//promedio
		txtVelocidadPromedioMostrar = (TextView)findViewById(R.id.txtPromedioActual);
		//reloj digital es un control
		//velocidad webview2
		if (USA_ANALOGO)
		{
			mWebViewVel = (WebView)this.findViewById(R.id.webView2);
			txtvelDig.setVisibility(View.INVISIBLE);
			
		}
		else
		{
			mWebViewVel = (WebView)this.findViewById(R.id.webView2);
			mWebViewVel.setVisibility(View.INVISIBLE);
		}
		CargarVelocimetro();
		changeText("1");
		//web view lateral
        mWebViewLateral = (WebView)findViewById(R.id.webViewL);
        mWebViewLateral.getSettings().setJavaScriptEnabled(true);
        mWebViewLateral.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebViewLateral.getSettings().setGeolocationEnabled(true);
        mWebViewLateral.getSettings().setLoadsImagesAutomatically(true);
        mWebViewLateral.getSettings().setBlockNetworkImage(true);
        mWebViewLateral.getSettings().setAllowFileAccess(true);
        mWebViewLateral.getSettings().setAllowFileAccess(true);
        ////mWebViewLateral.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebViewLateral.loadUrl("file:///android_asset/Lateral.html");	
		//webview1 tiempo
        ObtenerResumenHoy();
		//igual webview1
        //ActualizarAfuera();
        //bateria
        lblNivelBateria = (TextView)findViewById(R.id.txtCargaBateria);
        imagenBat = (ImageView)findViewById(R.id.imgBateriaPrincipal);
        int nivelBat = cargaBateria();
        asignaImagenBateria(nivelBat);
        
        //
        //boton mapa
        imageButtonMapa = (ImageButton)findViewById(R.id.imageButtonGmaps);
        //boton fono
        imageButtonFono = (ImageButton)findViewById(R.id.imageButtonFono);
        //image gps
        imgGpsMostrar = (ImageView)findViewById(R.id.imgGps);
        //status
        txtTextoProvider = (TextView)findViewById(R.id.txtStatus);
        //actualizado
        txtAct = (TextView)findViewById(R.id.txtTActualizado);
        //mostrar direccion
		lblDireccion = (TextView)findViewById(R.id.lblVelocidad);
		//lblDireccion2 = (TextView)findViewById(R.id.lblDireccion2);
        
        
		//DisplayMetrics metrics = new DisplayMetrics();
		//getWindowManager().getDefaultDisplay().getMetrics(metrics);
		//aca obtenemos el tamaño de la pantalla
		_CONTEXT = getApplicationContext();
		Log.v("mylog","tamano :" + String.valueOf(tabletSize()));
		
		
		imageButtonMapa.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast toast1 =
			            Toast.makeText(getApplicationContext(),
			                    "Abriendo Mapa", Toast.LENGTH_SHORT);
			 
			        toast1.show();
			  		Intent s = new Intent(getBaseContext(), MapaActivity.class );
			        startActivity(s);
			
			}});
		//imageButtonFono = (ImageButton)findViewById(R.id.imageButtonFono);
		imageButtonFono.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast toast1 =
			            Toast.makeText(getApplicationContext(),
			                    "Abriendo Fono", Toast.LENGTH_SHORT);
			 
			        toast1.show();
			        //EditText num=(EditText)findViewById(R.id.EditText01); 
			        String number = "tel:";
			        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(number)); 
			        startActivity(callIntent);

			
			}});
		//txtDistanciaMostrar = (TextView)findViewById(R.id.txtDistanciaVista);

		//ActualizarAfuera();

		//speedometer = (Speedometer) findViewById(R.id.speedometerMapa);
		//conf = new Configuracion(this);
		TAMANO_PANTALLA = tabletSize();

        actualizarPosicion();
        //****************************
		
        //ActualizarAfuera();
  		myTimer = new Timer();
		myTimer.schedule(new TimerTask() {			
			@Override
			public void run() {
				TimerMethod();
			}
			
		}, 0, 63000);
		
  		myTimerDireccion = new Timer();
		myTimerDireccion.schedule(new TimerTask() {			
			@Override
			public void run() {
				TimerMethodDireccion();
			}
			
		}, 0, 26000);
		
  		myTimerLateral = new Timer();
  		myTimerLateral.schedule(new TimerTask() {			
			@Override
			public void run() {
				TimerMethodLateral();
			}
			
		}, 0, 13000);
  		
  		
  		myTimerLlamadas = new Timer();
		myTimerLlamadas.schedule(new TimerTask() {			
			@Override
			public void run() {
				TimerMethodLlamadas();
			}
			
		}, 0, 26000);
		
  		myTimerSms = new Timer();
		myTimerSms.schedule(new TimerTask() {			
			@Override
			public void run() {
				TimerMethodSms();
			}
			
		}, 0, 20000);
		
  		//timer para pruebas
  		//myTimerPrueba = new Timer();
  		//myTimerPrueba.schedule(new TimerTask() {			
			//@Override
			//public void run() {
				//TimerMethodPrueba();
			//}
			
		//}, 0, 2000);
  		
  		//String pp = "01:00";
  		//RetornaHoras(pp);
  		
		HORA_INICIO = getHoraActual();
		
		final PowerManager pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
		
		this.wakelock=pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "etiqueta");
        wakelock.acquire();
		
		
	}
	public static double tabletSize() {

	     double size = 0;
	        try {

	            // Compute screen size

	            DisplayMetrics dm = _CONTEXT.getResources().getDisplayMetrics();

	            float screenWidth  = dm.widthPixels / dm.xdpi;

	            float screenHeight = dm.heightPixels / dm.ydpi;

	            size = Math.sqrt(Math.pow(screenWidth, 2) +

	                                 Math.pow(screenHeight, 2));

	        } catch(Throwable t) {

	        }

	        return size;

	    }
	
	private static String ObtenerFecha()
	{
		String fechaRetorno = "";
		Date c = new Date();
		SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
		fechaRetorno = df1.format(c.getTime());
		return fechaRetorno;
	}  
	private void TimerMethodLlamadas()
	{
		this.runOnUiThread(Timer_TickLlamadas);
		
	}
	private void TimerMethodSms()
	{
		this.runOnUiThread(Timer_TickSms);
		
	}
	private void TimerMethodLateral()
	{
		this.runOnUiThread(Timer_TickLateral);
		
	}
	private void TimerMethodPrueba()
	{
		this.runOnUiThread(Timer_TickPrueba);
		
	}
	private float ExtraccionTiempo(String pedazoTimpo)
	{
		String parte1 = "";
		String parte2 = "";
		int intParte1 = 0;
		int intParte2 = 0;
		int crearMinutos = 0;
		if (pedazoTimpo.length() == 1)
		{
			parte1 = pedazoTimpo.substring(0, 1);
			intParte1 = Integer.parseInt(parte1);
			crearMinutos = intParte1;
		
		}
		if (pedazoTimpo.length() == 2)
		{
			parte1 = pedazoTimpo.substring(0, 1);
			parte2 = pedazoTimpo.substring(1, 2);	
			intParte1 = Integer.parseInt(parte1);
			intParte2 = Integer.parseInt(parte2);
			if (intParte1 > 0 && intParte2 >= 0)
			{
				String ret = String.valueOf(intParte1) + String.valueOf(intParte2);
				crearMinutos = Integer.parseInt(ret);
			}
			else if (intParte1 == 0 && intParte2 > 0)
			{
				crearMinutos = intParte2;
			}
			else
				crearMinutos = 0;
		}

		return crearMinutos;
	}
	private float RetornaHoras(String tiempo)
	{
		float retorno = 0;
		int horas = 0;
		int minutos = 0;
		int segundos = 0;
		//el tiempo viene  en el siguiente formato
		//si han pasado solo segundos 00:10 ej 10 segundos
		//si han pasado minutos 02:10 ej. 2 minutos 10 seg.
		//si han pasado horas 01:10:10 ej. 1 hora, 10 min, 10 seg
		String [] pedazos = tiempo.split(":");
		
		
		if (pedazos.length == 3)
		{
			//tratamiento de los minutos
			float suma = 0;
			//tratamiento de horas
			float horasInt = ExtraccionTiempo(pedazos[0]);
			
			float minutosInt = ExtraccionTiempo(pedazos[1]);
			//tratamiento de los segundos
			float segundosInt = ExtraccionTiempo(pedazos[2]);
			
			suma = (horasInt * 3600) + (minutosInt * 60) + segundosInt;
			

			retorno = suma;
		}
		if (pedazos.length == 2)
		{
			//tratamiento de los minutos
			float suma = 0;
			float minutosInt = ExtraccionTiempo(pedazos[0]);
			//tratamiento de los segundos
			float segundosInt = ExtraccionTiempo(pedazos[1]);
			
			suma = (minutosInt * 60) + segundosInt;

			retorno = suma;
		}
		if (retorno > 0)
			retorno = retorno / 3600;
		return retorno;
	}
	private Runnable Timer_TickLateral = new Runnable() {
		public void run() {
			DecimalFormat formateador = new DecimalFormat("###.#");
		//carga altitud, velocidad maxima, velocidad promedio,satellites
			if (DireccionInicio != "")
			{
				if (ANTERIOR_LATITUD != 0.0 && ANTERIOR_LONGITUD != 0.0)
					SumarDistancia();
			}
			float mayorVelocidad = mayorVel;
			double altitud = ALTITUD_ACTUAL;
			double distancia = SUMADOR_DISTANCIA / 1000;
			float precision = PRECISION_ACTUAL;
			float horasTranscurridas = RetornaHoras(crono1.getText().toString());
			HORAS_ACTUALES = horasTranscurridas;
			double promedio = 0;
			if (distancia > 0 && horasTranscurridas > 0)
			{
				promedio = distancia / horasTranscurridas;
				VELOCIDAD_PROMEDIO_ACTUAL = promedio;
				txtVelocidadPromedioMostrar = (TextView)findViewById(R.id.txtPromedioActual);
				txtVelocidadPromedioMostrar.setText(formateador.format(promedio));
			}
			//transformar el crono1 en horas para calcular la velocidad promedio
			//SimpleDateFormat formatoFecha = new SimpleDateFormat ("HH:mm:ss");

			//float hor = RetornaHoras(crono1.getText().toString());
			try {
				changeTextLateral(precision, altitud, distancia, mayorVelocidad);
				//changeText("18.9");
			}
			catch(Exception e)
			{
				
			}
			
			//changeTextLateral(precision1, altitud1, kmsRec, mayorVelocidad)
		
		}
	};
	
	private void TimerMethod()
	{
		this.runOnUiThread(Timer_Tick);
		
	}


	private void SumarDistancia()
	{
		try {
			double ultLat = ULTIMA_LATITUD;
			double ultLon = ULTIMA_LONGITUD;
			double antLat = ANTERIOR_LATITUD;
			double antLon = ANTERIOR_LONGITUD;
			Location locationAnt = new Location("ini");
			Location locationUlt = new Location("ter");
			locationAnt.setLatitude(antLat);
			locationAnt.setLongitude(antLon);
			locationUlt.setLatitude(ultLat);
			locationUlt.setLongitude(ultLon);
			if (ULTIMA_LATITUD != ANTERIOR_LATITUD && ULTIMA_LONGITUD != ANTERIOR_LONGITUD)
			{
				//hay que revisar esto
				//Log.v("mylog","distancia rec :" + locationAnt.distanceTo(locationUlt) );
				if (locationAnt.distanceTo(locationUlt) > 5)
				{
					//Log.v("mylog","ultima lat :" + ULTIMA_LATITUD + "|" + ANTERIOR_LATITUD );
					SUMADOR_DISTANCIA = SUMADOR_DISTANCIA + locationAnt.distanceTo(locationUlt);
					//Toast.makeText(getApplicationContext(), 
	                    //"Distancia sumada" + formateador.format(SUMADOR_DISTANCIA), 
	                    //Toast.LENGTH_SHORT).show(); 	
				}
			//txtDistanciaMostrar.setText(formateador.format(SUMADOR_DISTANCIA));
			}
			//ahora volvemos a setear los valores	
			if (ULTIMA_LATITUD != ANTERIOR_LATITUD && ULTIMA_LONGITUD != ANTERIOR_LONGITUD)
			{
				ANTERIOR_LATITUD = ULTIMA_LATITUD;
				ANTERIOR_LONGITUD = ULTIMA_LONGITUD;
			}
		}
		catch(Exception e)
		{
			Toast toast2 = Toast.makeText(getApplicationContext(),
                    "Error al calcular la distancia", Toast.LENGTH_SHORT);
			toast2.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
			toast2.show();
		}
	}

	private Runnable Timer_Tick = new Runnable() {
		public void run() {
			int nivelBat = cargaBateria();
			asignaImagenBateria(nivelBat);
			ActualizarAfuera();
		}
	};
	
	private Runnable Timer_TickLlamadas = new Runnable() {
		public void run() {
			if (MUESTRA_LLAMADAS_PERDIDAS)
			{
				VerificaLLamadas();
			}
		}
	};
	private Runnable Timer_TickSms = new Runnable() {
		public void run() {
			if (MUESTRA_MENSAJES_NOLEIDOS)
			{
				//VerificaMensajes2();
				VerificarMensajes();
			}
		}
	};
	private Runnable Timer_TickPrueba = new Runnable() {
		public void run() {
			Random random = new Random();
			int valor = anyRandomIntRange(random, 1, 200);
			changeText(String.valueOf(valor) + ".987654223");
		}
	};
	public static int anyRandomIntRange(Random random, int low, int high) {
		int randomInt = random.nextInt(high) + low;
		return randomInt;
	}
	private void TimerMethodDireccion()
	{
		this.runOnUiThread(Timer_TickDireccion);
		
	}
	private Runnable Timer_TickDireccion = new Runnable() {
		public void run() {
		
		//This method runs in the same thread as the UI.    	       
		//ejecutarAccion();
		//Do something to the UI thread here
			double lat = ULTIMA_LATITUD;
			double lon = ULTIMA_LONGITUD;
			setLocation(lat, lon);
		}
	};
	private void TimerMethodDireccionNuevo()
	{
		this.runOnUiThread(Timer_TickDireccion_Nuevo);
		
	}
	private Runnable Timer_TickDireccion_Nuevo = new Runnable() {
		public void run() {
			try {
				ReverseGeocodeLookupTask task = new ReverseGeocodeLookupTask();
		        task.applicationContext = getApplicationContext();
		        task.execute();
			}
			catch(Exception ex)
			{
				
			}

		}
	};
	
	public class ReverseGeocodeLookupTask extends AsyncTask <Void, Void, String>
	{
	    private ProgressDialog dialog;
	    protected Context applicationContext;

	    @Override
	    protected void onPreExecute()
	    {
	        //this.dialog = ProgressDialog.show(applicationContext, "Please wait...contacting the tubes.",
	                //"Requesting reverse geocode lookup", true);
	    }

	    @Override
	    protected String doInBackground(Void... params)
	    {
	        String localityName = "";
			double latitud = ULTIMA_LATITUD;
			double longitud = ULTIMA_LONGITUD;

			if (latitud != 0.0 && longitud != 0.0)
			{
				try {
					geocoder =
							new Geocoder(getApplicationContext(), Locale.getDefault());
					if (geocoder.isPresent())
					{
						List<Address> list = geocoder.getFromLocation(latitud, longitud, 1);
						if (!list.isEmpty()){
							Address address = list.get(0);
							if (address.getAddressLine(0)!= null)
								localityName =  address.getAddressLine(0) + "|" + address.getAddressLine(1);
						}
					}
					
				}
				catch(IOException e)
				{
					CONTADOR_ERRORES_GEOCODER++;
					localityName = "nada|nada";
					e.printStackTrace();
				}
			}
			setLocationString(localityName);
	        return localityName;
	    }

	    @Override
	    protected void onPostExecute(String result)
	    {
	        //this.dialog.cancel();
	        //Utilities.showToast("Your Locality is: " + result, applicationContext);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.speed, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item){
	    switch (item.getItemId()){
	        case R.id.salir:
	        	
	        	new AlertDialog.Builder(this)
	            .setMessage("¿Desea Guardar antes de salir?")
	            .setCancelable(false)
	            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	    	        	HORA_TERMINO = getHoraActual();
	    	        	GuardarRegistro();
	    	        	crono1.stop();
	    	        	SUMADOR_DISTANCIA = 0;
	    	            finish();
	    	            android.os.Process.killProcess(android.os.Process.myPid());
	                }
	            })
	            .setNegativeButton("No",new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	    	        	HORA_TERMINO = getHoraActual();
	    	        	crono1.stop();
	    	        	SUMADOR_DISTANCIA = 0;
	    	            finish();
	    	            android.os.Process.killProcess(android.os.Process.myPid());
	                }
	            })
	            .show();
	            break;
	        case R.id.action_settings:
	        	Intent r = new Intent(this, OpcionesActivity.class );
	            startActivity(r);
                break;	    
	        case R.id.mapa:
	        	Intent s = new Intent(this, MapaActivity.class );
	            startActivity(s);
                break;	
	        case R.id.Guardar:
	        	new AlertDialog.Builder(this)
	            .setMessage("¿Esta seguro de guardar?")
	            .setCancelable(false)
	            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                     //CustomTabActivity.this.finish();
	    	        	HORA_TERMINO = getHoraActual();
	    	        	GuardarRegistro();
	                }
	            })
	            .setNegativeButton("No", null)
	            .show();
                break;	
	        case R.id.Listado:
	        	Intent i = new Intent(this, ListadoActivity.class );
	            startActivity(i);
                break;	
	    }
	    return false;
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
    public void onStart(){
    	super.onStart();
    	
	}
	
	public void actualizarPosicion()
    {
		Criteria req = new Criteria();
		req.setAccuracy(Criteria.ACCURACY_FINE);
		
    	locationManager = 
    		(LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	
    	//locationManager.addGpsStatusListener((Listener) this);
    	
    	//Obtenemos la última posición conocida
    	ultimaLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	
    	//Location location = 
    		//locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	 	
    	if (ultimaLocation != null)
    	{
    		setUltimaLocation(ultimaLocation.getLatitude(), ultimaLocation.getLongitude());
    	}

    	
    	//Nos registramos para recibir actualizaciones de la posición
    	locationListener = new LocationListener() {
	    	public void onLocationChanged(Location location) {
	    		if (location == null) return;
		    	muestraPosicion(location);
	    	}
	    	public void onProviderDisabled(String provider){
	    		txtTextoProvider.setText(provider.toUpperCase().toString());
	    		int img = R.drawable.gps_inactivo;
	    		imgGpsMostrar.setImageResource(img);
	    		
	    	}
	    	public void onProviderEnabled(String provider){
	    		txtTextoProvider.setText(provider.toUpperCase().toString());
	    		int img = R.drawable.gps_activo;
	    		imgGpsMostrar.setImageResource(img);
	    	}
	    	public void onStatusChanged(String provider, int status, Bundle extras){
	    		Log.i("LocAndroid", "Provider Status: " + status);
	    		//acá podemos mostrar al proveedor
	    		txtTextoProvider.setText(provider.toUpperCase().toString());
	    		//el status 0 indica GPS o Network Fuera de Servicio, 1 indica Temporalmente
	    		//fuera de servicio, 2 indica en servicio
	    		int img = 0;
	    		if (status == 0)
	    		{
	    			img = R.drawable.gps_inactivo;
	    		}
	    		if (status == 1)
	    		{
	    			img = R.drawable.gps_temporal;
	    		}
	    		if (status == 2)
	    		{
	    			img = R.drawable.gps_activo;
	    		}	    		
	    		imgGpsMostrar.setImageResource(img);
	    	}
    	};
    	
    	
    	provider = locationManager.getBestProvider(req, true);
    	
    	locationManager.requestLocationUpdates(provider, 0,0,locationListener);
    }
	public void setUltimaLocation(double latitud, double longitud)
	{
		String dirMostrar = "";
		if (latitud != 0.0 && longitud != 0.0)
		{
			
			try {
				
				geocoder =
						new Geocoder(getApplicationContext(), Locale.getDefault());
				if (Geocoder.isPresent())
				{
					List<Address> list = geocoder.getFromLocation(latitud, longitud, 2);
					if (!list.isEmpty()){
						Address address = list.get(0);
						if (address.getAddressLine(0)!= null)
						{
							dirMostrar = address.getAddressLine(0);
							//lblDireccion.setText(address.getAddressLine(0));
						}
						if (address.getAddressLine(1) != null)
						{
							dirMostrar = dirMostrar + ", " + address.getAddressLine(1);
							//lblDireccion2.setText(address.getAddressLine(1));
						}
						lblDireccion.setText(dirMostrar);
						Log.v("mylog","Obteniendo ultima locacion :" + address.getAddressLine(0) + "," +  address.getAddressLine(1));
					}
					
				}
			}
			catch(IOException e)
			{
				CONTADOR_ERRORES_GEOCODER++;
				Toast toast2 =
			            Toast.makeText(getApplicationContext(),
			                    "Error al obtener la dirección: CANT. " + String.valueOf(CONTADOR_ERRORES_GEOCODER), Toast.LENGTH_SHORT);
			 
			        toast2.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
			 
			        toast2.show();
				e.printStackTrace();
			}
		}

	}
    
	public int cargaBateria () 
    { 
        try 
        { 
           IntentFilter batIntentFilter = 
              new IntentFilter(Intent.ACTION_BATTERY_CHANGED); 
            Intent battery = 
               this.registerReceiver(null, batIntentFilter); 
            int nivelBateria = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            NIVEL_BATERIA_INT = nivelBateria;
            return nivelBateria; 
        } 
        catch (Exception e) 
        {            
           Toast.makeText(getApplicationContext(), 
                    "Error al obtener estado de la batería", 
                    Toast.LENGTH_SHORT).show(); 
           return 0; 
        }        
    }
    
	public void asignaImagenBateria(int carga)
    {
    	//lblNivelBateria = (TextView)findViewById(R.id.txtCargaBateria);
    	//imagenBat = (ImageView)findViewById(R.id.imgBateriaPrincipal);
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
    	lblNivelBateria.setText(String.valueOf(carga) + "%");
    }
	
	public void muestraPosicion(Location loc) {
	 	DecimalFormat formateador = new DecimalFormat("###.#");
	 	float KmHora = 0.0f;
	 	
	 	
	 	float constanteKm = 3.6f;
    	if(loc != null)
    	{
    		ultimaLocation = loc;
    		if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0)
    		{

    			//PASA POR ACÁ LA PRIMERA VEZ
    			if (CONTADOR_LATITUD == 0)
    			{
    				//esto se produce la primera vez
    				ANTERIOR_LATITUD = loc.getLatitude();
    				ANTERIOR_LONGITUD = loc.getLongitude();
    				DireccionInicio = entregaDireccion(loc.getLatitude(), loc.getLongitude());
    				//si no ha obtenido la direccion no aumentamos el contador hasta que lo haga
    				if (DireccionInicio != "")
    				{
    					//crono1.start();
    					CONTADOR_LATITUD++;
    				}
    			}
    			if (DireccionInicio != "")
    			{
    				ULTIMA_LATITUD = loc.getLatitude();
    				ULTIMA_LONGITUD = loc.getLongitude();
    				crono1.start();
    			}
	    		KmHora = loc.getSpeed() * constanteKm;
	    		KMHORA_MAPA = KmHora;
	    		if (KmHora >= mayorVel)
	    		{
	    			mayorVel = KmHora;
	    		}
	    		KMHORA_STRING = formateador.format(KmHora);
	    		ALTITUD_ACTUAL = loc.getAltitude();
	    		PRECISION_ACTUAL = loc.getAccuracy();

	    		changeText(String.valueOf(KmHora));

    		}
	    	else
	    	{
	    		int prueba = 0;
	    		float prec = 0;
	    		double alt = 0;
	    		changeText(String.valueOf(prueba));
	    		//changeTextLateral(prec, alt);
	    	}
    	}
    	else
    	{
    		int prueba = 0;
    		float prec = 0;
    		double alt = 0;
    		changeText(String.valueOf(prueba));
    		//changeTextLateral(prec, alt);
    	}
    }
	public String entregaComuna(double latitud, double longitud)
	{
		String retorno = "";
		try {
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		  
			List<Address> list = geocoder.getFromLocation(latitud, longitud, 1);
			if (!list.isEmpty()){
				Address address = list.get(0);
				retorno = address.getAddressLine(1); 
			}		  
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		  
		return retorno;
	}
	public String entregaDireccion(double latitud, double longitud)
	  {
			String retorno = "";
			try {
				Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			  
				List<Address> list = geocoder.getFromLocation(latitud, longitud, 1);
				if (!list.isEmpty()){
					Address address = list.get(0);
					retorno = address.getAddressLine(0) + "," + address.getAddressLine(1); 
					
					//Log.v("mylog","direccion guardada :" + address.getAddressLine(1) );
				}		  
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			  
			return retorno;
	  }
	
	public void setLocation(double latitud, double longitud)
	{
		String dirMostrar = "";
		if (latitud != 0.0 && longitud != 0.0)
		{
			
			try {
				
				geocoder =
						new Geocoder(getApplicationContext(), Locale.getDefault());
				if (Geocoder.isPresent())
				{
					List<Address> list = geocoder.getFromLocation(latitud, longitud, 2);
					if (!list.isEmpty()){
						Address address = list.get(0);
						if (address.getAddressLine(0)!= null)
						{
							dirMostrar = address.getAddressLine(0);
							//lblDireccion.setText(address.getAddressLine(0));
						}
						if (address.getAddressLine(1) != null)
						{
							dirMostrar = dirMostrar + ", " + address.getAddressLine(1);
							//lblDireccion2.setText(address.getAddressLine(1));
							PAIS_MOSTRAR = address.getAddressLine(3);
							DIRECCION_MOSTRAR = address.getAddressLine(1);
						}
						lblDireccion.setText(dirMostrar);
						//Log.v("mylog","Adrress :" + address.getAddressLine(1) );
					}
				}
			}
			catch(IOException e)
			{
				CONTADOR_ERRORES_GEOCODER++;
				Toast toast2 =
			            Toast.makeText(getApplicationContext(),
			                    "Error al obtener la dirección: CANT. " + String.valueOf(CONTADOR_ERRORES_GEOCODER), Toast.LENGTH_SHORT);
			 
			        toast2.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
			 
			        toast2.show();
				e.printStackTrace();
			}
		}

	}
    
	public void changeTextLateral(float precision1, double altitud1, double kmsRec, float mayorVelocidad ){
		  DecimalFormat formateador = new DecimalFormat("###.##");
		  String precision = formateador.format(precision1);
		  String altitud = formateador.format(altitud1);
		  String velMayor = formateador.format(mayorVelocidad);
		 
			
		  String kmts = formateador.format(kmsRec);
		  kmts = kmts.replace(',', '.').toString();
	      mWebViewLateral.loadUrl("javascript:var valor = parseInt('" + altitud + "');g1.refresh(" + velMayor + "); g3.refresh(valor); g4.refresh(" + precision + "); g5.refresh(parseFloat(" + kmts + ")); ");
	    }
	public void changeText(String someText){
		DecimalFormat formateador = new DecimalFormat("###.#");
		//"javascript:$(document).ready(function () {var gauge = new Gauge({renderTo: 'gauge',width: 260,height: 260,glow: true,units: 'Km/h',title: false,minValue: 0,maxValue: 200,majorTicks: ['0', '20', '40', '60', '80', '100', '120', '140', '160', '180', '200'],minorTicks: 1,strokeTicks: true,highlights: [{ from: 0, to: 50, color: 'rgba(0,   255, 0, .15)' },{ from: 50, to: 80, color: 'rgba(255, 255, 0, .99)' },{ from: 80, to: 100, color: 'rgba(255, 30,  0, .99)' },{ from: 100, to: 150, color: 'rgba(250, 130,  88, .99)' },{ from: 150, to: 200, color: 'rgba(255, 0,  0, .99)' }],colors: {plate: '#3A01DF',majorTicks: '#f5f5f5',minorTicks: '#ddd',title: '#fff',units: '#fff',numbers: '#fff',needle: { start: 'rgba(240, 128, 128, 1)', end: 'rgba(255, 160, 122, .9)' }}});gauge.onready = function () { gauge.setValue('" + someText + "'); };gauge.draw();});"
        Log.v("mylog","changeText is called valor:" + someText );
        //mWebViewVel.loadUrl("javascript:showGauge('" + someText + "');");
        //g1.refresh(" + velMayor + ");gauge.setValue(145);
        //"javascript:$(document).ready(function () {valor = '" + someText + "'; valorD = parseFloat(valor); valor = valorD.toFixed(2); var html = ''; html += '<div class="contenedor">'; html += '<div class="numero">'; html += valor; html += '</div>';html += '</div>';$('#test').append(html);});"
        if (USA_ANALOGO)
        {
        	mWebViewVel = (WebView)this.findViewById(R.id.webView2);
        	mWebViewVel.setVisibility(View.VISIBLE);
        	mWebViewVel.loadUrl("javascript:gauge.setValue('" + someText + "');");
        	txtvelDig.setVisibility(View.INVISIBLE);
        }
        else
        {
        	txtvelDig.setVisibility(View.VISIBLE);
        	mWebViewVel.setVisibility(View.INVISIBLE);
        	//someText = someText.replace(',', '.').toString();
        	double valorMostrar = Double.parseDouble(someText);
        	txtvelDig.setText(formateador.format(valorMostrar));
        }	
        //mWebViewVel.loadUrl("javascript:$(document).ready(function () {var gauge = new Gauge({renderTo: 'gauge',width: 260,height: 260,glow: true,units: 'Km/h',title: false,minValue: 0,maxValue: 200,majorTicks: ['0', '20', '40', '60', '80', '100', '120', '140', '160', '180', '200'],minorTicks: 1,strokeTicks: true,highlights: [{ from: 0, to: 50, color: 'rgba(0,   255, 0, .15)' },{ from: 50, to: 80, color: 'rgba(255, 255, 0, .99)' },{ from: 80, to: 100, color: 'rgba(255, 30,  0, .99)' },{ from: 100, to: 150, color: 'rgba(250, 130,  88, .99)' },{ from: 150, to: 200, color: 'rgba(255, 0,  0, .99)' }],colors: {plate: '#3A01DF',majorTicks: '#f5f5f5',minorTicks: '#ddd',title: '#fff',units: '#fff',numbers: '#fff',needle: { start: 'rgba(240, 128, 128, 1)', end: 'rgba(255, 160, 122, .9)' }}});gauge.onready = function () { gauge.setValue('" + someText + "'); };gauge.draw();});");
    }
	public void setLocationString(String direccion)
	{
		String dirMostrar = "";
		if (direccion != "")
		{
			try {
			String dir[] = direccion.split("|");
			dirMostrar = dir[0] + ", " + dir[1];
			lblDireccion = (TextView)findViewById(R.id.lblVelocidad);
			//lblDireccion2 = (TextView)findViewById(R.id.lblDireccion2);
			lblDireccion.setText(dirMostrar);
			//lblDireccion2.setText(dir[1]);
			}
			catch(Exception e)
			{
				lblDireccion.setText("Error");
				//lblDireccion2.setText("Error");
			}
		}

	}
	
	private void ObtenerResumenHoy()
	{
		//imagenTiempo = (ImageView)findViewById(R.id.imageView1);
		mWebView = (WebView)findViewById(R.id.webView1);
		
        MyJavaScriptInterface myJavaScriptInterface
    		= new MyJavaScriptInterface(this);
        	mWebView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");
		
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setGeolocationEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setBlockNetworkImage(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        //mWebView.loadUrl("file:///android_asset/Hoy.html");
        //mWebView.loadUrl("file:///android_asset/hoy1.html");
        mWebView.loadUrl("file:///android_asset/example_woeid.html");
	}
    public void SetearValores(int imagen, int imagenManana)
    {
		//imagenTiempo = (ImageView)findViewById(R.id.imageView1);
    	//imagenTiempo.setImageResource(imagen);   	
    }

	
	public class MyJavaScriptInterfaceManana {
		Context mContext;

	    MyJavaScriptInterfaceManana(Context c) {
	        mContext = c;
	    }


	  
	}
	
	public class MyJavaScriptInterface {
		Context mContext;

	    MyJavaScriptInterface(Context c) {
	        mContext = c;
	    }

	    public void showToast(String toast){

	    	//Log.v("mylog","toast codigo hoy:" + toast);

	    	

	    }
	    
	}
	public void CargarVelocimetro()
	{

			mWebViewVel = (WebView)this.findViewById(R.id.webView2);
			mWebViewVel.getSettings().setJavaScriptEnabled(true);
			mWebViewVel.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			mWebViewVel.getSettings().setGeolocationEnabled(true);
			mWebViewVel.getSettings().setLoadsImagesAutomatically(true);
			mWebViewVel.getSettings().setBlockNetworkImage(true);
			mWebViewVel.getSettings().setAllowFileAccess(true);
			mWebViewVel.getSettings().setAllowFileAccess(true);
			//mWebViewVel.getSettings().setAllowUniversalAccessFromFileURLs(true);
			mWebViewVel.loadUrl("file:///android_asset/velocimetro.html");
	}
	private void ActualizarAfuera()
	{
				
		int carga = cargaBateria();
		txtAct.setText(getHoraActual());
		try {
			mWebView = (WebView)findViewById(R.id.webView1);
	        mWebView.getSettings().setJavaScriptEnabled(true);
	        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
	        mWebView.getSettings().setGeolocationEnabled(true);
	        mWebView.getSettings().setLoadsImagesAutomatically(true);
	        mWebView.getSettings().setBlockNetworkImage(true);
	        mWebView.getSettings().setAllowFileAccess(true);
	        mWebView.getSettings().setAllowFileAccess(true);
	        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);

	        //mWebView.loadUrl("file:///android_asset/example_woeid.html");
			char comilla = '"';
			//mWebView.loadUrl("javascript:weatherGeocode('" + DIRECCION_MOSTRAR + "', '', '" + PAIS_MOSTRAR + "');");
			mWebView.loadUrl("javascript:$(document).ready(function () {weatherGeocode('" + DIRECCION_MOSTRAR + "', '', '" + PAIS_MOSTRAR + "');});");
			Log.v("mylog","javascript:$(document).ready(function () {weatherGeocode('" + DIRECCION_MOSTRAR + "', '', '" + PAIS_MOSTRAR + "')});");
		}
		catch(Exception ex)
		{
	           Toast.makeText(getApplicationContext(), 
	                    ex.getMessage(), 
	                    Toast.LENGTH_SHORT).show(); 
		}	
	}
	private void GuardarRegistro()
	{
		//si no ha sido guardada la direccion de inicio no se guarda
		//en base de datos
			if (DireccionInicio != "")
			{
				DecimalFormat formateador = new DecimalFormat("###.#");
				ContentValues registro = new ContentValues();
				double distancia = SUMADOR_DISTANCIA /1000;
				//registro.put("Codigo", "1");
				registro.put("Fecha", ObtenerFecha());
				registro.put("DireccionInicio", DireccionInicio);
				registro.put("LatitudInicio", ANTERIOR_LATITUD);
				registro.put("LongitudInicio", ANTERIOR_LONGITUD);
				registro.put("DireccionTermino", entregaDireccion(ULTIMA_LATITUD, ULTIMA_LONGITUD));
				registro.put("LatitudTermino", ULTIMA_LATITUD);
				registro.put("LongitudTermino", ULTIMA_LONGITUD);
				registro.put("DistanciaRecorrida", distancia);
				registro.put("VelocidadMaxima", mayorVel);
				registro.put("VelocidadPromedio", formateador.format(VELOCIDAD_PROMEDIO_ACTUAL));
				registro.put("TiempoTranscurrido", crono1.getText().toString());
				registro.put("HoraInicio", HORA_INICIO);
				registro.put("HoraTermino", HORA_TERMINO);
				Estadistica.InsertarRegistro(registro, this.getBaseContext());
				//DESPUES DE GUARDAR LA HORA DE INICIO DEBIERA SER RESETEADA
				HORA_INICIO= getHoraActual();
				//la direccion de inicio tambien debiera cambiar
				//por lo tanto dejamos el contador en 0 para que vuelva a 
				//pasar al obtener la locacion
				CONTADOR_LATITUD = 0;
				//el promedio debiera volver a cero
				VELOCIDAD_PROMEDIO_ACTUAL = 0;
				SUMADOR_DISTANCIA = 0;
				//reseteamos el tiempo transcurrido
				crono1.setBase(SystemClock.elapsedRealtime());
				crono1.setText("00:00");
				crono1.start();
				
				//velocidad maxima a 0
				mayorVel = 0;
				
				
			}
			else
			{
				Toast.makeText(getApplicationContext(), "No se puede guardar hasta que se obtenga dirección válida", Toast.LENGTH_LONG).show();


			}

		
	}

	 public static String getHoraActual() {
		  Date ahora = new Date();
		  SimpleDateFormat formateador = new SimpleDateFormat("hh:mm:ss");
		  return formateador.format(ahora);
		 }

	 // Cursor myMessages = context.getContentResolver().query(Uri.parse("content://sms"), new String[] { "address", "_id", "body", "type", "date", "read", "thread_id" }, "date > your_timestamp", null, "date ASC");
	 
	public void VerificaMensajes2()
	{
		 String SMS_READ_COLUMN = "read";
         boolean unreadOnly = true;
         String WHERE_CONDITION = unreadOnly  ? SMS_READ_COLUMN + " = 0" : null;
         String SORT_ORDER = "date DESC";
         int count = 0;
         int contador=0;
         ContentResolver cr =getContentResolver();
         Uri uri = Uri.parse("content://sms");
         
         StringBuilder mostrar = new StringBuilder();
         mostrar.append("Mensajes:\r\n");
		 Cursor cursor = cr.query(
		       uri,
		       new String[] { "_id", "thread_id", "address", "person", "date", "body" },
		                 WHERE_CONDITION,
		                 null,
		                 SORT_ORDER);
		
		 if (cursor != null) {
			 try {
                 count = cursor.getCount();
                 if (count > 0) {
                         cursor.moveToFirst();

                         long messageId = cursor.getLong(0);
                         long threadId = cursor.getLong(1);
                         String address = cursor.getString(2);
                         long contactId = cursor.getLong(3);
                         String contactId_string = String.valueOf(contactId);
                         long timestamp = cursor.getLong(4);

                         String body = cursor.getString(5);
                         contador = count + 1;
                         
                         mostrar.append(String.valueOf(contador));
                         mostrar.append(". ");
                         mostrar.append(address);
                         mostrar.append(body);
                         mostrar.append(" - ");
                         

                         Toast.makeText(getApplicationContext(), mostrar.toString(), Toast.LENGTH_LONG).show();

                         
                         if (!unreadOnly) {
                                 count = 0;
                                 contador = 0;
                         }


                 }
			 } 
			 finally {
                 cursor.close();
			 }
			 
			 
		 }
		 Log.v("sms", String.valueOf(count));
         
	}
	
	 
	 public void VerificarMensajes()
	 {
		StringBuilder sb = new StringBuilder();
		String body;
		String adres;
		String read;
		String status;
		int contadorNoLeidos = 0;
		String SMS_READ_COLUMN = "read";
		String WHERE_CONDITION = SMS_READ_COLUMN + " = 0";
	    
	    ContentResolver cr =getContentResolver();
        Uri uri = Uri.parse("content://sms");
        sb.append("Mensajes no leídos:\r\n");
        
        //Uri uri = Uri.parse("content://sms"); -- For all SMS
        //Uri uri = Uri.parse("content://sms/sent"); -- For all Sent Items
        //If you want to read the Sent SMS then change the URi to /sent.

        //In this example we are using Query as we have defined URi as above.
        //We have declared all the Column names we need in string array in the second parameter.
        //If you dont need all then leave null
        //Notice that we did not call managedQuery instead we used Query method of ContentResolver
        Cursor messagesCursor = cr.query(uri, new String[] { "_id","address","body","person","read","status"}, WHERE_CONDITION,null, null);
        if (messagesCursor != null)
        {

	        if(messagesCursor.getCount() > 0)
	        {
	        	int colread = messagesCursor.getColumnIndex("read");
	        	int colStatus = messagesCursor.getColumnIndex("status");
	        	int colAdres = messagesCursor.getColumnIndex("address");
	        	int colBody = messagesCursor.getColumnIndex("body");
	            while(messagesCursor.moveToNext())
	            {
	                //colName = colName +  messagesCursor.getString(messagesCursor.getColumnIndex("body")) + "--";
	                //colName = colName +  messagesCursor.getString(messagesCursor.getColumnIndex("address")) + "\n";
	            	read = messagesCursor.getString(colread);
	            	status = messagesCursor.getString(colStatus);
	            	adres = messagesCursor.getString(colAdres);
	            	body = messagesCursor.getString(colBody);
	            	sb.append(adres);
	            	sb.append("Mensaje: " + body);
	            	sb.append(" - ");
	            	Log.v("sms", read + " ----- " + body);
	            	
	                contadorNoLeidos++;

	            }
	            Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
	        }
	        //Log.v("sms", colName);
        }
        Log.v("leidos", String.valueOf(contadorNoLeidos));
		
        
	 }
	 public void VerificaLLamadas()
	 {
		 StringBuilder sb = new StringBuilder();
		 int contadorPerdidas = 0;
		 String[] projection = new String[] {
				    Calls.TYPE,
				    Calls.NUMBER,
				    Calls.IS_READ,
				    Calls.CACHED_NAME};
		 
		 Uri llamadasUri =  Calls.CONTENT_URI;
		 ContentResolver cr = getContentResolver();
		 Cursor cur = cr.query(llamadasUri,
			        projection, //Columnas a devolver
			        null,       //Condición de la query
			        null,       //Argumentos variables de la query
			        null);      //Orden de los resultados
		 
		 sb.append("Llamadas Perdidas:\r\n");
		 
		 
		 if (cur.moveToFirst())
		 {
		     int tipo;
		     String tipoLlamada = "";
		     String telefono;
		     String name;
		     int esta_leida;
		  
		     int colTipo = cur.getColumnIndex(Calls.TYPE);
		     int colTelefono = cur.getColumnIndex(Calls.NUMBER);
		     int colLeido = cur.getColumnIndex(Calls.IS_READ);
		     int colName = cur.getColumnIndex(Calls.CACHED_NAME);
		  
		     //txtResultados.setText("");
		  
		     do
		     {
		         tipo = cur.getInt(colTipo);
		         telefono = cur.getString(colTelefono);
		         esta_leida = cur.getInt(colLeido);
		         
		         if(tipo == Calls.INCOMING_TYPE)
		             tipoLlamada = "ENTRADA";
		         else if(tipo == Calls.OUTGOING_TYPE)
		             tipoLlamada = "SALIDA";
		         else if(tipo == Calls.MISSED_TYPE)
		         {
		        	 Log.v("mylog","perdida: " + String.valueOf(esta_leida));
		        	 if (esta_leida == 0)
		        	 {
			             tipoLlamada = "PERDIDA";
			             name = cur.getString(colName);
			             
			             sb.append(telefono);
			             if (name != "" && name != null)
			            	 sb.append(" " + name);
			             //sb.append(fecha);
			             if (contadorPerdidas == 0)
			             {
			            	 sb.append(" - ");
			             }
			            	 
			             contadorPerdidas++;
		        	 }
		         }
		         //txtResultados.append(tipoLlamada + " - " + telefono + "\n");
		  
		     } while (cur.moveToNext());
		 }
		 Log.v("mylog","Llamadas: " + String.valueOf(contadorPerdidas));
		 if (contadorPerdidas > 0)
		 {
			 Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
		 }
		 
	 }

}
