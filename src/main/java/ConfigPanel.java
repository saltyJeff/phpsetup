import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ConfigPanel extends JPanel {
	private JTextField installDirInput, wwwFolderInput, downloadUrlInput;
	private JSpinner serverPortInput;
	public ConfigPanel() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(new JLabel("Install to Directory"));
		add(installDirInput = new JTextField());
		add(new JLabel("WWW Folder"));
		add(wwwFolderInput = new JTextField());
		add(new JLabel("Server Port"));
		add(serverPortInput = new JSpinner());
		add(new JLabel("Download URL"));
		add(downloadUrlInput = new JTextField());
		setBorder(new EmptyBorder(10, 20, 10, 20));

		installDirInput.setText(Config.installDir.getAbsolutePath());
		wwwFolderInput.setText(Config.wwwFolder.getAbsolutePath());
		downloadUrlInput.setText(Config.downloadUrl.toString());
		serverPortInput.setModel(new SpinnerNumberModel(Config.serverPort, 8000, 27999, 1));

		JButton nextButton = new JButton("Next");
		nextButton.addActionListener((ActionEvent e) -> {
			File installDir = new File(installDirInput.getText());
			File wwwFolder = new File(wwwFolderInput.getText());
			wwwFolder.mkdirs();

			try {
				Config.installDir = installDir;
				Config.wwwFolder = wwwFolder.getCanonicalFile();
			}
			catch(IOException ex) {
				JOptionPane.showMessageDialog(null, "You don't have permission to write to install dir or www folder");
				return;
			}
			Config.serverPort = (int) serverPortInput.getValue();
			try {
				Config.downloadUrl  = new URL(downloadUrlInput.getText());
			}
			catch(MalformedURLException ex) {
				JOptionPane.showMessageDialog(null, "The download URL is malformed");
				return;
			}
			Main.router.show(Main.routerPanel, Main.Pages.SETUP.toString());
			Main.setupPanel.init();
		});
		add(nextButton);
	}
}
