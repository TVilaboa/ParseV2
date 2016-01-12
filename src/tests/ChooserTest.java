package tests;

import views.LegacyFileChooserView;

/**
 * @author Nicolas Burroni
 * @since 8/3/2014
 */
public class ChooserTest {
	public static void main(String[] args) {
		LegacyFileChooserView chooser = new LegacyFileChooserView();
		chooser.setVisible(true);
		chooser.addBrowseButtonListener(e -> chooser.browseLegacyFile("C:\\"));
	}
}
