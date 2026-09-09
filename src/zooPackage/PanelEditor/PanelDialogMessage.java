package zooPackage.PanelEditor;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PanelDialogMessage {

	private static PanelDialogMessage instance;

	private PanelDialogMessage() {
	}

	public static synchronized PanelDialogMessage getInstance() {
		if (instance == null) {
			instance = new PanelDialogMessage();
		}
		return instance;
	}

	public int showConfirmDialog(String message) {
		int input = JOptionPane.showConfirmDialog(null, message, "Exit Confirmation", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE); // 0 - Yes, 1 - No
		return input;
	}

	public int showConfirmDialog(JPanel panel, String title) {
		int input = JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE); // 0 - Ok, 2 - Cancel, -1 - Close
		return input;
	}

	public String showInputDialog(String message, String title) {
		String input = JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
		return input;
	}

	public void showListDialog(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
	}

	public void showInformationDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
	}

	public void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void showDBErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Data Base Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void showWarningDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}

}
