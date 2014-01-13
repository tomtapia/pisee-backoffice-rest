package cl.gob.minsegpres.pisee.rest.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ORG_ORGANISMO")
public class Organismo implements Serializable {

	private static final long serialVersionUID = -7313768775907222904L;
	
	@Id
	@Column(name = "ORG_ID", unique = true, nullable = false, precision = 18, scale = 0)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="idSequence")
	@SequenceGenerator(name="idSequence", sequenceName="SEQ_PISEE")	
	private Long id;
	
	@Column(name = "ORG_SIGLA", length = 10)
	private String sigla;
	
	@Column(name = "ORG_NOMBRE", nullable = false, length = 40)
	private String nombre;
	
	@Column(name = "ORG_DIRECCION", nullable = false, length = 100)
	private String direccion;
	
	@Column(name = "ORG_TIPO", nullable = false, length = 1)
	private String tipo;
	
	@Column(name = "ORG_OBSERVACION", length = 200)
	private String observacion;
	
	@Column(name = "ORG_ESTADO", length = 6)
	private String estado;
	
	@Column(name = "ORG_PASSWORD", length = 50)
	private String password;

	@Column(name = "ORG_HOST", length = 50)
	private String host;

	@Column(name = "ORG_PUERTO", length = 50)
	private String puerto;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organismo")
	private List<Usuario> usuarios; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organismo")
	private List<Tramite> tramites ;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organismo")
	private List<Servicio> servicios;
	
	public Organismo() {
		
	}

	public Long getId() {
		return id;
	}

	public String getSigla() {
		return sigla;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public String getTipo() {
		return tipo;
	}

	public String getObservacion() {
		return observacion;
	}

	public String getEstado() {
		return estado;
	}

	public String getPassword() {
		return password;
	}

	public String getHost() {
		return host;
	}
	
	public String getPuerto() {
		return puerto;
	}
	
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public List<Tramite> getTramites() {
		return tramites;
	}

	public List<Servicio> getServicios() {
		return servicios;
	}

	//---
	public void setId(Long id) {
		this.id = id;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public void setTramites(List<Tramite> tramites) {
		this.tramites = tramites;
	}

	public void setServicios(List<Servicio> servicios) {
		this.servicios = servicios;
	}

	//---	
	@Transient
	private String descripcionEstado;
	
	@Transient
	private String descripcionTipo;	
	
	public String getDescripcionEstado() {
		if ("ACT".equals(estado)){
			descripcionEstado = "Activo";
		}
		if ("BLOQ".equals(estado)){
			descripcionEstado = "Bloqueado";
		}		
		return descripcionEstado;
	}
	
	public String getDescripcionTipo() {
		if ("C".equals(tipo)){
			descripcionTipo = "Consumidor";
		}
		if ("P".equals(tipo)){
			descripcionTipo = "Proveedor";
		}
		if ("A".equals(tipo)){
			descripcionTipo = "Ambos";
		}
		if ("D".equals(tipo)){
			descripcionTipo = "Administrador";
		}		
		return descripcionTipo;
	}
	
	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}	

	public void setDescripcionTipo(String descripcionTipo) {
		this.descripcionTipo = descripcionTipo;
	}	
	
}
