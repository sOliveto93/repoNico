package common.model.domain.validacion;

import java.util.Iterator;

public class TestRoles {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// PRE-CARGA MANUAL DE VALORES
		UsuarioRol user1 = new UsuarioRol("pepePompin");
		UsuarioRol user2 = new UsuarioRol("carozo");
		
		UsuarioRol user3 = new UsuarioRol("Batman");
		
		Rol lectura = new Rol("LECTURA");
		lectura.setId(new Long(1));
		
		Rol escritura = new Rol("ESCRITURA");
		escritura.setId(new Long(2));
		
		
		GrupoRol grupoA = new GrupoRol("muniecos");
		grupoA.add(lectura);
		
		grupoA.add(user1);
		grupoA.add(user2);
		
		
		user3.add(lectura);
		user3.add(escritura);
		// - FIN PRE_CARGA
		
		// TODOS QUIEREN LEER
		Iterator<UsuarioRol> it = grupoA.getIntegrantes().iterator();
		while(it.hasNext()){
			UsuarioRol usuario = it.next();
			if(usuario.puedePonerseComo(lectura)){
				System.out.println(usuario.getUsuario()+" quiere asumir rol LECTURA y SI puede");
			}else{
				System.out.println(usuario.getUsuario()+" quiere asumir rol LECTURA y NO puede");
			}		
		}
		
		if(user3.puedePonerseComo(lectura)){
			System.out.println(user3.getUsuario()+" quiere asumir rol LECTURA y SI puede");
		}else{
			System.out.println(user3.getUsuario()+" quiere asumir rol LECTURA y NO puede");
		}		
		
		System.out.println("---");
		
		// TODOS QUIEREN ESCRIBIR
		
		Iterator<UsuarioRol> it2 = grupoA.getIntegrantes().iterator();
		while(it2.hasNext()){
			UsuarioRol usuario = it2.next();
			if(usuario.puedePonerseComo(escritura)){
				System.out.println(usuario.getUsuario()+" quiere asumir rol ESCRITURA y SI puede");
			}else{
				System.out.println(usuario.getUsuario()+" quiere asumir rol ESCRITURA y NO puede");
			}		
		}
		
		if(user3.puedePonerseComo(escritura)){
			System.out.println(user3.getUsuario()+" quiere asumir rol ESCRITURA y SI puede");
		}else{
			System.out.println(user3.getUsuario()+" quiere asumir rol ESCRITURA y NO puede");
		}	
		
	}

}
