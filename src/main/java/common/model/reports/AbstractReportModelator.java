package common.model.reports;

import java.util.Iterator;

import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

public abstract class  AbstractReportModelator<T> {

	private String [] columnNames;
	@SuppressWarnings("unchecked")
	private Class[] columnTypes;
	private TypedTableModel model;
	
	public AbstractReportModelator(){
		
		columnNames = createColumnNames();
		columnTypes = createColumnTypes();
		
		model = new TypedTableModel(columnNames, columnTypes);
	}
	
	public void flushModel(){
		model = new TypedTableModel(columnNames, columnTypes);
	}
	
	public abstract String [] createColumnNames();
	@SuppressWarnings("unchecked")
	public abstract Class[] createColumnTypes();
	
	public abstract TypedTableModel getModel(Iterator<T> iterator);

	public void setColumnNames(String [] columnNames) {
		this.columnNames = columnNames;
	}

	public String [] getColumnNames() {
		return columnNames;
	}

	@SuppressWarnings("unchecked")
	public void setColumnTypes(Class[] columnTypes) {
		this.columnTypes = columnTypes;
	}

	@SuppressWarnings("unchecked")
	public Class[] getColumnTypes() {
		return columnTypes;
	}

	public void setModel(TypedTableModel model) {
		this.model = model;
	}

	public TypedTableModel getModel() {
		return model;
	}

}