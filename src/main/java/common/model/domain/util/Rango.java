package common.model.domain.util;


public abstract class Rango<T> {

	private T desde;
	private T hasta;
	
	public abstract void rangoOrder(T desde, T hasta);
		
	public boolean estaCompleto(){
		return (this.desde !=null && this.hasta!=null);
	}
	
	public T getDesde() {
		return desde;
	}
	public void setDesde(T desde) {
		this.desde = desde;
	}
	public T getHasta() {
		return hasta;
	}
	public void setHasta(T hasta) {
		this.hasta = hasta;
	}
	
}
