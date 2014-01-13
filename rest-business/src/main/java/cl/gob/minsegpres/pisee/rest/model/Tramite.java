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
@Table(name = "TRA_TRAMITE")
public class Tramite implements java.io.Serializable {

	private static final long serialVersionUID = -6838107402212792891L;
	
	@Id
	@Column(name = "TRA_ID", unique = true, nullable = false, precision = 18, scale = 0)	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="idSequence")
	@SequenceGenerator(name="idSequence", sequenceName="SEQ_PISEE")	
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORG_ID", nullable = false)	
	private Organismo organismo;
	
	@Column(name = "TRA_NOMBRE", nullable = false, length = 200)	
	private String nombre;
	
	@Column(name = "TRA_ESTADO", nullable = false, length = 1)
	private String estado;
	
	@Column(name = "TRA_OBSERVACION", length = 200)
	private String observacion;
	
	@Column(name = "TRA_NOMBRE_CORTO", length = 40)
	private String nombreCorto;

	public Tramite() {
		
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

	public String getEstado() {
		return estado;
	}

	public String getObservacion() {
		return observacion;
	}

	public String getNombreCorto() {
		return nombreCorto;
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

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	//------
	@Transient
	private String descripcionEstado;
	
	public String getDescripcionEstado() {
		if ("A".equals(estado)){
			descripcionEstado = "Activo";
		}
		if ("B".equals(estado)){
			descripcionEstado = "Bloqueado";
		}		
		return descripcionEstado;
	}
	
	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}
	
	
}
