package cosc202.andie.actions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import cosc202.andie.SetLanguage;


/**
 * <p>
 * Creates a colourchooser.
 * </p>
 * 
 * <p>
 * The ColorChooser creates an interface to select a new colour and returns the colour when selected, or returns the previous colour when cancelled or exited.
 * </p>
 * 
 * @author Sola Woodhouse
 * @version 1.0
 */
public class ColorChooser extends JDialog {

	private SetLanguage language = SetLanguage.getInstance();
	private int rValue,gValue,bValue;
	private Color color;
	private JPanel panel;
	public JLabel label1 = new JLabel(language.getTranslated("rgb_colour")+rValue+","+gValue+","+bValue);
	private static Color colourDefault = Color.WHITE;
	String title;
	
	/**The method to return the colour selected with the colour chooser inteface 
	 *@return color
	*/
	public Color getColor(){
		return color;
	} 

	/** Constructor for ColorChooser */
	public ColorChooser(){
	}

	/**showDialog creates the colour chooser panel with a JDialog and an AbstractColorChooserPanel
	 * @param input is component and the title of the colourpanel
	 */
	public void showDialog(Component comp, String titleIn){

        JDialog dialog = createDialog(comp, title);
		title = titleIn;

		
		AbstractColorChooserPanel chooser = new JColorChooser().getChooserPanels()[1];

		// disable the visibility of the hue,sat... text
		Component[] components = chooser.getComponents();
		components[0].setVisible(false);
		chooser.getColorSelectionModel().setSelectedColor(colourDefault);

		// Dialog panel
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// dialog options
		dialog.setResizable(false);
		dialog.setTitle(language.getTranslated("colour_picker"));

		// options panel
		JPanel optionsPanel = new JPanel(new FlowLayout());

		// add colour chooser to main panel
		panel.add(chooser);


		// add cancel and select buttons
		JButton select = new JButton(language.getTranslated("select_colour"));
		optionsPanel.add(select);

		JButton cancel = new JButton(language.getTranslated("cancel"));
		optionsPanel.add(cancel);

		panel.add(optionsPanel);

		// add panel to dialog
		dialog.add(panel, BorderLayout.CENTER);
		dialog.pack();
		select.addActionListener((ActionListener) new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==select){
					color = chooser.getColorSelectionModel().getSelectedColor();
					colourDefault = color;
					dialog.setVisible(false);
				}
			}
		});
		cancel.addActionListener((ActionListener) new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==cancel){
					dialog.setVisible(false);
				}
			}
		});



		dialog.setSize(400, 400);

		dialog.setVisible(true);

	}
	
	/**Createdialog method for the colour chooser
	 * @param component 
	 * @param title
	 * @return JDialog
	 */
	private JDialog createDialog(Component comp, String title) {
        JDialog dialog = new JDialog(null, title, DEFAULT_MODALITY_TYPE, getGraphicsConfiguration());

        return dialog;
	}

	
}	