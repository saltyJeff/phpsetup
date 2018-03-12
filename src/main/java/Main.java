import javax.swing.*;
import java.awt.*;

public class Main {
	public static CardLayout router;
	public static JPanel routerPanel;
	public static ConfigPanel configPanel;
	public static SetupPanel setupPanel;
	public static RunPanel runPanel;
	public enum Pages {
		CONFIG,
		SETUP,
		RUN
	}
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("PHP Setup Wizard");
		router = new CardLayout();
		routerPanel = new JPanel(router);

		routerPanel.add(new JScrollPane(configPanel = new ConfigPanel()), Pages.CONFIG.toString());
		routerPanel.add(setupPanel = new SetupPanel(), Pages.SETUP.toString());
		routerPanel.add(runPanel = new RunPanel(), Pages.RUN.toString());

		frame.add(routerPanel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
