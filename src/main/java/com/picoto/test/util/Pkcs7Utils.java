package com.picoto.test.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;

public class Pkcs7Utils {

	public enum Formato {
		DER, PEM
	};

	public enum Tipo {
		ATTACHED, DETACHED
	};

	public static byte[] signData(byte[] data, byte[] signingCertificateData, byte[] signingKeyData, String passwd, Tipo tipo)
			throws Exception {

		byte[] signedMessage = null;
		X509Certificate signingCertificate = getCertificado(signingCertificateData);
		List<X509Certificate> certList = new ArrayList<X509Certificate>();
		CMSTypedData cmsData = new CMSProcessableByteArray(data);
		certList.add(signingCertificate);
		JcaCertStore certs = new JcaCertStore(certList);

		CMSSignedDataGenerator cmsGenerator = new CMSSignedDataGenerator();
		ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA")
				.build(getPrivateKey(signingKeyData, passwd));
		cmsGenerator.addSignerInfoGenerator(
				new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build())
						.build(contentSigner, signingCertificate));
		cmsGenerator.addCertificates(certs);

		boolean attach = tipo == Tipo.ATTACHED;
		CMSSignedData cms = cmsGenerator.generate(cmsData, attach);
		signedMessage = cms.getEncoded();
		return signedMessage;
	}

	private static X509Certificate getCertificado(byte[] cert) throws CertificateException, IOException {
		PEMParser pemParser = new PEMParser(new InputStreamReader(new ByteArrayInputStream(cert)));
		JcaX509CertificateConverter x509Converter = new JcaX509CertificateConverter();
		return x509Converter.getCertificate((X509CertificateHolder) pemParser.readObject());
	}

	private static PrivateKey getPrivateKey(byte[] signingKey, String passwd) throws IOException {
		PEMParser pemParser = new PEMParser(new InputStreamReader(new ByteArrayInputStream(signingKey)));
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
		PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemParser.readObject());
		return converter.getPrivateKey(privateKeyInfo);
	}

	public static void validateSignedData(byte[] signedMessage, Tipo tipo, Formato formato, byte[] message)
			throws Exception {
		if (tipo == Tipo.DETACHED) {
			validateSignedDataDetached(message, signedMessage, formato);
		} else {
			validateSignedDataAttached(signedMessage, formato);
		}
	}

	private static void validateSignedDataAttached(byte[] signedMessage, Formato formato) throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		CMSSignedData signedData = null;

		if (formato == Formato.DER) {
			signedData = new CMSSignedData(signedMessage);
		} else {
			signedData = new CMSSignedData(getPEMData(signedMessage));
		}

		validar(signedData);
	}

	private static void validateSignedDataDetached(byte[] message, byte[] signedMessage, Formato formato)
			throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		CMSSignedData signedData = null;

		if (formato == Formato.DER) {
			signedData = new CMSSignedData(new CMSProcessableByteArray(message), signedMessage);
		} else {
			signedData = new CMSSignedData(new CMSProcessableByteArray(message), getPEMData(signedMessage));
		}

		validar(signedData);
	}

	private static void validar(CMSSignedData signedData)
			throws OperatorCreationException, CertificateException, CMSException, IOException {
		Store<X509CertificateHolder> certStore = signedData.getCertificates();
		SignerInformationStore signers = signedData.getSignerInfos();
		Collection<SignerInformation> signersCollection = signers.getSigners();

		for (SignerInformation signer : signersCollection) {
			Collection<X509CertificateHolder> certCollection = certStore.getMatches(signer.getSID());
			for (X509CertificateHolder cert : certCollection) {
				debug("Firmante: " + cert.getSubject());
				if (signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert))) {
					debug("Firma correcta");
					// TODO: Ojo ahora validar el CERTIFICADO (cert.getEncoded);
					debug("No olvidarse de: Validar el certificado");
				}
			}
		}
	}

	private static ContentInfo getPEMData(byte[] datosFirmadosPem) throws IOException {
		PEMParser p = new PEMParser(new StringReader(new String(datosFirmadosPem)));
		return (ContentInfo) p.readObject();
	}

	private static void debug(String str) {
		System.out.println(str);
	}

}
