package common.model.domain.util;

import java.util.Calendar;

public class Reloj {

	public static String getHoraActual(){
		Calendar toDay = Calendar.getInstance();
		int hora = toDay.get(Calendar.HOUR_OF_DAY);
		int min = toDay.get(Calendar.MINUTE);
		int seg = toDay.get(Calendar.SECOND);
		
		String horario="";
		if(hora < 10) horario+="0";
		horario+= hora+":";
		if(min < 10) horario+="0";
		horario+=min+":";
		if(seg < 10) horario+="0";
		horario+=seg;
		
		return horario;
		
	}
}
