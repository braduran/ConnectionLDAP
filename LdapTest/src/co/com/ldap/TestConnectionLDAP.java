package co.com.ldap;

import java.util.Hashtable;

import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class TestConnectionLDAP {
	
	public static void main(String[] args) {
		getConnection("riemann", "password");
		getConnection("einstein", "passwor");
	}
	
	public static void getConnection(String user, String pass) {
		DirContext dirContext = null;
		
		Hashtable<Object, Object> params = new Hashtable<Object, Object>();
		params.put(DirContext.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		params.put(DirContext.PROVIDER_URL, "ldap://ldap.forumsys.com:389/cn=read-only-admin,dc=example,dc=com");
		params.put(DirContext.SECURITY_AUTHENTICATION, "simple");
		params.put(DirContext.SECURITY_PRINCIPAL, "uid="+ user + ",dc=example,dc=com");
		params.put(DirContext.SECURITY_CREDENTIALS, pass);
		
		try {
			dirContext = new InitialDirContext(params);
			dirContext.addToEnvironment("java.naming.referral", "follow");	
			System.out.println("Usuario conectado...");
		} catch (NamingException ne) {
			System.out.println("Fallo conexión");
			ne.printStackTrace();
		}finally {
			try {
				dirContext.close();
			} catch (NamingException ne) {
				ne.printStackTrace();
			}
		}
	}

}
