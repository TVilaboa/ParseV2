package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Nicolas Burroni
 * @since 5/27/2014
 */
public class VisualizerView extends JFrame{

	private JPanel root;
	private JTabbedPane legacyCodePane;
	private JDesktopPane classesPane;
	private JPanel rightPanel;
	private JPanel leftPanel;
	private JPanel tabsPanel;
	private JSplitPane splitPane;
	private int horizontalFrameCount, ccdFrameOffsetX, ccdFrameOffsetY;
	private ActionListener methodButtonListener, renameListener, deleteListener, commentListener, rankingListener;
	private final static int DEFAULT_OFFSET = 65;

	public VisualizerView(ActionListener methodButtonListener,
	                           /*ActionListener renameListener, ActionListener deleteListener, ActionListener commentListener,*/ ActionListener rankingListener,ActionListener browseCode,
                          ActionListener loadAction,ActionListener saveAction) {
		super();
		setContentPane(root);
		this.methodButtonListener = methodButtonListener;
		/*this.renameListener = renameListener;
		this.deleteListener = deleteListener;
		this.commentListener = commentListener;*/
		this.rankingListener = rankingListener;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int inset = 60;
		setBounds(inset, inset,
				screenSize.width  - inset*2,
				screenSize.height - inset*2);
		setMinimumSize(new Dimension(350, 300));
		setLocationRelativeTo(null);
		ccdFrameOffsetX = DEFAULT_OFFSET;
		ccdFrameOffsetY = 0;
		horizontalFrameCount = 0;
		createMenuBar(browseCode,loadAction,saveAction);
	}

	public void addCandidateClass(String className, List<String> attributes, List<String> methods, List<String> methodBodies){
		CandidateClassFrame ccdFrame = new CandidateClassFrame(className, attributes, methods, methodBodies, methodButtonListener, rankingListener);
		ccdFrame.setLocation(ccdFrameOffsetX*horizontalFrameCount, ccdFrameOffsetY);
		horizontalFrameCount++;
		if(horizontalFrameCount == 5) {
			horizontalFrameCount = 0;
			ccdFrameOffsetY += DEFAULT_OFFSET;
		}
		ccdFrame.setVisible(true);
		classesPane.add(ccdFrame);
		try {
			ccdFrame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {}
	}

	public void addLegacyCode(String fileName, String code){
		JTextArea legacyCodeArea = new JTextArea(code);
		legacyCodeArea.setMargin(new Insets(5, 5, 5, 5));
		legacyCodeArea.setEditable(false);
		legacyCodePane.addTab(fileName, new JScrollPane(legacyCodeArea));
	}

	private void createUIComponents() {
		classesPane = new JDesktopPane();
		legacyCodePane = new JTabbedPane(SwingConstants.TOP);
		tabsPanel = new JPanel();
	}

	private void createMenuBar(ActionListener browseCode,ActionListener loadAction,ActionListener saveAction){
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu(LanguageManager.getString("file"));
		JMenuItem openNew = new JMenuItem(LanguageManager.getString("openNew"));

		openNew.addActionListener(browseCode);
		JMenuItem load = new JMenuItem(LanguageManager.getString("load"));
		load.addActionListener(loadAction);
		JMenuItem save = new JMenuItem(LanguageManager.getString("save"));
		save.addActionListener(saveAction);
		fileMenu.add(openNew);
		fileMenu.add(load);
		fileMenu.add(save);
		menuBar.add(fileMenu);
		//TODO continue...
		setJMenuBar(menuBar);
	}

}
