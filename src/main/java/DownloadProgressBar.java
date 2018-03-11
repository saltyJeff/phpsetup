import javax.print.DocFlavor;
import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadProgressBar extends JProgressBar {
	public interface OnComplete {
		public void onComplete();
	}

	private OnComplete onCmplt;
	private URL origin;
	private File dest;

	public DownloadProgressBar(URL origin, File dest, OnComplete cmplt) {
		setSize(150, 35);
		this.origin = origin;
		this.dest = dest;
		onCmplt = cmplt;
	}

	public void beginDownload() {
		new Thread(() -> {
			try {
				HttpURLConnection connection = (HttpURLConnection) origin.openConnection();
				int fileSize = connection.getContentLength();
				float alreadyRead = 0;
				BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
				FileOutputStream fos = new FileOutputStream(dest);
				BufferedOutputStream out = new BufferedOutputStream(fos, 1024);
				byte[] data = new byte[1024];
				int i = 0;
				while ((i = in.read(data, 0, 1024)) >= 0) {
					alreadyRead = alreadyRead + i;
					out.write(data, 0, i);
					float pct = (alreadyRead * 100) / fileSize;
					SwingUtilities.invokeLater(() -> {
						setValue((int) pct);
					});
				}
				out.close();
				in.close();
				onCmplt.onComplete();
			}
			catch (Exception e) {
				System.out.println(e);
			}
		}).start();
	}
}
