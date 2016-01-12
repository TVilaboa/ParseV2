package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author Nicolas Burroni
 * @since 7/4/2014
 */
public class CandidateClassFrame extends JInternalFrame {

	private JPanel attributesPanel, methodsPanel;
	private JPopupMenu popup;

	public CandidateClassFrame(String className, List<String> attributes, List<String> methods, List<String> methodBodies, ActionListener methodButtonListener,
	                           /*ActionListener renameListener, ActionListener deleteListener, ActionListener commentListener,*/ ActionListener rankingListener){
		super(className, true, false, false, true);
		setFrameIcon(null);
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		attributesPanel = new JPanel();
		methodsPanel = new JPanel();
		attributesPanel.setOpaque(false);
		methodsPanel.setOpaque(false);
		attributesPanel.setLayout(new BoxLayout(attributesPanel, BoxLayout.Y_AXIS));
		methodsPanel.setLayout(new BoxLayout(methodsPanel, BoxLayout.Y_AXIS));
		//Add attributes and methods
		for (String attribute : attributes) {
			attributesPanel.add(new JLabel(attribute));
		}
		attributesPanel.add(Box.createVerticalStrut(3));
		attributesPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		for (int i = 0; i < methods.size(); i++) {
			methodsPanel.add(new MethodButton(methods.get(i), methodBodies.get(i), methodButtonListener));
		}
		//Set up popup menu
		//TODO remane, delete, comments, ccd ranking (boton derecho, submenu color ranking y que cambie el color del frame)
		popup = new JPopupMenu();
		JMenuItem renameItem = new JMenuItem(LanguageManager.getString("rename"));
//	TODO	renameItem.addActionListener(renameListener);
		JMenuItem deleteItem = new JMenuItem(LanguageManager.getString("delete"));
//	TODO	deleteItem.addActionListener(deleteListener);
		JMenuItem commentItem = new JMenuItem(LanguageManager.getString("comment"));
//	TODO	commentItem.addActionListener(commentListener);
		JMenu rankingMenu = new JMenu(LanguageManager.getString("ranking"));
		for (int i = 0; i < 4; i++) {
			JMenuItem rank = new JMenuItem(String.valueOf(i));
			rank.addActionListener(rankingListener);
			rank.addActionListener(this::changeColor);
			rankingMenu.add(rank);
		}
		popup.add(renameItem);
		popup.add(deleteItem);
		popup.add(commentItem);
		popup.add(new JSeparator(SwingConstants.HORIZONTAL));
		popup.add(rankingMenu);
		addMouseListener(new PopupListener());
		//
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(attributesPanel, constraints);
		constraints.gridy = 1;
		add(methodsPanel, constraints);
		setBackground(Color.WHITE);
		setMinimumSize(new Dimension(150, 150));
		setSize(200, 250);
	}

	private void changeColor(ActionEvent event){
		int rank = Integer.parseInt(((JMenuItem) event.getSource()).getText());
		switch(rank){
			case 0: setBackground(Color.WHITE);
				break;
			case 1: setBackground(Color.GREEN);
				break;
			case 2: setBackground(Color.YELLOW);
				break;
			case 3: setBackground(Color.RED);
				break;
			default: break;
		}
		validate();
	}

	public class MethodButton extends JButton {

		private String methodBody;

		public MethodButton(String methodName, String methodBody, ActionListener listener) {
			super(methodName);
			this.methodBody = methodName + "\n" + methodBody;
			addActionListener(listener);
			setContentAreaFilled(false);
			setBorderPainted(false);
		}

		public void showMethodBodyFrame(){
			JOptionPane.showMessageDialog(new JFrame(), methodBody, LanguageManager.getString("methodBody"), JOptionPane.PLAIN_MESSAGE);
		}

	}

	protected class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(),
						e.getX(), e.getY());
			}
		}
	}

}
