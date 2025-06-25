package common.model.domain.datos;

public abstract class GenericValidableBean<ID extends Number> extends GenericBean<ID> {

	private static final long serialVersionUID = -4776236656610871012L;
	
	private Boolean isValidado = new Boolean(false);
	protected Boolean isValido = new Boolean(false);
	
	public void validar(){
		reglasValidacion();
		this.isValidado = new Boolean(true);		
	}
	
	public abstract void reglasValidacion();
	
	public Boolean isValidado(){
		return isValidado;
	}

	public Boolean isValido(){
		return isValido;
	}
}
