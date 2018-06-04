package co.com.ldap;

import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class ConnectionLDAP {
	
	public static void main(String[] args) {
		DirContext ctxUser = getConnection("password");
		searchUser(ctxUser, "einstein");
		DirContext ctxGroup = getConnection("password");
		searchGruop(ctxGroup, "scientists");
	}
	
	public static DirContext getConnection(String pass) {
		DirContext dirContext = null;
		
		Hashtable<Object, Object> params = new Hashtable<Object, Object>();
		params.put(DirContext.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		params.put(DirContext.PROVIDER_URL, "ldap://ldap.forumsys.com:389");
		params.put(DirContext.SECURITY_AUTHENTICATION, "simple");
		params.put(DirContext.SECURITY_PROTOCOL, "none");
		params.put(DirContext.SECURITY_PRINCIPAL, "cn=read-only-admin,dc=example,dc=com");
		params.put(DirContext.SECURITY_CREDENTIALS, pass);
		
		try {
			dirContext = new InitialDirContext(params);
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
		return dirContext;
	}
	
	public static void searchUser(DirContext dirContext, String user) {
		NamingEnumeration<?> namingEnum = null;
		String name = "uid=" + user + ",dc=example,dc=com";
		String filter = "(objectClass=inetOrgPerson)";
		String []returnedAttributes = {"cn","sn","email"};
		
		SearchControls sc = new SearchControls();
		sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
		sc.setReturningAttributes(returnedAttributes);
		
		SearchResult sr = null;
		try {
			namingEnum = dirContext.search(name, filter, sc);		
			while(namingEnum.hasMore()) {
				sr = (SearchResult) namingEnum.next();
				Attributes attr =  sr.getAttributes();
				for (int i = 0; i < attr.size(); i++) {
					Attribute attrChild = attr.get(returnedAttributes[i]);
					System.out.println(attrChild);
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();		
		}finally {
			try {
				if(dirContext != null) {
					dirContext.close();
				}
				if(namingEnum != null) {
					namingEnum.close();
				}
			} catch (NamingException ne) {
				ne.printStackTrace();
			}
		}
	}

	public static void searchGruop(DirContext dirContext, String grupo) {
		NamingEnumeration<?> namingEnum = null;
		String name = "ou=" + grupo + ",dc=example,dc=com";
		String filter = "(objectClass=groupOfUniqueNames)";
		
		SearchControls sc = new SearchControls();
		sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
		
		SearchResult sr = null;
		try {
			namingEnum = dirContext.search(name, filter, sc);		
			while(namingEnum.hasMore()) {
				sr = (SearchResult) namingEnum.next();
				Attribute attr =  sr.getAttributes().get("uniqueMember");
				for (int i = 0; i < attr.size(); i++) {
					System.out.println(attr.get(i));
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();		
		}finally {
			try {
				if(dirContext != null) {
					dirContext.close();
				}
				if(namingEnum != null) {
					namingEnum.close();
				}
			} catch (NamingException ne) {
				ne.printStackTrace();
			}
		}
	}
}