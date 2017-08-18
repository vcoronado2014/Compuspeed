package com.example.compuspeed;

import android.annotation.SuppressLint;
import java.nio.charset.CodingErrorAction;
import java.text.DecimalFormat;

@SuppressLint("DefaultLocale")
public class Recorrido {

	public String codigo;
	public String fecha;
	public String direccionInicio;
	public String latitudInicio;
	public String longitudInicio;
	public String direccionTermino;
	public String latitudTermino;
	public String longitudTermino;
	public String distanciaRecorrida;
	public String velocidadMaxima;
	public String velocidadPromedio;
	public String tiempoTranscurrido;
	public String horaInicio;
	public String horaTermino;

	public Recorrido()
	{
		
	}
	//primer constructor
	public Recorrido(String codigo, String fecha, String direccionInicio, String latitudInicio,
			String longitudInicio, String direccionTermino, String latitudTermino, String longitudTermino,
			String distanciaRecorrida, String velocidadMaxima, String velocidadPromedio, String tiempoTranscurrido) {
		this.codigo = codigo;
		this.fecha = fecha;
		this.direccionInicio = direccionInicio;
		this.latitudInicio = latitudInicio;
		this.longitudInicio = longitudInicio;
		this.direccionTermino = direccionTermino;
		this.latitudTermino = latitudTermino;
		this.longitudTermino = longitudTermino;
		this.distanciaRecorrida = distanciaRecorrida;
		this.velocidadMaxima = velocidadMaxima;
		this.velocidadPromedio = velocidadPromedio;
		this.tiempoTranscurrido = tiempoTranscurrido;
		
	}

	public Recorrido(String codigo, String fecha, String direccionInicio, String direccionTermino, String velocidadMaxima, 
			String distanciaRecorrida, String tiempoTranscurrido, String horaInicio, String horaTermino)
	{
		this.codigo = codigo;
		this.fecha = fecha;
		this.direccionInicio = direccionInicio;
		this.direccionTermino = direccionTermino;
		this.distanciaRecorrida = distanciaRecorrida;
		this.velocidadMaxima = velocidadMaxima;
		this.tiempoTranscurrido = tiempoTranscurrido;
		this.horaInicio = horaInicio;
		this.horaTermino = horaTermino;
	}
	public String getCODIGO() {
		return codigo;
	}
	public void setCODIGO(String codigo) {
		this.codigo = codigo;
	}
	
	public String getFECHA() {
		return fecha;
	}
	public void setFECHA(String fecha) {
		this.fecha = fecha;
	}
	
	public String getDIRECCION_INICIO() {
		return direccionInicio;
	}
	public void setDIRECCION_INICIO(String direccionInicio) {
		this.direccionInicio = direccionInicio;
	}	
	
	public String getLATITUD_INICIO() {
		return latitudInicio;
	}
	public void setLATITUD_INICIO(String latitudInicio) {
		this.latitudInicio = latitudInicio;
	}
	

	public String getLONGITUD_INICIO() {
		return longitudInicio;
	}
	public void setLONGITUD_INICIO(String longitudInicio) {
		this.longitudInicio = longitudInicio;
	}	
	
	public String getDIRECCION_TERMINO() {
		return direccionTermino;
	}
	public void setDIRECCION_TERMINO(String direccionTermino) {
		this.direccionTermino = direccionTermino;
	}
	
	public String getLATITUD_TERMINO() {
		return latitudTermino;
	}
	public void setLATITUD_TERMINO(String latitudTermino) {
		this.latitudTermino = latitudTermino;
	}
	

	public String getLONGITUD_TERMINO() {
		return longitudTermino;
	}
	public void setLONGITUD_TERMINO(String longitudTermino) {
		this.longitudTermino = longitudTermino;
	}
	
	public String getDISTANCIA_RECORRIDA() {
		return distanciaRecorrida;
	}
	public void setDISTANCIA_RECORRIDA(String distanciaRecorrida) {
		this.distanciaRecorrida = distanciaRecorrida;
	}
	
	public String getVELOCIDAD_MAXIMA() {
		//return  String.format("%.2f", velocidadMaxima);
		return velocidadMaxima;
	}
	public void setVELOCIDAD_MAXIMA(String velocidadMaxima) {
		this.velocidadMaxima = velocidadMaxima;
	}
	
	public String getVELOCIDAD_PROMEDIO() {
		return velocidadPromedio;
	}
	public void setVELOCIDAD_PROMEDIO(String velocidadPromedio) {
		this.velocidadPromedio = velocidadPromedio;
	}
	
	public String getTIEMPO_TRANSCURRIDO() {
		return tiempoTranscurrido;
	}
	public void setTIEMPO_TRANSCURRIDO(String tiempoTranscurrido) {
		this.tiempoTranscurrido = tiempoTranscurrido;
	}
	public String getHORA_INICIO() {
		return horaInicio;
	}
	public void setHORA_INICIO(String horaInicio) {
		this.horaInicio = horaInicio;
	}
	public String getHORA_TERMINO() {
		return horaTermino;
	}
	public void setHORA_TERMINO(String horaTermino) {
		this.horaTermino = horaTermino;
	}
	
	
}
