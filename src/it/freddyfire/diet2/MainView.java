package it.freddyfire.diet2;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private MainController c;
	private ButtonGroup sex;
	private JRadioButton male, female;
	private JTextField name, address, phone, duration, heigth;
	private JTextField[] birth;
	private JTextField[][] meetings;
	private JTextArea notes;

	public MainView() {
		c = new MainController(this);
		this.setLayout(new BorderLayout(10, 10));
		
		JPanel leftLabelPanel = new JPanel(new GridLayout(7, 1, 5, 5));
		JPanel leftFieldsPanel = new JPanel(new GridLayout(7 ,1));
		
		sex = new ButtonGroup();
		male = new JRadioButton("Male");
		male.setActionCommand("uomo");
		male.setSelected(true);
		sex.add(male);
		female = new JRadioButton("Female");
		female.setActionCommand("donna");
		sex.add(female);
		JPanel sexPanel = new JPanel(new GridLayout(1, 2));
		sexPanel.add(male);
		sexPanel.add(female);
		leftLabelPanel.add(new JLabel("Sex: ", JLabel.RIGHT));
		leftFieldsPanel.add(sexPanel);
		
		name = new JTextField(10);
		leftLabelPanel.add(new JLabel("Nome: ", JLabel.RIGHT));
		leftFieldsPanel.add(name);
		
		address = new JTextField(10);
		leftLabelPanel.add(new JLabel("Indirizzo: ", JLabel.RIGHT));
		leftFieldsPanel.add(address);
		
		phone = new JTextField(10);
		leftLabelPanel.add(new JLabel("Phone: ", JLabel.RIGHT));
		leftFieldsPanel.add(phone);
		
		duration = new JTextField(10);
		leftLabelPanel.add(new JLabel("Duration: ", JLabel.RIGHT));
		JPanel durationPanel = new JPanel(new GridLayout(1, 2));
		durationPanel.add(duration);
		durationPanel.add(new JLabel(" days"));
		leftFieldsPanel.add(durationPanel);
		
		birth = new JTextField[3];
		leftLabelPanel.add(new JLabel("Birth: ", JLabel.RIGHT));
		JPanel birthPanel = new JPanel(new GridLayout(1, 3));
		for (int i = 0; i < 3; i++) {
			birth[i] = new JTextField(4);
			birthPanel.add(birth[i]);
		}
		leftFieldsPanel.add(birthPanel);
		
		heigth = new JTextField(4);
		leftLabelPanel.add(new JLabel("Heigth: ", JLabel.RIGHT));
		JPanel heightPanel = new JPanel(new GridLayout(1, 2));
		heightPanel.add(heigth);
		heightPanel.add(new JLabel(" cm"));
		leftFieldsPanel.add(heightPanel);
		
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(leftLabelPanel, BorderLayout.WEST);
		leftPanel.add(leftFieldsPanel, BorderLayout.EAST);
		this.add(leftPanel, BorderLayout.WEST);
		
		JPanel rigthPanel = new JPanel(new GridLayout(6, 1));
		meetings = new JTextField[6][5];
		for (int i = 0; i < 6; i++) {
			JPanel rowPanel = new JPanel(new FlowLayout());
			String tmp = "\u2160";
			switch (i) {
			case 0:
				tmp = "\u2160";
				break;

			case 1:
				tmp = "\u2161";
				break;
			
			case 2:
				tmp = "\u2162";
				break;
			
			case 3:
				tmp = "\u2163";
				break;
				
			case 4:
				tmp = "\u2164";
				break;
				
			case 5:
				tmp = "\u2165";
				break;
			}
			rowPanel.add(new JLabel(tmp, JLabel.CENTER));
			for (int j = 0; j < 5; j++) {
				meetings[i][j] = new JTextField(2);
				rowPanel.add(meetings[i][j]);
				if (j == 0 || j == 1)
					rowPanel.add(new JLabel("/", JLabel.CENTER));
				else if (j == 2)
					rowPanel.add(new JLabel("-", JLabel.CENTER));
				else if (j == 3)
					rowPanel.add(new JLabel(":", JLabel.CENTER));
			}
			rigthPanel.add(rowPanel);
		}
		
		this.add(rigthPanel, BorderLayout.EAST);
		
		notes = new JTextArea(20, 20);
		notes.setAutoscrolls(true);
		notes.setLineWrap(true);
		notes.setWrapStyleWord(true);
		notes.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		JScrollPane notesScroll = new JScrollPane(notes, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(notesScroll, BorderLayout.SOUTH);

		JButton open = new JButton(c.getOpenAction());
		JButton print = new JButton(c.getPrintAction());
		JPanel controlPanel = new JPanel(new FlowLayout());
		controlPanel.add(open);
		controlPanel.add(print);
		this.add(controlPanel, BorderLayout.NORTH);
	}

	public String getDietName() {
		String dietName = "";
		dietName += sex.getSelection().getActionCommand();
		dietName += "_";
		
		Date birthDate = new GregorianCalendar(Integer.parseInt(birth[2].getText()), Integer.parseInt(birth[1].getText()) - 1, Integer.parseInt(birth[0].getText())).getTime();
		long age = (System.currentTimeMillis() - birthDate.getTime()) / (1000L * 60L * 60L * 24L * 365L);
		if (age <= 59)
			dietName += "j";
		else
			dietName += "o";
		dietName += "_";
		
		int heightValue = Integer.parseInt(heigth.getText());
		if (sex.getSelection().getActionCommand().equals("uomo")) {
			if (heightValue < 165)
				dietName += "1";
			else if (heightValue < 175)
				dietName += "2";
			else
				dietName += "3";
		} else {
			if (heightValue < 155)
				dietName += "1";
			else if (heightValue < 165)
				dietName += "2";
			else
				dietName += "3";
		}
		System.out.println(dietName);
		return dietName;
	}
	
	public String getName() {
		return name.getText();
	}
	
	public String getAddress() {
		return address.getText();
	}
	
	public String getNotes() {
		return notes.getText();
	}
	
	public void printMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame f = new JFrame("Diet2");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.add(new MainView());
				f.pack();
				f.setVisible(true);
			}
		});
	}
}