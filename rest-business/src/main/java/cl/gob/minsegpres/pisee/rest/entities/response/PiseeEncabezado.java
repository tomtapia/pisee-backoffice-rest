package cl.gob.minsegpres.pisee.rest.entities.response;

public class PiseeEncabezado {

	//-- attribs
	private String idSobre;
	private String emisorSobre;
	private String estadoSobre;
	private String glosaSobre;
	private String nombreProveedor;
	private String nombreServicio;
	private String nombreConsumidor;
	private String nombreTramite;
	private String fechaHora;
	private String fechaHoraReq;
	
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
	public String getFechaHoraReq() {
		return fechaHoraReq;
	}
	
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
	public void setFechaHoraReq(String fechaHoraReq) {
		this.fechaHoraReq = fechaHoraReq;
	}
	
}
