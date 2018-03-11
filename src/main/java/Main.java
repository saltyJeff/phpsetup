import javax.swing.*;
import java.awt.*;

public class Main {
	public static CardLayout router;
	public static JPanel routerPanel;
	public static ConfigPanel configPanel;
	public static SetupPanel setupPanel;
	public enum Pages {
		CONFIG,
		SETUP,
		RUN
	}
	public static void main(String[] args) {
		JFrame frame = new JFrame();

		router = new CardLayout();
		routerPanel = new JPanel(router);

		routerPanel.add(configPanel = new ConfigPanel(), Pages.CONFIG.toString());
		routerPanel.add(setupPanel = new SetupPanel(), Pages.SETUP.toString());

		frame.add(new JScrollPane(routerPanel));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
