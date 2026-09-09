package zooPackage.PanelEditor;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Entities.Tickets.Ticket;
import zooPackage.Management.ZooFacade;

public class PanelDisplay {

	private static ZooFacade zooFacade = ZooFacade.getInstance();

	private static PanelDisplay instance;

	private PanelDisplay() {
	}

	public static synchronized PanelDisplay getInstance() {
		if (instance == null) {
			instance = new PanelDisplay();
		}
		return instance;
	}

	public static void showTextPane(String textContent, String imagePath, float opacity, String title) {
		final BufferedImage[] imgContainer = new BufferedImage[1];

		try {
			BufferedImage originalImg = ImageIO.read(new File(imagePath));
			BufferedImage resizedImage = new BufferedImage(1000, 493, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = resizedImage.createGraphics();
			g2.setComposite(AlphaComposite.SrcOver.derive(opacity));
			g2.drawImage(originalImg, 0, 0, 1000, 493, null);
			g2.dispose();
			imgContainer[0] = resizedImage;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		JTextArea textArea = new JTextArea(textContent);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setOpaque(false);
		textArea.setEditable(false);
		textArea.setForeground(Color.BLACK);
		textArea.setFont(textArea.getFont().deriveFont(Font.BOLD, 16f));

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);

		JPanel panel = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (imgContainer[0] != null) {
					g.drawImage(imgContainer[0], 0, 0, this);
				}
			}
		};
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.setPreferredSize(new Dimension(1000, 493));

		Object[] options = { "OK" };
		JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
				options, options[0]);
		JDialog dialog = optionPane.createDialog(null, title);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setIconImage(null);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);

		Object selectedValue = optionPane.getValue();
		if (selectedValue != null && selectedValue.equals("OK")) {
			dialog.dispose();
		}
	}

	public JPanel createPersonalInfoPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		addLabelAndField(panel, "First name:", zooFacade.getPanelFields().getFirstNameField());
		addLabelAndField(panel, "Last name:", zooFacade.getPanelFields().getLastNameField());
		addLabelAndField(panel, "ID number:", zooFacade.getPanelFields().getIdField());
		addLabelAndField(panel, "Date of birth:", zooFacade.getPanelFields().getDobField());
		addLabelAndField(panel, "Phone number:", zooFacade.getPanelFields().getPhoneNumberField());
		return panel;
	}

	public JPanel createVisitorLoginInfoPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		addLabelAndField(panel, "Visitor ID:", zooFacade.getPanelFields().getIdField());
		return panel;
	}

	public JPanel createEmployeeLoginInfoPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		addLabelAndField(panel, "Username:", zooFacade.getPanelFields().getUserNameField());
		addLabelAndField(panel, "Password:", zooFacade.getPanelFields().getPasswordField());
		return panel;
	}

	public JPanel createPromotionAddingPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		addLabelAndField(panel, "Description:", zooFacade.getPanelFields().getDescriptionField());
		addLabelAndField(panel, "Percentage {%}:", zooFacade.getPanelFields().getPercentageField());
		return panel;
	}

	public JPanel createPromotionUpdatingPanel(Promotion promo, int flag) {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		switch (flag) {
		case 1:
			addLabelAndField(panel, "Description:", zooFacade.getPanelFields().getDescriptionField());
			addLabelAndField(panel, "Percentage {%}:", zooFacade.getPanelFields().getPercentageField());
			zooFacade.getPanelFields().setPercentageField("" + promo.getDiscountPercentage());
			break;
		case 2:
			addLabelAndField(panel, "Description:", zooFacade.getPanelFields().getDescriptionField());
			addLabelAndField(panel, "Percentage {%}:", zooFacade.getPanelFields().getPercentageField());
			zooFacade.getPanelFields().setDescriptionField("" + promo.getDescription());
			break;
		case 3:
			addLabelAndField(panel, "Description:", zooFacade.getPanelFields().getDescriptionField());
			addLabelAndField(panel, "Percentage {%}:", zooFacade.getPanelFields().getPercentageField());
			break;
		}
		return panel;
	}

	public void addLabelAndField(JPanel panel, String labelText, JTextField textField) {
		panel.add(new JLabel(labelText));
		panel.add(textField);
	}

	public JPanel createTicketTypeCategoryBoxPanel(JComboBox<String> categoryComboBox) {
		JLabel categoryLabel = new JLabel("Category:");
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(categoryLabel);
		panel.add(categoryComboBox);
		return panel;
	}

	public JPanel createTicketTypeListPanel(JList<String> ticketTypesJList, String promt) {
		JScrollPane ticketTypeScrollPane = new JScrollPane(ticketTypesJList);
		JLabel ticketLabel = new JLabel(promt);
		JPanel panel = createListPanel(ticketLabel, ticketTypeScrollPane);
		return panel;
	}

	public JPanel createTicketListPanel(JList<Ticket> ticketJList, String promt) {
		JScrollPane ticketTypeScrollPane = new JScrollPane(ticketJList);
		JLabel ticketLabel = new JLabel(promt);
		JPanel panel = createListPanel(ticketLabel, ticketTypeScrollPane);
		return panel;
	}

	public JPanel createPromotionListPanel(JList<Promotion> promotionJList, String promt) {
		JScrollPane promotionTypeScrollPane = new JScrollPane(promotionJList);
		JLabel promotionLabel = new JLabel(promt);
		JPanel panel = createListPanel(promotionLabel, promotionTypeScrollPane);
		return panel;
	}

	public JPanel createListPanel(JLabel label, JScrollPane typeScrollPane) {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		int padding = 10;
		panel.add(label, gbc);
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		panel.add(typeScrollPane, gbc);
		gbc.insets = new Insets(padding, 0, 0, 0);
		return panel;
	}
}