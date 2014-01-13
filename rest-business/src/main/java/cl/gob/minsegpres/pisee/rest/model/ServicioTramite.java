package cl.gob.minsegpres.pisee.rest.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
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

@Entity
@Table(name = "STR_SERV_TRAM")
public class ServicioTramite implements Serializable {

	private static final long serialVersionUID = -2577674641546331938L;
	
	@Id
	@Column(name = "STR_ID", unique = true, nullable = false, precision = 18, scale = 0)	
	@GeneratedValue(strategy=GenerationType.AUTO, generator="idSequence")
	@SequenceGenerator(name="idSequence", sequenceName="SEQ_PISEE")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "TRA_ID", nullable = false)	
	private Tramite tramite;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SRV_ID", nullable = false)	
	private Servicio servicio;

	public ServicioTramite() {
		
	}

	public Long getId() {
		return id;
	}

	public Tramite getTramite() {
		return tramite;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTramite(Tramite tramite) {
		this.tramite = tramite;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

}
