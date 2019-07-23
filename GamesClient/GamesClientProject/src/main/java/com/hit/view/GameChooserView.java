package com.hit.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.hit.controller.GamesController;

public class GameChooserView extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JRadioButton tttRB;
	private JRadioButton ctbRB;
	private JRadioButton smartRB;
	private JRadioButton psRB;
	private JRadioButton csRB;
	private JRadioButton rsRB;
	
	
	public GameChooserView(GamesController controller) {
		super("Game Chooser");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setIconImage(new ImageIcon("res/icon.png").getImage());
		setLocationByPlatform(true);
		setLayout(new BorderLayout());
		setAlwaysOnTop(true);
		initCenterPanel();
		initBottomPanel(controller);
		pack();
	}
	
	private void initCenterPanel() {
		JPanel center = new JPanel(new GridLayout(0, 1));
		JPanel p1 = new JPanel(new FlowLayout());
		JPanel p2 = new JPanel(new FlowLayout());
		JPanel p3 = new JPanel(new FlowLayout());
		//create labels..
		JLabel l1 = new JLabel("Pick-Up a Game:");
		JLabel l2 = new JLabel("Choose Game Level:");
		JLabel l3 = new JLabel("Who Starts:");
		Font font = new Font("Ariel", Font.BOLD, 14);
		l1.setFont(font);
		l2.setFont(font);
		l3.setFont(font);
		p1.add(l1);
		p2.add(l2);
		p3.add(l3);
		
		tttRB = new JRadioButton("Tic Tac Toe", true);
		ctbRB = new JRadioButton("Catch The Bunny", false);
		smartRB = new JRadioButton("Smart", true);
		JRadioButton randomRB = new JRadioButton("Random", false);
		psRB = new JRadioButton("Player", true);
		csRB = new JRadioButton("Computer", false);
		rsRB = new JRadioButton("Random", false);
		ButtonGroup group1 = new ButtonGroup();
		ButtonGroup group2 = new ButtonGroup();
		ButtonGroup group3 = new ButtonGroup();
		group1.add(tttRB);
		group1.add(ctbRB);
		group2.add(smartRB);
		group2.add(randomRB);
		group3.add(psRB);
		group3.add(csRB);
		group3.add(rsRB);
		
		p1.add(tttRB);
		p1.add(ctbRB);
		p2.add(smartRB);
		p2.add(randomRB);
		p3.add(psRB);
		p3.add(csRB);
		p3.add(rsRB);
		
		center.add(p1);
		center.add(p2);
		center.add(p3);
		getContentPane().add(center, BorderLayout.CENTER);
	}
	
	private void initBottomPanel(GamesController controller) {
		JPanel p = new JPanel(new FlowLayout());
		JButton b = new JButton("Start");
		b.setActionCommand("NEW_GAME");
		b.addActionListener(controller);
		p.add(b);
		getContentPane().add(p, BorderLayout.SOUTH);
	}
	
	public String getChosenGameType() {
		return tttRB.isSelected() ? tttRB.getText() : ctbRB.getText();
	}
	
	public String getChosenOpponentType() {
		return smartRB.isSelected() ? "Smart" : "Random";
	}
	
	public boolean isPlayerStarts() {
		if(psRB.isSelected())
			return true;
		if(csRB.isSelected())
			return false;
		//else, if random selected..
		Random random = new Random();
		return random.nextBoolean();
	}
}