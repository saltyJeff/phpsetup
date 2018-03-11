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
			String phpExe = new File(Config.installDir, "php.exe").getCanonicalPath();
			String wwwFolder = Config.wwwFolder.getCanonicalPath();
			String addr = "localhost:"+Config.serverPort;
			String command = String.format("%s -S %s -t %s", phpExe, addr, wwwFolder);
			out.println("Executing the following command:");
			ProcessBuilder processBuilder = new ProcessBuilder()
					.command(phpExe, "-S", addr, "-t", wwwFolder);
			out.println(command);

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
			if(Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(new URI("http://"+addr));
				Desktop.getDesktop().open(Config.wwwFolder);
			}

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