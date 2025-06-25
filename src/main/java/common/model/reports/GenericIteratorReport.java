package common.model.reports;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;


/**
 * Generates a report in the following scenario:
 * <ol>
 * <li>The report definition file is a .prpt file which will be loaded and parsed
 * <li>The data factory is a simple JDBC data factory using HSQLDB
 * <li>There are no runtime report parameters used
 * </ol>
 */
public abstract class GenericIteratorReport<T> extends AbstractReportGenerator{
	
	private static final String QUERY_NAME = "ReportQuery";
	private URL templateURL;
	private AbstractReportModelator<T> am;
	private Iterator<T> resultados;
	
	public GenericIteratorReport(){	
	}

  /**
   * Returns the report definition which will be used to generate the report. In this case, the report will be
   * loaded and parsed from a file contained in this package.
   *
   * @return the loaded and parsed report definition to be used in report generation.
   */
	public MasterReport getReportDefinition(){
	    try
	    {
	      // Parse the report file
	      final ResourceManager resourceManager = new ResourceManager();
	      resourceManager.registerDefaults();
	      final Resource directly = resourceManager.createDirectly(getTemplateURL(), MasterReport.class);
	      MasterReport resultado = (MasterReport) directly.getResource();
	      if(am != null){
	    	  // En base al mapeo especificado y al iterator de resultados arma el juego de datos.
	    	  resultado.setDataFactory(new TableDataFactory(QUERY_NAME, am.getModel(this.getResults())));
	    	  resultado.setQuery(QUERY_NAME);
	      }
	      return  resultado;
	    }
	    catch (ResourceException e){
	      e.printStackTrace();
	    }
	    return null;
	 }
  
	public URL getTemplateURL(){
		return this.templateURL;
	}
	
	public void setTemplateURL(URL location){
		this.templateURL = location;
	}
  
	public Iterator<T> getResults(){
		return this.resultados;
	}
	
	public void setResults(Iterator <T> results){
		this.resultados = results;
	}
	
	public AbstractReportModelator<T> getAm() {
		return am;
	}
	
	public void setAm(AbstractReportModelator<T> am) {
		this.am = am;
	}

  /**
   * Retorna el DataFactory que sera usado en la generacion del reporte.
   * Si se definio un AbstractReportModelator debe implementar como "return null"
   *
   * @return the data factory used with the report generator
   */
  public abstract DataFactory getDataFactory();
  
  /**
   * Retorna un Map de parametros para "alimentar" al reporte.
   * En el reporte pueden ser llamados por su key y luego será reemplazado el valor.
   *
   * @return <code>null</code> indica el generador de reportes que no recibira parametros.
   */
  public abstract Map<String, Object> getReportParameters();
}
