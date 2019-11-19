package xyz.biandeshen.commonstests.util.common;

import org.bouncycastle.openpgp.PGPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchProviderException;

/**
 * @author fjp
 * @Title: PGPUtils
 * @ProjectName icp
 * @Description: PGP工具类
 * @date 2018/12/222:05
 */
//@Component
public class PGPUtils {
	@Value("${zjs.pgp.privateKeyPath}")
	private String privateKeyPath;
	
	@Value("${zjs.pgp.publicKeyPath}")
	private String publicKeyPath;
	
	
	private static PGPUtils INSTANCE = null;
	
	@Bean
	@Autowired
	public static PGPUtils getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PGPUtils();
		}
		return INSTANCE;
	}
	
	
	@Autowired
	private PGPUtils() {
	
	}
	
	/**
	 * 获取公钥
	 *
	 * @return 公钥路径
	 */
	private InputStream getKEY(String keyPath) {
		/**
		 * 读取jar包中的资源文件
		 * 1. 如果用getResource获取，则在打包后，由于获取的文件路径是file:开头的路径
		 * File file = new File("file:...")的形式的文件无法解析，所以会抛异常
		 * 2. 网上查询，通过使用getResourceAsStream的形式，直接获取文件流，这样就不会产生
		 * 打包后文件路径格式错误，无法获取文件的问题，而是直接
		 */
		return PGPUtils.class.getClassLoader().getResourceAsStream(keyPath);
	}
	
	/**
	 * 获取公钥
	 *
	 * @return 公钥路径
	 */
	public InputStream getPrivateKey() {
		/**
		 * 读取jar包中的资源文件
		 * 1. 如果用getResource获取，则在打包后，由于获取的文件路径是file:开头的路径
		 * File file = new File("file:...")的形式的文件无法解析，所以会抛异常
		 * 2. 网上查询，通过使用getResourceAsStream的形式，直接获取文件流，这样就不会产生
		 * 打包后文件路径格式错误，无法获取文件的问题，而是直接
		 */
		return PGPUtils.class.getClassLoader().getResourceAsStream(privateKeyPath);
	}
	
	/**
	 * 获取公钥
	 *
	 * @return 公钥路径
	 */
	public InputStream getPublicKey() {
		/**
		 * 读取jar包中的资源文件
		 * 1. 如果用getResource获取，则在打包后，由于获取的文件路径是file:开头的路径
		 * File file = new File("file:...")的形式的文件无法解析，所以会抛异常
		 * 2. 网上查询，通过使用getResourceAsStream的形式，直接获取文件流，这样就不会产生
		 * 打包后文件路径格式错误，无法获取文件的问题，而是直接
		 */
		return PGPUtils.class.getClassLoader().getResourceAsStream(publicKeyPath);
	}
	
	private boolean isArmored = false;
	private boolean integrityCheck = true;
	
	/**
	 * 加密
	 *
	 * @param pubKeyFile
	 * 		公钥(路径)
	 * @param cipherTextFile
	 * 		加密文本路径
	 * @param plainTextFile
	 * 		纯文本路径
	 *
	 * @throws NoSuchProviderException
	 * @throws IOException
	 * @throws PGPException
	 */
	public void encrypt(String pubKeyFile, String plainTextFile, String cipherTextFile) throws NoSuchProviderException, IOException, PGPException {
		try (FileInputStream pubKeyIs = new FileInputStream(pubKeyFile)) {
			try (FileOutputStream cipheredFileIs = new FileOutputStream(cipherTextFile)) {
				PgpHelper.getInstance().encryptFile(cipheredFileIs, plainTextFile, PgpHelper.getInstance().readPublicKey(pubKeyIs), isArmored, integrityCheck);
			}
		}
	}
	
	/**
	 * 加密
	 *
	 * @param plainTextFile
	 * 		纯文本路径
	 * @param cipherTextFile
	 * 		加密文本路径
	 *
	 * @throws NoSuchProviderException
	 * @throws IOException
	 * @throws PGPException
	 */
	public void encrypt(String plainTextFile, String cipherTextFile) throws NoSuchProviderException, IOException, PGPException {
		InputStream key = getPublicKey();
		try (FileOutputStream cipheredFileIs = new FileOutputStream(cipherTextFile)) {
			PgpHelper.getInstance().encryptFile(cipheredFileIs, plainTextFile, PgpHelper.getInstance().readPublicKey(key), isArmored, integrityCheck);
		}
	}
	
	/**
	 * 解密
	 *
	 * @param cipherTextFile
	 * 		加密文本路径
	 * @param privKeyFile
	 * 		私钥
	 * @param decPlainTextFile
	 * 		解密文本路径
	 * @param passwd
	 * 		密码
	 *
	 * @throws Exception
	 */
	public void decrypt(String cipherTextFile, String privKeyFile, String decPlainTextFile, String passwd) throws Exception {
		try (FileInputStream cipheredFileIs = new FileInputStream(cipherTextFile)) {
			try (FileInputStream privKeyIn = new FileInputStream(privKeyFile)) {
				try (FileOutputStream plainTextFileIs = new FileOutputStream(decPlainTextFile)) {
					PgpHelper.getInstance().decryptFile(cipheredFileIs, plainTextFileIs, privKeyIn, passwd.toCharArray());
				}
			}
		}
	}
	
	/**
	 * 解密
	 *
	 * @param cipherTextFile
	 * 		加密文本路径
	 * @param decPlainTextFile
	 * 		解密文本路径
	 * @param passwd
	 * 		密码
	 *
	 * @throws Exception
	 */
	public void decrypt(String cipherTextFile, String decPlainTextFile, String passwd) throws Exception {
		InputStream privKeyIn = getPrivateKey();
		try (FileInputStream cipheredFileIs = new FileInputStream(cipherTextFile)) {
			try (FileOutputStream plainTextFileIs = new FileOutputStream(decPlainTextFile)) {
				PgpHelper.getInstance().decryptFile(cipheredFileIs, plainTextFileIs, privKeyIn, passwd.toCharArray());
			}
		}
	}
	
	
	/**
	 * 签名校验
	 *
	 * @param privKeyFile
	 * 		私钥
	 * @param pubKeyFile
	 * 		公钥
	 * @param plainTextFile
	 * 		纯文本
	 * @param signatureFile
	 * 		签名文件
	 * @param passwd
	 * 		密码
	 *
	 * @throws Exception
	 */
	public void signAndVerify(String privKeyFile, String pubKeyFile, String plainTextFile, String signatureFile, String passwd) throws Exception {
		try (FileInputStream privKeyIn = new FileInputStream(privKeyFile)) {
			try (FileInputStream pubKeyIs = new FileInputStream(pubKeyFile)) {
				//FileInputStream plainTextInput = new FileInputStream(plainTextFile);
				FileOutputStream signatureOut = new FileOutputStream(signatureFile);
				
				//byte[] bIn = PgpHelper.getInstance().inputStreamToByteArray(plainTextInput);
				byte[] sig = PgpHelper.getInstance().createSignature(plainTextFile, privKeyIn, signatureOut, passwd.toCharArray(), true);
				PgpHelper.getInstance().verifySignature(plainTextFile, sig, pubKeyIs);
				signatureOut.close();
			}
		}
	}
	
}
