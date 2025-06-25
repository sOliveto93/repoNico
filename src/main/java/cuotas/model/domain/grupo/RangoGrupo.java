package cuotas.model.domain.grupo;

import common.model.domain.util.Rango;
import cuotas.model.domain.datos.Grupo;

public class RangoGrupo extends Rango<Grupo> {

	public RangoGrupo(){
		
	}
	public RangoGrupo(Grupo grupoDesde, Grupo grupoHasta){
		if(grupoDesde != null)
			setDesde(grupoDesde);
		
		if(grupoHasta != null)
			setHasta(grupoHasta);
	}
	@Override
	public void rangoOrder(Grupo desde, Grupo hasta) {
		Grupo aux=null;
		if(desde.compareTo(hasta)>0){
			aux = hasta;
			setHasta(desde);
			setDesde(aux);
		}else if(desde.compareTo(hasta)==0){
			setDesde(desde);
		}else{
			setDesde(desde);
			setHasta(hasta);
		}
	}

	

}
