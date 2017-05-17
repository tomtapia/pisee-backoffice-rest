package cl.gob.minsegpres.pisee.rest.entities.response;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class PiseeEncabezado implements Serializable{

	//-- attribs
	private static final long serialVersionUID = -1023810672259549996L;
	
	@SerializedName("id_sobre")
	private String idSobre;
	
	@SerializedName("emisor_sobre")
	private String emisorSobre;
	
	@SerializedName("estado_sobre")
	private String estadoSobre;
	
	@SerializedName("glosa_sobre")
	private String glosaSobre;
	
	@SerializedName("proveedor")
	private String nombreProveedor;
	
	@SerializedName("servicio")
	private String nombreServicio;
	
	@SerializedName("consumidor")
	private String nombreConsumidor;
	
	@SerializedName("tramite")
	private String nombreTramite;
	
	@SerializedName("fecha_hora")
	private String fechaHora;
	
	
//	private String fechaHoraReq;
	
	//-- getters
	public String getIdSobre() {
		return idSobre;
	}
	public String getEmisorSobre() {
		return emisorSobre;
	}
	public String getEstadoSobre() {
		return estadoSobre;
	}
	public String getGlosaSobre() {
		return glosaSobre;
	}
	public String getNombreProveedor() {
		return nombreProveedor;
	}
	public String getNombreServicio() {
		return nombreServicio;
	}
	public String getNombreConsumidor() {
		return nombreConsumidor;
	}
	public String getNombreTramite() {
		return nombreTramite;
	}
	public String getFechaHora() {
		return fechaHora;
	}
//	public String getFechaHoraReq() {
//		return fechaHoraReq;
//	}
	
	//-- setters
	public void setIdSobre(String idSobre) {
		this.idSobre = idSobre;
	}
	public void setEmisorSobre(String emisorSobre) {
		this.emisorSobre = emisorSobre;
	}
	public void setEstadoSobre(String estadoSobre) {
		this.estadoSobre = estadoSobre;
	}
	public void setGlosaSobre(String glosaSobre) {
		this.glosaSobre = glosaSobre;
	}
	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}
	public void setNombreConsumidor(String nombreConsumidor) {
		this.nombreConsumidor = nombreConsumidor;
	}
	public void setNombreTramite(String nombreTramite) {
		this.nombreTramite = nombreTramite;
	}
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}
//	public void setFechaHoraReq(String fechaHoraReq) {
//		this.fechaHoraReq = fechaHoraReq;
//	}
	
}
