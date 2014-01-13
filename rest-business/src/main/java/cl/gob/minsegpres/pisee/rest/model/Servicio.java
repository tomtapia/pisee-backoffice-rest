package cl.gob.minsegpres.pisee.rest.model;

import java.io.Serializable;

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
@Table(name = "SRV_SERVICIO")
public class Servicio implements Serializable {

	private static final long serialVersionUID = 6631212931281899529L;
	
	@Id
	@Column(name = "SRV_ID", unique = true, nullable = false, precision = 18, scale = 0)	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="idSequence")
	@SequenceGenerator(name="idSequence", sequenceName="SEQ_PISEE")	
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORG_ID", nullable = false)	
	private Organismo organismo;
	
	@Column(name = "SRV_NOMBRE", length = 200)
	private String nombre;
	
	@Column(name = "SRV_TIPO", length = 5)
	private String tipo;
	
	@Column(name = "SRV_NOMBRE_CORTO", length = 40)
	private String nombreCorto;
	
	@Column(name = "SRV_WS", length = 50)
	private String ws;

	@Column(name = "SRV_WSDL", length = 200)
	private String wsdl;
	
	@Column(name = "SRV_ESTADO", length = 6)
	private String estado;
	
	@Column(name = "SRV_OBSERVACION", length = 200)
	private String observacion;
	
	public Servicio() {
		
	}

	public Long getId() {
		return id;
	}

	public Organismo getOrganismo() {
		return organismo;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public String getNombreCorto() {
		return nombreCorto;
	}

	public String getWs() {
		return ws;
	}

	public String getWsdl() {
		return wsdl;
	}
	
	public String getEstado() {
		return estado;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrganismo(Organismo organismo) {
		this.organismo = organismo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	public void setWs(String ws) {
		this.ws = ws;
	}

	public void setWsdl(String wsdl) {
		this.wsdl = wsdl;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	//------
	@Transient
	private String descripcionEstado;
	
	public String getDescripcionEstado() {
		if ("ACT".equals(estado)){
			descripcionEstado = "Activo";
		}
		if ("BLOQ".equals(estado)){
			descripcionEstado = "Bloqueado";
		}		
		return descripcionEstado;
	}
	
	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}
	
}
