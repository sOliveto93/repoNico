package cuotas.model.service;

 public class CuotasServletUtils {
	
	private static CuotasServletUtils instancia=null;
	
	private CuotasServletUtils(){}
	public static CuotasServletUtils getInstance(){		
		if(instancia==null) instancia = new CuotasServletUtils();
		return instancia;
	}
	
	public static final String OPERACION="operacion";
	public static final String ALTA="alta";
	public static final String GRABAR="grabar";
	public static final String LISTAR="listar";
	public static final String BUSCAR="buscar";
	public static final String BORRAR="borrar";
	public static final String GRAFICAR="graficar";
	public static final String ACTUALIZAR="actualizar";
	public static final String BUSCADO="buscado";
	
	public static final String NOMBRE="nombre";
	public static final String APELLIDO="apellido";
	public static final String TELEFONO="telefono";
	public static final String CELULAR="celular";
	public static final String DOCUMENTO="documento";
	public static final String SEXO="sexo";
	public static final String LOCALIDAD="localidad";
	
	public static final String BORRAR_ALUMNO="borrar_alumno";
	public static final String BORRAR_CURSADA="borrar_cursada";
	
	public static final String MENSAJE="msg";
	public static final String MENSAJE_ERROR="msgError";
	public static final String POPUP="popup";
	public static final String LEER="leer";
	/**
	 * Usuado para mantener en la sesion el usuario logueado
	 */
	public static final String USUARIO="usuario";

	public static final String SALTO_DEFAULT ="/index.jsp";
	public static final String CARRERA="carrera";
	public static final String SALTO_CARRERA_LISTAR="/carrera/Carrera.jsp";
	public static final String SALTO_RUBROCOBRO_LISTAR="/factura/FacturaRubroCobro.jsp";
	public static final String SALTO_FACTURAITEMTIPO_LISTAR="/factura/FacturaItemTipo.jsp";
	public static final String SALTO_TIPO_PAGO = "/factura/TipoPago.jsp";
	public static final String SALTO_CARRERA_CARGAR="/Cursos/Cursada/cargar.jsp";
	//TODO - a la variable de abajo no se le puede asignar directamente "SALTO_CARRERA_LISTAR"
	public static final String SALTO_CARRERA_BUSCAR="/carrera/Carrera.jsp";
	public static final String SALTO_BECA_LISTAR="persona/Beca.jsp";

	public static final String SALTO_PARTIDA_LISTAR="/factura/Partida.jsp";
	
	public static final String SALTO_GRUPO_LISTAR="/grupo/Grupo.jsp";
	public static final String SALTO_GRUPO_LISTAR_DEP="/grupo/GrupoDEP.jsp";
	public static final String SALTO_GRUPO_LISTAR_DEP_ADMIN="/grupo/GrupoDEPadmin.jsp";
	
	public static final String SALTO_GRUPO_CUOTAS_LISTAR="/grupo/GrupoCuotas.jsp";
	public static final String SALTO_PERSONA_LISTAR="/persona/Personas.jsp";
	public static final String SALTO_PERSONA_LISTAR_DEP_DRYC="/persona/PersonaDepDryc.jsp";
	public static final String SALTO_PERSONA_MANTENIMIENTO="/persona/PersonaMantenimiento.jsp";
	public static final String SALTO_GENERAR_PAGO_PERSONA="/persona/GenerarPagoPersona.jsp";
	public static final String SALTO_PERSONA_MANTENIMIENTO_DEP_DRY="/persona/PersonaMantenimientoDepDryc.jsp";
			//"/persona/PersonaDatosAcademicosyPersonalesDepDryc.jsp";
	public static final String SALTO_PERSONA_MORA ="/persona/PersonaCuotasMora.jsp";
	public static final String SALTO_CARRERAS_DEP_LISTAR="/carrera/CarrerasDepCuotas.jsp";
	
	public static final String SALTO_BUSCAR_PERSONAS_CON_CHEQUERA_DEP="/chequera/chequeraPorPersonaDEP.jsp";
	
	public static final String SALTO_INFORME_PAGO="/persona/InformesPago.jsp";
	
	public static final String SALTO_CHEQUERA_INGRESO="/chequera/chequera.jsp";
	
	public static final String SALTO_FACTURA_LISTAR="/factura/FacturaFiltro.jsp";
	public static final String SALTO_FACTURA_MODIF_NUMERO="/factura/facturaModificarNumeracion.jsp";
	public static final String SALTO_FACTURA_ALTA="/factura/Factura.jsp";
	public static final String SALTO_FACTURA_IMPRIMIR="/factura/FacturaImprimir.jsp";
	public static final String SALTO_FACTURA_IMPRIMIR_A4="/factura/FacturaImprimir_A4.jsp";
	
	public static final String SALTO_REPORTES="/administracion/reporte.jsp";
	public static final String SALTO_INFORMES_FACTURA="/factura/InformesFactura.jsp";
	public static final String SALTO_INFORMES_CARRERA="/carrera/InformesCarrera.jsp";
	public static final String SALTO_UNIFICAR_PERSONA_LISTAR="/persona/unificarPersona.jsp";
	
 }
 
