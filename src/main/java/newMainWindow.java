import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Rectangle;

public class newMainWindow extends JFrame{
	public newMainWindow() {
		
		MainPanel panel = new MainPanel();
		panel.setBounds(new Rectangle(0, 0, 400, 400));
		getContentPane().add(panel, BorderLayout.CENTER);
		setVisible(true);
		setBounds(new Rectangle(0, 0, 800, 800));
	}
	

}
