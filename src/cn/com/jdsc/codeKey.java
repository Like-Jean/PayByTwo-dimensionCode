package cn.com.jdsc;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class codeKey {
	static RSAPublicKey pubKey;
	static RSAPrivateKey priKey;
	
	static void setRSAPublicKey(RSAPublicKey  pub){
		pubKey = pub;
	}
	
	static void setRSAPrivateKey(RSAPrivateKey  pri){
		priKey = pri;
	}
	
	static RSAPublicKey getRSAPublicKey(){
		return pubKey;
	}
	
	static RSAPrivateKey getRSAPrivateKey(){
		return priKey;
	}
}
