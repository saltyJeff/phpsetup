import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SetupPanel extends JPanel {
	PrintStream out;
	public SetupPanel() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JTextArea outputPanel = new JTextArea();
		outputPanel.setEditable(false);
		TextAreaOutputStream outputStream = new TextAreaOutputStream(outputPanel, 60);
		out = new PrintStream(outputStream);

		add(new JScrollPane(outputPanel));
	}

	public void init() {
		try {
			if (System.getenv("PATH").contains("php.exe")) {
				out.println("php.exe already found in the path");
				nextStep();
				return;
			}
			else {
				Config.get().installDir().mkdirs();
				Config.get().installDir(Config.get().installDir().getCanonicalFile());
				File phpExe = new File(Config.get().installDir(), "php.exe");
				out.println("Testing for an existing PHP installation at " + phpExe.toString());
				if (!phpExe.exists()) {
					File outFile = new File(Config.get().installDir(), "php_download.zip");
					if (!outFile.exists()) {
						out.println("Beginning download of php from " + Config.get().downloadUrl());
						out.println("Downloading... (Will take a while)");
						DownloadProgressBar download = new DownloadProgressBar(Config.get().downloadUrl(), outFile, () -> {
							out.println("Download complete");
							beginUnzip(outFile);
						});
						add(new JLabel("Downloading PHP"));
						add(download);
						download.beginDownload();
						this.repaint();
					}
					else {
						out.println("Previously downloaded zip of php found, using that");
						beginUnzip(outFile);
					}
				}
				else {
					out.println("Existing installation detected");
					nextStep();
					return;
				}
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	void beginUnzip(File zip) {
		out.println("Unzipping file");
		byte[] buffer = new byte[1024];
		try {
			File folder = Config.get().installDir();
			if (!folder.exists()) {
				folder.mkdir();
			}
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zip));
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				if(!ze.isDirectory()){
					String fileName = ze.getName();
					File newFile = new File(folder, fileName);
					out.println("Unzipping: "+fileName);
					new File(newFile.getParent()).mkdirs();
					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
				}
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
			out.println("Unzip complete");
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		nextStep();
	}

	void nextStep() {
		try {
			out.println("Ensuring the www directory exists");
			if (!Config.get().wwwFolder().exists()) {
				out.println("Creating www directory");
				Config.get().wwwFolder().mkdirs();
				Config.get().wwwFolder(Config.get().wwwFolder().getCanonicalFile());
				out.println("Adding demo index.php");
				InputStream indexStream = getClass().getResourceAsStream("index.php");
				Files.copy(indexStream, new File(Config.get().wwwFolder(), "index.php").toPath());
				InputStream iniStream = getClass().getResourceAsStream("php.ini");
				Files.copy(iniStream, new File(Config.get().wwwFolder(), "php.ini").toPath());
			}
			else {
				out.println("Www directory found");
			}
			out.println("All set up! Click the next button to continue");
			JButton nxtButton = new JButton("Next");
			add(nxtButton);
			nxtButton.addActionListener((ActionEvent e) -> {
				Main.router.show(Main.routerPanel, Main.Pages.RUN.toString());
				Main.runPanel.init();
			});
			Main.frame.pack();
		}
		catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
