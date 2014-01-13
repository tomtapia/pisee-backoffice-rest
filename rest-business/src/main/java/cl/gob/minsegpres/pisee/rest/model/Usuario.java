package cl.gob.minsegpres.pisee.rest.model;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
@Table(name = "USR_USUARIO")
public class Usuario implements Serializable {

	private static final long serialVersionUID = -67015860591650357L;
	
	@Id
	@Column(name = "USR_ID", unique = true, nullable = false, precision = 18, scale = 0)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="idSequence")
	@SequenceGenerator(name="idSequence", sequenceName="USERS_SEQ")		
	private long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORG_ID", nullable = false)	
	private Organismo organismo;
	
	@Column(name = "USR_USERNAME", nullable = false, length = 20)
	private String username;
	
	@Column(name = "USR_PASSWORD", nullable = false, length = 40)
	private String password;
	
	@Column(name = "USR_EMAIL", length = 50)
	private String email;
	
	@Column(name = "USR_LLAVE_CAMBIO", length = 40)
	private String llaveCambio;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "USR_FECHA_CAMBIO", length = 7)	
	private Date fechaCambio;
	
	@Column(name = "USR_ROL", length = 6)	
	private String rol;
	
	@Column(name = "USR_TELEFONO", precision = 11, scale = 0)
	private Long telefono;
	
	@Column(name = "USR_RESPONSABLE", length = 1)
	private Character responsable;
	
	@Column(name = "USR_ESTADO", length = 6)
	private String estado;
	
	@Column(name = "USR_DESCRIPCION", length = 200)
	private String descripcion;
	
	@Column(name = "USR_CARGO", length = 50)
	private String cargo;
	
	@Column(name = "USR_NOMBRE", length = 50)
	private String nombre;
	
	@Column(name = "USR_RUT", length = 10)
	private String rut;
	
	@Column(name = "USR_CELULAR", precision = 11, scale = 0)
	private Long celular;
	
	@Column(name = "USR_FAX", precision = 11, scale = 0)
	private Long fax;
	
	@Column(name = "USR_XPLANNER", length = 1)
	private Character accesoPizarra;
	
	@Column(name = "USR_QATOOL", length = 1)
	private Character accesoQatool;
	
	@Column(name = "USR_PIZARRA", length = 1)
	private Character accesoXplanner;
	
	@Column(name = "USR_WEBLOGIC", length = 1)
	private Character accesoWeblogic;
	
	public Usuario() {
		
	}

	public long getId() {
		return id;
	}

	public Organismo getOrganismo() {
		return organismo;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getLlaveCambio() {
		return llaveCambio;
	}

	public Date getFechaCambio() {
		return fechaCambio;
	}

	public String getRol() {
		return rol;
	}

	public Long getTelefono() {
		return telefono;
	}

	public Character getResponsable() {
		return responsable;
	}

	public String getEstado() {
		return estado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getCargo() {
		return cargo;
	}

	public String getNombre() {
		return nombre;
	}

	public String getRut() {
		return rut;
	}

	public Long getCelular() {
		return celular;
	}

	public Long getFax() {
		return fax;
	}

	public Character getAccesoPizarra() {
		return accesoPizarra;
	}

	public Character getAccesoQatool() {
		return accesoQatool;
	}

	public Character getAccesoXplanner() {
		return accesoXplanner;
	}

	public Character getAccesoWeblogic() {
		return accesoWeblogic;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setOrganismo(Organismo organismo) {
		this.organismo = organismo;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setLlaveCambio(String llaveCambio) {
		this.llaveCambio = llaveCambio;
	}

	public void setFechaCambio(Date fechaCambio) {
		this.fechaCambio = fechaCambio;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public void setTelefono(Long telefono) {
		this.telefono = telefono;
	}

	public void setResponsable(Character responsable) {
		this.responsable = responsable;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public void setCelular(Long celular) {
		this.celular = celular;
	}

	public void setFax(Long fax) {
		this.fax = fax;
	}

	public void setAccesoPizarra(Character accesoPizarra) {
		this.accesoPizarra = accesoPizarra;
	}

	public void setAccesoQatool(Character accesoQatool) {
		this.accesoQatool = accesoQatool;
	}

	public void setAccesoXplanner(Character accesoXplanner) {
		this.accesoXplanner = accesoXplanner;
	}

	public void setAccesoWeblogic(Character accesoWeblogic) {
		this.accesoWeblogic = accesoWeblogic;
	}

	//------
	@Transient
	private String descripcionEstado;
	@Transient
	private String descripcionRol;
	
	public String getDescripcionEstado() {
		if ("ACT".equals(estado)){
			descripcionEstado = "Activo";
		}
		if ("BLOQ".equals(estado)){
			descripcionEstado = "Bloqueado";
		}		
		return descripcionEstado;
	}
	public String getDescripcionRol() {
		if ("A".equals(rol)){
			descripcionRol = "Administrador";
		}
		if ("U".equals(rol)){
			descripcionRol = "Usuario";
		}	
		if ("P".equals(rol)){
			descripcionRol = "Director PISEE";
		}		
		return descripcionRol;
	}	
	
	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}	
	public void setDescripcionRol(String descripcionRol) {
		this.descripcionRol = descripcionRol;
	}	

}