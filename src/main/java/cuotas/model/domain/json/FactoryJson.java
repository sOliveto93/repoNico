package cuotas.model.domain.json;


public class FactoryJson {

	public static final GrupoJson grupoJson;
	public static final CuotaJson cuotasJson;
	public static final CuotaJson cuotaJson;
	public static final CarreraJson carreraJson;
	public static final PersonaJson personaJson;
	public static final InscripcionesJson inscripcionesJson; 
	public static final DomicilioJson domicilioJson;
	public static final FacturaItemTipoJson facturaItemTipoJson;
	public static final RubroCobroJson rubroCobroJson;
	public static final FacturaItemJson facturaItemJson;
	public static final FacturaJson facturaJson;
	public static final PagosJson pagosJson;
	public static final PartidaJson partidaJson;
	public static final TipoPagoJson tipoPagoJson;
	public static final BecaJson becaJson;
	public static final FormaPagoJson formaPagoJson;
	public static final PersonasBecaJson personasBecaJson;
	public static final CodigosAnterioresJson codigosAnterioresJson;
	public static final OrigenPagoJson origenPagoJson;
	static {
		try{
			origenPagoJson = new OrigenPagoJson();
			codigosAnterioresJson = new CodigosAnterioresJson();
			formaPagoJson = new FormaPagoJson();
			becaJson = new BecaJson();
			carreraJson = new CarreraJson();
			pagosJson = new PagosJson();
			facturaJson = new FacturaJson();
			facturaItemJson = new FacturaItemJson();
			rubroCobroJson = new RubroCobroJson();
			facturaItemTipoJson = new FacturaItemTipoJson();
			cuotasJson = new CuotaJson(); 
			grupoJson = new GrupoJson();
			cuotaJson = new CuotaJson();
			personaJson = new PersonaJson();
			inscripcionesJson = new InscripcionesJson();
			domicilioJson = new DomicilioJson();
			partidaJson = new PartidaJson();
			tipoPagoJson = new TipoPagoJson();
			personasBecaJson = new PersonasBecaJson();
		}catch (Throwable ex) {
		    ex.printStackTrace(System.out);
		    throw new ExceptionInInitializerError(ex);
		}
	}
	
	
}
