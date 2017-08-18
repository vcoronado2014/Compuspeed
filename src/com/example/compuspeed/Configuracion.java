package com.example.compuspeed;

import android.content.Context;
import android.content.SharedPreferences;


public class Configuracion {

	private final String SHARED_PREFS_FILE = "HMPrefs";
	private final String USA_ESCALA = "True";
	private final String USA_ANALOGO = "True";
	private final String MUESTRA_LLAMADAS_PERDIDAS = "False";
	private final String MUESTRA_MENSAJES_NO_LEIDOS = "False";

	private Context mContext;
	
	public Configuracion(Context context){
		 mContext = context;
	}
	private SharedPreferences getSettings(){
		 return mContext.getSharedPreferences(SHARED_PREFS_FILE, 0);
		}

	public String getUsaEscala(){
		 return getSettings().getString(USA_ESCALA, null);  
	}
	public String getUsaAnalogo(){
		 return getSettings().getString(USA_ANALOGO, null);  
	}
	public String getMuestraLLamadasPerdidas(){
		 return getSettings().getString(MUESTRA_LLAMADAS_PERDIDAS, null);  
	}
	public String getMuestraMensajes(){
		 return getSettings().getString(MUESTRA_MENSAJES_NO_LEIDOS, null);  
	}
	public void setMuestraMensajes(String muestraMensajes){
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putString(MUESTRA_MENSAJES_NO_LEIDOS, muestraMensajes);
		editor.commit();
	}
	public void setMuestraLlamadasPerdidas(String muestraLLamadasPerdidas){
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putString(MUESTRA_LLAMADAS_PERDIDAS, muestraLLamadasPerdidas);
		editor.commit();
	}
	public void setUsaEscala(String usaEscala){
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putString(USA_ESCALA, usaEscala);
		editor.commit();
	}
	public void setUsaAnalogo(String usaAnalogo){
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putString(USA_ANALOGO, usaAnalogo);
		editor.commit();
	}
	
}
