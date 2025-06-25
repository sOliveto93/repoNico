package cuotas.model.domain.datos;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import common.model.domain.datos.GenericBean;

public class Partida extends GenericBean<Integer> {

	private static final long serialVersionUID = 3641662319170819845L;
	
	private String descripcion;
	private Set<Grupo> grupos = new HashSet<Grupo>(0);
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Set<Grupo> getGrupos() {
		return grupos;
	}
	public void setGrupos(Set<Grupo> grupos) {
		this.grupos = grupos;
	}
	public Iterator<Grupo> getGruposIterator(){
		return this.grupos.iterator();
	}	
	public void addGrupo(Grupo grupo){
		if(!this.grupos.contains(grupo)){
			this.grupos.add(grupo);
		}
	}
	public void removeGrupo(Grupo grupo){
		if(this.grupos.contains(grupo)){
			this.grupos.remove(grupo);
		}
	}
}
