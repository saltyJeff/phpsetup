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
	private JCheckBox autoOpenInput;
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
		add(new JLabel("Automatically open explorer/browser when done"));
		add(autoOpenInput = new JCheckBox());
		autoOpenInput.setSelected(Config.get().autoOpen());
		setBorder(new EmptyBorder(10, 20, 10, 20));

		installDirInput.setText(Config.get().installDir().getAbsolutePath());
		wwwFolderInput.setText(Config.get().installDir().getAbsolutePath());
		downloadUrlInput.setText(Config.get().downloadUrl().toString());
		serverPortInput.setModel(new SpinnerNumberModel(Config.get().serverPort(), 8000, 27999, 1));

		JButton nextButton = new JButton("Next");
		nextButton.addActionListener((ActionEvent e) -> {
			Config.get().autoOpen(autoOpenInput.isSelected());
			File installDir = new File(installDirInput.getText());
			try {
				installDir.mkdirs();
				Config.get().installDir(installDir.getCanonicalFile());
			}
			catch(IOException ex) {
				JOptionPane.showMessageDialog(null, "You don't have permission to write to install dir or www folder");
				return;
			}
			Config.get().serverPort((int) serverPortInput.getValue());
			try {
				Config.get().downloadUrl(new URL(downloadUrlInput.getText()));
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
