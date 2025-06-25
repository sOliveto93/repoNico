package common.model.domain.datosPersonales;


public class TelefonoCelular extends Telefono {

	private static final long serialVersionUID = -7613932802787819316L;

	public TelefonoCelular(){
		super();
		setTipo("Celular");

	}
	
	public TelefonoCelular(String numero){
		super("Celular",numero);

	}

	public boolean numeroValido(String num) {
		/** TODO validar numero celular **/
		return true;

	}
}
