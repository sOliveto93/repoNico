package common.model.domain.fecha;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class FechaNacimiento extends Fecha {

	private static final long serialVersionUID = 1256487764480141848L;
	
	public FechaNacimiento(){
		super();
	}
	
	public FechaNacimiento(String fecha) {
		super(fecha, false);
	}
	
	
	public Integer getEdad() {
		Integer edad ;
		if(super.getFecha() == null){ 
			edad =  -1;
		}else{
			long ageInMillis = new GregorianCalendar().getTimeInMillis() - super.getFecha().getTime();
			Date age = new Date(ageInMillis);
			SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");
			edad = Integer.parseInt(simpleDateformat.format(age)) - 1970;
		}
		return edad;        
	}

}
