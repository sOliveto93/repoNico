package cuotas.dataSource.repository.hibernate;

import common.dataSource.repository.ldap.UsuarioLdapDAO;
import cuotas.model.domain.datos.UsuarioCuotas;

public class FactoryDAO {
	
	public static final BecaDAO becaDAO;
	public static final CarreraDAO carreraDAO;
	public static final CarrerasDepDAO carrerasDepDAO;
	public static final CuotaDAO cuotaDAO;
	public static final EventoDAO eventoDAO;
	public static final FacturaDAO facturaDAO;
	public static final FacturaItemDAO facturaItemDAO;
	public static final FacturaItemTipoDAO	facturaItemTipoDAO; // FIXME - Se puede cambiar a CobroTipos.  
	public static final GuaraniDAO guaraniDAO;
	public static final GrupoDAO grupoDAO;
	public static final InscripcionDAO inscripcionDAO;
	public static final MapeoTipoDocumentoDAO mapeoTipoDocumentoDAO;
	public static final PagoDAO pagoDAO;
	public static final PartidaDAO partidaDAO;
	public static final PersonaDAO personaDAO;
	public static final Persona_AuxiliarDAO personaAuxiliarDAO;
	public static final RegistroPagoDAO registroPagoDAO;
	public static final RubroCobroDAO rubroCobroDAO;
	public static final SucursalCobroDAO sucursalCobroDAO;
	public static final TipoCuotaDAO tipoCuotaDAO;
	public static final TipoDocumentoDAO tipoDocumentoDAO;
	public static final TipoFacturaDAO tipoFacturaDAO ;
	public static final TipoImpresionDAO tipoImpresionDAO;
	public static final TipoPagoDAO tipoPagoDAO;
	public static final UsuarioDAO usuarioDAO;
	public static final UsuarioLdapDAO<UsuarioCuotas> usuarioLdapDAO;
		
	public static final FormaPagoDAO formaPagoDAO;
	public static final OrigenPagoDAO origenPagoDAO;
	public static final PersonasBecaDAO personasBecaDAO;
	public static final CodigosAnterioresDAO codigosAnterioresDAO;
	
	static {
        try {
        	becaDAO = new BecaDAO();
        	carreraDAO =new CarreraDAO();
        	carrerasDepDAO = new CarrerasDepDAO();
        	cuotaDAO =new CuotaDAO();
        	codigosAnterioresDAO = new CodigosAnterioresDAO();
        	eventoDAO = new EventoDAO();
        	facturaDAO =new FacturaDAO();
        	facturaItemDAO =new FacturaItemDAO();
        	facturaItemTipoDAO =new FacturaItemTipoDAO();
        	formaPagoDAO = new FormaPagoDAO();
        	grupoDAO =new GrupoDAO();
        	guaraniDAO = new GuaraniDAO();
        	inscripcionDAO =new InscripcionDAO();
        	mapeoTipoDocumentoDAO = new MapeoTipoDocumentoDAO();
        	origenPagoDAO = new OrigenPagoDAO();
        	pagoDAO =new PagoDAO();
        	partidaDAO = new PartidaDAO();
        	personaDAO =new PersonaDAO();
        	personaAuxiliarDAO = new Persona_AuxiliarDAO();
        	personasBecaDAO = new PersonasBecaDAO();        	
        	registroPagoDAO = new RegistroPagoDAO();
        	rubroCobroDAO =new RubroCobroDAO();
        	sucursalCobroDAO =new SucursalCobroDAO();
        	tipoCuotaDAO =new TipoCuotaDAO();
        	tipoDocumentoDAO = new TipoDocumentoDAO();
        	tipoImpresionDAO = new TipoImpresionDAO();
        	tipoFacturaDAO  = new TipoFacturaDAO();
        	tipoPagoDAO = new TipoPagoDAO();        	
        	usuarioDAO = new UsuarioDAO();        	
        	usuarioLdapDAO = new UsuarioLdapDAO<UsuarioCuotas>(); 
        	//LdapConfig.getInstance(getRealPath()+"/WEB-INF/ldap.cfg.xml");
       } catch(Throwable ex) {
        	ex.printStackTrace(System.out);
        	throw new ExceptionInInitializerError(ex);
        }
	}	
	
}
