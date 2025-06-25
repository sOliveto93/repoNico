package cuotas.reports.src;

import java.util.HashMap;
import java.util.Map;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import common.model.reports.GenericIteratorReport;
import cuotas.model.domain.factura.Factura;
import cuotas.model.domain.factura.FacturaItem;

public class FacturaPorItemIteratorReport extends GenericIteratorReport<FacturaItem> {

	@Override
	public DataFactory getDataFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getReportParameters() {
		final Map<String, Object> parameters = new HashMap<String, Object>();
	      parameters.put("Report Title", "Reporte de pagos");
	      parameters.put("Col Headers BG Color", "yellow");

	      return parameters;
	}

}
