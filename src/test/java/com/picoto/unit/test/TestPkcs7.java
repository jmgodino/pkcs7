package com.picoto.unit.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import com.picoto.test.util.Pkcs7Utils;

public class TestPkcs7 {

	static final String PATH = "/home/jmgodino/pkcs7";
	
	@Test
	void testSignatureDetachedDer() throws Exception {
		Pkcs7Utils.validateSignedData(getDatosFirmadosDetachedDer(), Pkcs7Utils.Tipo.DETACHED, Pkcs7Utils.Formato.DER, getMensaje());
	}

	@Test
	void testSignatureAttachedDer() throws Exception {
		Pkcs7Utils.validateSignedData(getDatosFirmadosAttachedDer(), Pkcs7Utils.Tipo.ATTACHED, Pkcs7Utils.Formato.DER, null);
	}

	@Test
	void testSignatureDetachedPem() throws Exception {
		Pkcs7Utils.validateSignedData(getDatosFirmadosDetachedPem(), Pkcs7Utils.Tipo.DETACHED, Pkcs7Utils.Formato.PEM, getMensaje());
	}

	@Test
	void testSignatureAttachedPem() throws Exception {
		Pkcs7Utils.validateSignedData(getDatosFirmadosAttachedPem(), Pkcs7Utils.Tipo.ATTACHED, Pkcs7Utils.Formato.PEM, null);
	}

	@Test
	void testSign() throws Exception {
		String msg = "Hola mundo";
		byte[] signedMsg = Pkcs7Utils.signData(msg.getBytes(), getCertificado(), getPrivateKey(), "picoto");
		Pkcs7Utils.validateSignedData(signedMsg, Pkcs7Utils.Tipo.ATTACHED, Pkcs7Utils.Formato.DER, null);
		Pkcs7Utils.validateSignedData(signedMsg, Pkcs7Utils.Tipo.DETACHED, Pkcs7Utils.Formato.DER, msg.getBytes());
	}

	private byte[] getCertificado() throws IOException {
		return readFile(PATH+"/paucel.crt");
	}

	private byte[] getDatosFirmadosDetachedDer() throws IOException {
		return readFile(PATH+"/texto_cms.pkcs7");
	}

	private byte[] getDatosFirmadosAttachedDer() throws IOException {
		return readFile(PATH+"/texto_smime.pkcs7");
	}

	private byte[] getDatosFirmadosDetachedPem() throws IOException {
		return readFile(PATH+"/texto_cms_pem.pkcs7");
	}

	private byte[] getDatosFirmadosAttachedPem() throws IOException {
		return readFile(PATH+"/texto_smime_pem.pkcs7");
	}

	private byte[] getMensaje() throws IOException {
		return readFile(PATH+"/texto.txt");
	}

	private byte[] getPrivateKey() throws IOException {
		return readFile(PATH+"/paucel.key");
	}
	
	private byte[] readFile(String name) throws IOException {
		InputStream is = new FileInputStream(name);
		byte[] data = IOUtils.toByteArray(is);
		is.close();
		return data;
	}
	
}
