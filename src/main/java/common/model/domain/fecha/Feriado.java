package common.model.domain.fecha;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que representa los feriados, especificando el motivos
 * @author pmaseda
 *
 */
public class Feriado extends Fecha implements Serializable{

	private static final long serialVersionUID = 597764181986739937L;
	private Long idFeriado;
	private String motivo;
	
	public Feriado(){
		super();
		motivo="";
				
	}
	
	public Feriado(Date fecha){
		super(fecha);
		motivo="";
	}
	public Feriado(Date fecha, String mot){
		super(fecha);
		this.motivo = mot;
	}
	
	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Long getIdFeriado() {
		return idFeriado;
	}

	public void setIdFeriado(Long idFeriado) {
		this.idFeriado = idFeriado;
	}

}
