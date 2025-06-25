package common.model.domain.fecha;

import common.model.domain.util.Rango;

public class RangoFecha extends Rango<Fecha> {

	public RangoFecha(){
		
	}
	public RangoFecha(Fecha fechaDesde, Fecha fechaHasta){
		setHasta(fechaHasta);
		setDesde(fechaDesde);
	}
	public RangoFecha(String fechaDesde,String fechaHasta){
		Fecha desde = new Fecha(fechaDesde);
		Fecha hasta = new Fecha(fechaHasta);
		setHasta(hasta);
		setDesde(desde);
	}
	@Override
	public void rangoOrder(Fecha desde, Fecha hasta) {
		Fecha aux;
		if(desde.compareTo(hasta)>0){
			aux = hasta;
			setHasta(hasta);
			setDesde(desde);
		}else if(desde.compareTo(hasta)==0){
			setDesde(desde);
		}else{
			setHasta(hasta);
			setDesde(desde);
		}
	}
	public String toString(){
		return "desde "+getDesde()+ " a "+ getHasta();
	}

}
