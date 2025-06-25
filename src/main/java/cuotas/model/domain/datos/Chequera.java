package cuotas.model.domain.datos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import cuotas.dataSource.repository.hibernate.FactoryDAO;

public class Chequera {
	Persona persona;
	Grupo grupo;
	List<Cheque> cheques;
	public Chequera(Persona persona,Grupo grupo, List<Pago> pagos, boolean forzado, int numero, boolean soloImprimir, String[] meses, String[] meses2,String[] tipo, boolean desdePersona){
		this.persona = persona;
		this.grupo = grupo;
		generarCheques(pagos, forzado,numero, soloImprimir, meses, meses2, tipo, desdePersona);
		
	}
	
	
	private void generarCheques(List<Pago> pagos, boolean forzado, int numero, boolean soloImprimir, String[] mes,String[] mes2, String[] tipo, boolean desdePersona){
		Cuota temp;
		this.cheques = new ArrayList<Cheque>();
		Iterator<Pago> ps = pagos.iterator();
		//uso para controlar que meses tienen pago ya cargado y en cuales hay que generarlo.
		boolean meses[] = {false,false,false,false,false,false,false,false,false,false,false,false};
		// Para los casos en que puede tener dos carreras en un mismo grupo, uso dos arreglos de pago
		Pago aux_pagos[] = {null,null,null,null,null,null,null,null,null,null,null,null};
		Pago aux_pagos2[] = {null,null,null,null,null,null,null,null,null,null,null,null};
		Pago aux_pagosE[] = {null,null,null,null,null,null,null,null,null,null,null,null};
		Pago aux_pagos2E[] = {null,null,null,null,null,null,null,null,null,null,null,null};
		// El contenedor de las cuotas, que mantengo uno para cuotas E y otro para M
		Cuota aux_cuotas_M[] = {null,null,null,null,null,null,null,null,null,null,null,null};
		Cuota aux_cuotas_E[] = {null,null,null,null,null,null,null,null,null,null,null,null};
		boolean[] mes_a_imprimir ={false,false,false,false,false,false,false,false,false,false,false,false};
		boolean[] expcecion = {false,false,false,false,false,false,false,false,false,false,false,false};
		if(grupo.getCobraMora()!='S'){	
			for (int i = 0; i < expcecion.length;i++){
				expcecion[i] = true;
			}
		}
		 
		boolean tipo_e = false;
		boolean tipo_m = false;
		if(tipo == null){
			tipo_e = true;
			tipo_m = true;
		}else{
			if (ArrayUtils.contains( tipo, "E" )){
				tipo_e = true;
			}
			if (ArrayUtils.contains( tipo, "M" )){
				tipo_m = true;
			}
		}
		if(tipo_e){
			System.out.println("Selecciono tipo E");
		}else{
			System.out.println("No selecciono tipo E");
		}
		if(tipo_m){
			System.out.println("Selecciono tipo M");
		}else{
			System.out.println("No selecciono tipo M");
		}
		if(mes!=null){			
			for(int i= 0; i<mes.length; i++){
				mes_a_imprimir[new Integer(mes[i])] = true;								
			}
		}
		if(mes2!=null){			
			for(int i= 0; i<mes2.length; i++){
				expcecion[new Integer(mes2[i])] = true;
								
			}
		}
		Pago pago;
		//para los pagos que tiene armo las chequeras
		if(!pagos.isEmpty()){
			while (ps.hasNext()){
				pago = ps.next();		

				if(pago.getTipo().equals("E")){
					if(aux_pagosE[pago.getFecha().getMes()]!=null){
						aux_pagos2E[pago.getFecha().getMes()] = pago;
					}else{
						meses[pago.getFecha().getMes()] = true;
						aux_pagosE[pago.getFecha().getMes()] = pago;
					}
				}else{
					if(aux_pagos[pago.getFecha().getMes()]!=null){
						aux_pagos2[pago.getFecha().getMes()] = pago;
					}else{
						meses[pago.getFecha().getMes()] = true;
						aux_pagos[pago.getFecha().getMes()] = pago;
					}
				}				
			}
		}
		//luego agrego las cuotas que no estan ya generadas en esa persona
		if(this.grupo != null && this.grupo.getCuotas() != null){
			Iterator<Cuota> coutas = this.grupo.getCuotas().iterator();
			int i = 0;
			while(coutas.hasNext()){				
				temp = coutas.next();
				i++;
				//System.out.println(i+"Datos de las cuotas"+temp.getMes().getNombre()+" - "+temp.getTipo().getLetra());
				String letra = temp.getTipo().getLetra()+"";
				if(letra.equals("M")){
					if(soloImprimir){
						aux_cuotas_M[temp.getMes().getNumero()-1] = null;
					}else{
						aux_cuotas_M[temp.getMes().getNumero()-1] = temp;
					}
				}else{
					if(soloImprimir){
						aux_cuotas_E[temp.getMes().getNumero()-1] = null;
					}else{
						aux_cuotas_E[temp.getMes().getNumero()-1] = temp;
					}
				}
			}
		}
		int cont = numero;
		for (int i = 0; i<meses.length; i++){
			if(mes==null || mes_a_imprimir[i]){
				if(aux_cuotas_M[i]!=null || aux_cuotas_E[i]!=null){
					//tener en cuenta de buscar tanto los pagos M como los pagos E, y que no sean el mismo (igual que hice con las cuotas)......
					if(aux_cuotas_M[i]!=null && tipo_m){	
						System.out.println("Entro en M");
						if(aux_pagos[i]!=null){
							if(aux_pagos[i].getFechaPgo()==null){
								if(aux_pagos[i].getEstado().isActivo() || forzado){
									Cheque c = new Cheque(this,aux_pagos[i],aux_cuotas_M[i], forzado,++cont,expcecion[i],"M",desdePersona);
									cheques.add(c);
									//System.out.println("El cheque es: "+c.getFechaSting()+" "+c.getCuota().getTipo()+" "+c.getCodigo());
									//System.out.println("El pago es: "+c.getPago_m().getCodigo_barra());
								}
							}
							if(aux_pagos2[i]!=null && aux_pagos2[i].getFechaPgo()==null){
								if(aux_pagos2[i].getEstado().isActivo() || forzado){
									cheques.add(new Cheque(this,aux_pagos2[i],aux_cuotas_M[i], forzado,++cont,expcecion[i],"M",desdePersona));
								}
								
							}
						}else{
							Cheque c = new Cheque(this,null,aux_cuotas_M[i],true,++cont,expcecion[i],"M",desdePersona);
							cheques.add(c);
							//System.out.println("El cheque es: "+c.getFechaSting()+" "+c.getCuota().getTipo()+" "+c.getCodigo());
							//System.out.println("El pago es: "+c.getPago_m().getCodigo_barra());
						}
					}
					if(aux_cuotas_E[i]!=null && tipo_e){
						System.out.println("Entro en E");
						if(aux_pagosE[i]!=null){
							if(aux_pagosE[i].getFechaPgo()==null){		
								if(aux_pagosE[i].getEstado().isActivo() || forzado){
									cheques.add(new Cheque(this,aux_pagosE[i],aux_cuotas_E[i], forzado,++cont,expcecion[i],"E",desdePersona));
								}
							}
							if(aux_pagos2E[i]!=null && aux_pagos2E[i].getFechaPgo()==null){
								if(aux_pagos2E[i].getEstado().isActivo() || forzado){
									cheques.add(new Cheque(this,aux_pagos2E[i],aux_cuotas_E[i], forzado,++cont,expcecion[i],"E",desdePersona));
								}
							}
						}else{
							Cheque c = new Cheque(this,null,aux_cuotas_E[i],true,++cont,expcecion[i],"E",desdePersona);
							cheques.add(c);
							//System.out.println("El cheque es: "+c.getFechaSting()+" "+c.getCuota().getTipo()+" "+c.getCodigo());
							//System.out.println("El pago es: "+c.getPago_m().getCodigo_barra());
						}
					}
				}else{
					if(aux_pagos[i]!=null){
						if(forzado){
							if(aux_pagos[i].getFechaPgo()==null){
								FactoryDAO.pagoDAO.delete(aux_pagos[i]);
							}
						}else{
							if(aux_pagos[i].getFechaPgo()==null && aux_pagos[i].getEstado().isActivo()  && tipo_m){
								cheques.add(new Cheque(this,aux_pagos[i], null,false,++cont,expcecion[i],"M",desdePersona));
							}
						}
					}
					if(aux_pagos2[i]!=null){
						if(forzado){
							if(aux_pagos2[i].getFechaPgo()==null){
								FactoryDAO.pagoDAO.delete(aux_pagos2[i]);
							}
						}else{
							if(aux_pagos2[i].getFechaPgo()==null && aux_pagos2[i].getEstado().isActivo() && tipo_m){
								cheques.add(new Cheque(this,aux_pagos2[i], null,false,++cont,expcecion[i],"M",desdePersona));
							}
						}
					}
					if(aux_pagosE[i]!=null){
						if(forzado){
							if(aux_pagosE[i].getFechaPgo()==null){
								FactoryDAO.pagoDAO.delete(aux_pagosE[i]);
							}
						}else{
							if(aux_pagosE[i].getFechaPgo()==null && aux_pagosE[i].getEstado().isActivo() && tipo_e){
								cheques.add(new Cheque(this,aux_pagosE[i], null,false,++cont,expcecion[i],"E",desdePersona));
							}
						}
					}
					if(aux_pagos2E[i]!=null){
						if(forzado){
							if(aux_pagos2E[i].getFechaPgo()==null){
								FactoryDAO.pagoDAO.delete(aux_pagos2E[i]);
							}
						}else{
							if(aux_pagos2E[i].getFechaPgo()==null && aux_pagos2E[i].getEstado().isActivo() && tipo_e){
								cheques.add(new Cheque(this,aux_pagos2E[i], null,false,++cont,expcecion[i],"E",desdePersona));
							}
						}
					}
				}			
			}
		}
	}
	public List<Cheque> getCheques() {
		return cheques;
	}
	public void setCheques(List<Cheque> cheques) {
		this.cheques = cheques;
	}
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
}
