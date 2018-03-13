import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URI;

public class RunPanel extends JPanel {
	private PrintStream out;
	private Process proc;
	public RunPanel() {
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
			String phpExe = new File(Config.get().installDir(), "php.exe").getCanonicalPath();
			String wwwFolder = Config.get().wwwFolder().getCanonicalPath();
			String addr = "localhost:"+Config.get().serverPort();
			out.println("Executing the following command:");
			ProcessBuilder processBuilder = new ProcessBuilder()
					.command(phpExe, "-S", addr, "-t", wwwFolder, "-c", new File(wwwFolder, "php.ini").getPath());
			out.println(String.join(" ", processBuilder.command()));
			proc = processBuilder.start();
			new StreamGobbler(proc.getErrorStream(), out).start();
			new StreamGobbler(proc.getInputStream(), out).start();

			JButton exitButton = new JButton("Stop the server and exit");
			exitButton.addActionListener((ActionEvent e) -> {
				proc.destroy();
				System.exit(0);
			});
			add(exitButton);

			//because double clicking is too hard
			if(Config.get().autoOpen() && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(new URI("http://"+addr));
				Desktop.getDesktop().open(Config.get().wwwFolder());
			}

			Config.get().save();
			Main.frame.pack();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
class StreamGobbler extends Thread {
	InputStream input;
	PrintStream output;
	// reads everything from is until empty.
	public StreamGobbler(InputStream from, PrintStream to) {
		input = from;
		output = to;
	}
	public void run() {
		try {
			InputStreamReader reader = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(reader);
			String line = null;
			while ((line = br.readLine()) != null) {
				output.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}