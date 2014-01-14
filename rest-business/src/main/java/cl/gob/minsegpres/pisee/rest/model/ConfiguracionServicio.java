package cl.gob.minsegpres.pisee.rest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "CFG_CONFIG_SERVICIO")
public class ConfiguracionServicio implements java.io.Serializable{

	private static final long serialVersionUID = 8372825824409394856L;

	public static final String ACTIVO = "1";
	public static final String BLOQUEADO = "0";
	
	@Id
	@Column(name = "CFG_ID", unique = true, nullable = false, precision = 18, scale = 0)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="idSequence")
	@SequenceGenerator(name="idSequence", sequenceName="SEQ_CONFIG_SERVICIO")		
	private Long id;
	
	@Column(name = "CFG_TOKEN", length = 10)
	private String token;
	
	@Column(name = "CFG_OPERATION", length = 50)
	private String operation;
	
	@Column(name = "CFG_ENDPOINT", length = 255)	
	private String endPoint;	
	
	@Column(name = "CFG_TIMEOUT", precision = 5, scale = 0)
	private Long timeout;	
	
	@Column(name = "CFG_HTTP_USERNAME", length = 50)
	private String httpUserName;
	
	@Column(name = "CFG_HTTP_PASSWORD", length = 50)
	private String httpPassword;

	@Column(name = "CFG_CODIGO_INSTITUCION", length = 10)
	private String codigoInstitucion;
	
	@Column(name = "CFG_CODIGO_TRAMITE", length = 10)
	private String codigoTramite;
	
	@Column(name = "CFG_ESTADO", length = 1)
	private String estado;
	
	@Column(name = "CFG_REQUIERE_FIRMA", length = 1)
	private String requiereFirma;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STR_ID", nullable = false)		
	private ServicioTramite servicioTramite;
	
	//-- getters
	public Long getId() {
		return id;
	}
	public String getToken() {
		return token;
	}
	public String getOperation() {
		return operation;
	}
	public String getEndPoint() {
		return endPoint;
	}
	public Long getTimeout() {
		return timeout;
	}
	public String getHttpUserName() {
		return httpUserName;
	}
	public String getHttpPassword() {
		return httpPassword;
	}
	public ServicioTramite getServicioTramite() {
		return servicioTramite;
	}
	public String getCodigoInstitucion() {
		return codigoInstitucion;
	}
	public String getCodigoTramite() {
		return codigoTramite;
	}
	public String getEstado() {
		return estado;
	}
	public String getRequiereFirma() {
		return requiereFirma;
	}
	
	//--- setters
	public void setId(Long id) {
		this.id = id;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}
	public void setHttpUserName(String httpUserName) {
		this.httpUserName = httpUserName;
	}
	public void setHttpPassword(String httpPassword) {
		this.httpPassword = httpPassword;
	}
	public void setServicioTramite(ServicioTramite servicioTramite) {
		this.servicioTramite = servicioTramite;
	}
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	public void setCodigoTramite(String codigoTramite) {
		this.codigoTramite = codigoTramite;
	}	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void setRequiereFirma(String requiereFirma) {
		this.requiereFirma = requiereFirma;
	}	
	//------
	@Transient
	private String descripcionEstado;
	
	@Transient
	private String descripcionFirma;
	
	public String getDescripcionEstado() {
		if ("1".equals(estado)){
			descripcionEstado = "Activo";
		}
		if ("0".equals(estado)){
			descripcionEstado = "Bloqueado";
		}		
		return descripcionEstado;
	}
	
	public String requiereFirma() {
		if ("1".equals(estado)){
			descripcionFirma = "Si";
		}
		if ("0".equals(estado)){
			descripcionFirma = "No";
		}		
		return descripcionFirma;
	}
	
	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}
	
	public void setDescripcionFirma(String descripcionFirma) {
		this.descripcionFirma = descripcionFirma;
	}
	
	public boolean hasFirma(){
		return "1".equals(requiereFirma) ? true : false;
	}
}
