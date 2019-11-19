package xyz.biandeshen.commonstests.util.common;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.io.*;

/**
 * ftp连接工具
 *
 * @Author: Lic
 * @create: 2018/10/17 16:28
 * @update: Fjp
 * @updateTime: 2018-11-22 16:27
 */
public class FtpUtil {
	
	private static FtpUtil INSTANCE = null;
	
	@Bean
	@Autowired
	public static FtpUtil getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FtpUtil();
		}
		return INSTANCE;
	}
	
	private FtpUtil() {
	
	}
	
	/**
	 * 默认FTPClient
	 */
	private FTPClient ftpClient;
	/**
	 * 本地字符编码
	 */
	private String LOCAL_CHARSET = "GBK";
	
	/**
	 * FTP协议里面，规定文件名编码为iso-8859-1
	 */
	private final static String SERVER_CHARSET = "ISO-8859-1";
	
	/**
	 * ftp地址
	 */
	private String hostname;
	
	/**
	 * ip端口
	 */
	private String port;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 文件在ftp上的储存地址
	 */
	private String url;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 初始化FTP
	 * <p>必须先初始化，否则无法连接ftp</p>
	 * <p>或者利用自定义getCustomerConnect方法,</p>
	 * <p>在ftpClient中自定义连接从而不用初始化，</p>
	 * <p>但不能保证此次连接的可靠性</p>
	 *
	 * @param hostname
	 * 		ip地址
	 * @param port
	 * 		端口号
	 * @param url
	 * 		ftp路径
	 * @param username
	 * 		用户名
	 * @param password
	 * 		密码
	 */
	public FtpUtil init(String hostname, String port, String url, String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.url = url;
		this.username = username;
		this.password = password;
		this.ftpClient = new FTPClient();
		return getInstance();
	}
	
	public boolean getConnect() throws IOException {
		return getConnect(ftpClient);
	}
	
	/**
	 * 连接ftp服务器
	 *
	 * @Author: Lic
	 * @create: 2018/10/17 16:30
	 * @update: Fjp
	 * @updateTime: 2018-11-22 16:27
	 */
	public Boolean getConnect(FTPClient ftpClient) throws IOException {
		this.ftpClient = ftpClient;
		try {
			//如果存在连接，先断开，再重新连接
			this.close();
			
			// 连接FTP服务器   服务器地址                IP 端口
			ftpClient.connect(hostname, Integer.parseInt(port));
			// 登录FTP服务器   用户名 密码
			if (ftpClient.login(username, password)) {
				// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
				if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
					LOCAL_CHARSET = "UTF-8";
				}
				// 解决中文乱码
				ftpClient.setControlEncoding(LOCAL_CHARSET);
				// 下面两行代码必须要，而且不能改变编码格式，否则不能正确下载中文文件
				FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
				conf.setServerLanguageCode("zh");
				//设置传输超时（30分钟）
				ftpClient.setDataTimeout(1000 * 60 * 30);
				
				// 验证FTP服务器是否登录成功   ftp客户端代码
				int replyCode = ftpClient.getReplyCode();
				if (!FTPReply.isPositiveCompletion(replyCode)) {
					return false;
				}
				//文件类型为二进制文件
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				
				//工作路径切换
				if (!ftpClient.changeWorkingDirectory(url)) {
					//创建文件目录
					if (!ftpClient.makeDirectory(url)) {
						return false;
					} else {
						ftpClient.changeWorkingDirectory(url);
					}
				}
			}
		} catch (IOException e) {
			throw new IOException("获取FTP服务器连接失败！", e);
		}
		return true;
	}
	
	/**
	 * 自定义的ftpClient连接ftp
	 * <p>自己对ftpClient进行设置</p>
	 * <p>如以下等内容：</p>
	 * <p>1.连接ftp服务器</p>
	 * <p>2.登录ftp服务器</p>
	 * <p>3.切换工作路径</p>
	 *
	 * @param ftpClient
	 * 		自定义ftpClient
	 */
	public boolean getCustomerConnect(FTPClient ftpClient) throws IOException {
		boolean isConnect = false;
		this.ftpClient = ftpClient;
		if (!ftpClient.isConnected()) {
			// 连接FTP服务器   服务器地址                IP 端口
			ftpClient.connect(hostname, Integer.parseInt(port));
			// 登录FTP服务器   用户名 密码
			if (ftpClient.login(username, password)) {
				isConnect = true;
			}
		} else {
			isConnect = true;
		}
		return isConnect;
	}
	
	/**
	 * 上传文件
	 *
	 * @param fileName
	 * 		上传到ftp的文件名
	 * @param inputStream
	 * 		输入文件流
	 *
	 * @return
	 *
	 * @update: Fjp
	 * @updateTime: 2018-11-22 16:27
	 */
	public boolean uploadFile(String fileName, InputStream inputStream, FTPClient ftpClient) throws IOException {
		boolean isUploaded;
		try {
			//上传 默认主动模式，主动模式失败，则切换被动模式重试
			if (!ftpClient.storeFile(fileName, inputStream)) {
				//设置被动模式
				ftpClient.enterLocalPassiveMode();
				String filename = new String(fileName.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
				if (ftpClient.storeFile(filename, inputStream)) {
					isUploaded = true;
				} else {
					isUploaded = false;
				}
			} else {
				isUploaded = true;
			}
		} catch (IOException e) {
			throw new IOException("上传文件发生异常！", e);
		}
		return isUploaded;
	}
	
	
	/**
	 * 下载文件
	 *
	 * @param pathname
	 * 		FTP服务器文件目录 *
	 * @param filename
	 * 		文件名称 *
	 * @param localPath
	 * 		下载后的文件路径 *
	 *
	 * @return boolean
	 *
	 * @Author: Lic
	 * @create: 2018/10/17 16:31
	 */
	public boolean downloadFile(String pathname, String filename, String localPath, FTPClient ftpClient) throws IOException {
		boolean isDownloaded;
		ftpClient.setControlEncoding("UTF-8");
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
		conf.setServerLanguageCode("zh");
		try {
			isDownloaded = getConnect(ftpClient);
			if (isDownloaded) {
				// 切换(定位)FTP目录
				ftpClient.changeWorkingDirectory(pathname);
				FTPFile[] ftpFiles = ftpClient.listFiles();
				for (FTPFile file : ftpFiles) {
					if (filename.equalsIgnoreCase(file.getName())) {
						File localFile = new File(localPath + File.separator + file.getName());
						try (OutputStream os = new FileOutputStream(localFile)) {
							//ftp服务器检索命名文件并将其写入给定的OutputStream中。
							ftpClient.retrieveFile(file.getName(), os);
						}
					}
				}
				ftpClient.logout();
				isDownloaded = true;
			}
		} catch (Exception e) {
			throw new IOException("FTP下载文件失败！", e);
		} finally {
			if (ftpClient.isConnected()) {
				ftpClient.logout();
			}
		}
		return isDownloaded;
	}
	
	/**
	 * 简单上传文件
	 * 初始化后调用即可 init
	 *
	 * @param pathWithFileName
	 * 		带文件名的路径
	 *
	 * @return
	 */
	public boolean simpleUploadFile(String pathWithFileName) throws IOException {
		boolean isUpload = false;
		if (StringUtils.isNoneEmpty(pathWithFileName)) {
			File file = new File(pathWithFileName);
			if (file.exists() && file.isFile()) {
				try (FileInputStream in = new FileInputStream(file)) {
					String filename = new String(file.getName().getBytes(LOCAL_CHARSET), SERVER_CHARSET);
					boolean isConnected = getConnect(ftpClient);
					if (isConnected) {
						isUpload = uploadFile(filename, in, ftpClient);
					}
				} finally {
					ftpClient.logout();
					if (ftpClient.isConnected()) {
						try {
							ftpClient.disconnect();
						} catch (IOException e) {
							throw new IOException("关闭FTP连接失败！", e);
						}
					}
				}
			}
		}
		return isUpload;
	}
	
	/**
	 * 关闭当前ftpClient连接
	 */
	public Boolean close() {
		boolean isClose = false;
		try {
			ftpClient.logout();
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
					isClose = true;
				} catch (IOException e) {
					throw new IOException("关闭FTP连接失败！", e);
				}
			}
		} catch (IOException e) {
			isClose = false;
		}
		return isClose;
	}
	
	/**
	 * Sets the ftpClient.
	 * 设置自己定义的FTPClient
	 * <p>
	 * <p>You can use getFtpClient() to get the value of ftpClient</p>
	 *
	 * @param ftpClient
	 * 		ftpClient
	 */
	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
}
