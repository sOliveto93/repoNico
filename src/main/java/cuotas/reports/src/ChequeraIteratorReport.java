package cuotas.reports.src;

import java.util.HashMap;
import java.util.Map;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import common.model.reports.GenericIteratorReport;
import cuotas.model.domain.datos.Chequera;

public class ChequeraIteratorReport extends GenericIteratorReport<Chequera> {

	@Override	
	public DataFactory getDataFactory() {
		return null;
	}

	@Override
	public Map<String, Object> getReportParameters() {
	     final Map<String, Object> parameters = new HashMap<String, Object>();
	      parameters.put("Report Title", "Reporte de alumnos DEP");
	      parameters.put("Col Headers BG Color", "yellow");
	          
	      return parameters;
	}
	
}
