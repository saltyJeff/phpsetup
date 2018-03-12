import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.util.prefs.Preferences;

public class Config implements Serializable {
	private String installDir = "php";
	private String wwwFolder = "public";
	private int serverPort = 27345;
	private String downloadUrl = "https://windows.php.net/downloads/releases/php-7.2.3-nts-Win32-VC15-x64.zip";
	private boolean autoOpen = true;

	public String getInstallDir() {
		return installDir;
	}

	public void setInstallDir(String installDir) {
		this.installDir = installDir;
	}

	public String getWwwFolder() {
		return wwwFolder;
	}

	public void setWwwFolder(String wwwFolder) {
		this.wwwFolder = wwwFolder;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public boolean isAutoOpen() {
		return autoOpen;
	}

	public void setAutoOpen(boolean autoOpen) {
		this.autoOpen = autoOpen;
	}

	public static Config getConf() {
		return conf;
	}

	public static void setConf(Config conf) {
		Config.conf = conf;
	}

	private Config () { }
	private static Config conf = null;
	public static Config get() {
		if(conf != null) {
			return conf;
		}
		File prefs = new File("phpsetup.yaml");
		try {
			Yaml yaml = new Yaml();
			try (FileInputStream in = new FileInputStream(prefs)) {
				conf = yaml.loadAs(in, Config.class);
				return conf;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			conf = new Config();
			return conf;
		}
	}
	public void save() throws IOException {
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setPrettyFlow(true);
		Yaml yaml = new Yaml(options);
		FileWriter writer = new FileWriter("phpsetup.yaml");
		yaml.dump(this, writer);
	}
	public File installDir() {
		return new File(installDir);
	}
	public File wwwFolder() {
		return new File (wwwFolder);
	}
	public int serverPort() {
		return serverPort;
	}
	public URL downloadUrl() {
		try {
			return new URL(downloadUrl);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean autoOpen () {
		return autoOpen;
	}
	public void installDir(File f) {
		installDir = f.getAbsolutePath();
	}
	public void wwwFolder(File f) {
		wwwFolder = f.getAbsolutePath();
	}
	public void serverPort(int i) {
		serverPort = i;
	}
	public void downloadUrl(URL u) {
		downloadUrl = u.toString();
	}
	public void autoOpen(boolean b) {
		autoOpen = b;
	}
}
