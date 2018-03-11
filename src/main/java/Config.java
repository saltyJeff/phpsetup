import java.io.File;
import java.net.URL;

public class Config {
	public static File installDir = new File("php").getAbsoluteFile();
	public static File wwwFolder = new File("public").getAbsoluteFile();
	public static int serverPort = 27345;
	public static URL downloadUrl;
	static {
		try {
			downloadUrl = new URL("https://windows.php.net/downloads/releases/php-7.2.3-nts-Win32-VC15-x64.zip");
		}
		catch(Exception e) {}
	}
}
