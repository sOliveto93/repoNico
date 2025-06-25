package common.dataSource.repository.ldap;

import javax.naming.directory.Attributes;

public interface MappingStrategy<T> {

	public T map(LdapConfig ldap, Attributes atributos);
}
