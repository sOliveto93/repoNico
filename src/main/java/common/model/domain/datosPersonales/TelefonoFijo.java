package common.model.domain.datosPersonales;


public class TelefonoFijo extends Telefono {

	public TelefonoFijo(){
		super();
		setTipo("Fijo");

	}

	public TelefonoFijo(String numero){
		super("Fijo",numero);

	}

	public boolean numeroValido(String num) {
		
		/** TODO validar numero fijo **/
		return true;

	}
}
