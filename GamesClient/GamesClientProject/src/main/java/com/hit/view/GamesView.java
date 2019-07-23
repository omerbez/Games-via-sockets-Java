package com.hit.view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.hit.controller.GamesController;
import com.hit.model.Cell;
import com.hit.model.GamesModel;


public class GamesView implements View
{
	private JFrame gameFrame;
	private GameChooserView gameChooser;
	private DrawingPanel drawingPanel;
	private List<PropertyChangeListener> observers;
	
	
	public GamesView() {
		observers = new ArrayList<PropertyChangeListener>();
	}
	
	private void createAndShowGUI() {
		gameFrame = new JFrame("Games Client");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setSize(gameFrame.getMaximumSize());
		gameFrame.setIconImage(new ImageIcon("res/icon.png").getImage());
		gameFrame.setLayout(new BorderLayout());
		
		//create the Model and Controller
		GamesModel model = new GamesModel();
		GamesController controller = new GamesController(model, this);
		model.addPropertyChangeListener(controller);
		addPropertyChangeListener(controller);
		initMenu(controller);
		
		drawingPanel = new DrawingPanel(controller);
		gameFrame.getContentPane().add(drawingPanel, BorderLayout.CENTER);
		drawingPanel.setVisible(false);
		gameFrame.setVisible(true);
		
		gameChooser = new GameChooserView(controller);
		gameChooser.setVisible(true);
	}
	
	private void initMenu(GamesController controller) {
		JMenuBar bar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newgame = new JMenuItem("New Game", new ImageIcon("res/new game.png"));
		newgame.setActionCommand("SHOW_GAME_CHOOSER");
		newgame.addActionListener(controller);
		JMenuItem exit = new JMenuItem("Exit");
		exit.setActionCommand("FORCE_EXIT");
		exit.addActionListener(controller);
		fileMenu.add(newgame);
		fileMenu.addSeparator();
		fileMenu.add(exit);
		bar.add(fileMenu);
		gameFrame.setJMenuBar(bar);
	}
	
	public void prepareNewGame(String gameType) {
		gameChooser.dispose();
		if(gameType.contentEquals("Tic Tac Toe"))
			drawingPanel.prepareNewGame("res/board.png", "res/X.png", "res/O.png", 3);
		else
			drawingPanel.prepareNewGame("res/ctb_board.png", "res/kid.png", "res/bunny.png", 9);
		drawingPanel.setVisible(true);
	}
	
	public void showGameChooser() {
		gameChooser.setVisible(true);
	}
	
	/**
	 * convert mouse click spot into board cell
	 * @param x width mouse position value
	 * @param y height mouse position value
	 * @return board cell or null if out of range.
	 */
	public Cell getCellByLocation(int x, int y) {
		return drawingPanel.getCellByLocation(x, y);
	}
	
	@Override
	public void start() {
		//lambda expression, move to dispacher thread and create GUI
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch(Exception ex) {
				ex.printStackTrace();
				//ignore..
			}			
			createAndShowGUI(); 
		});
	}
	
	public String getChosenGameType() {
		return gameChooser.getChosenGameType();
	}
	
	public String getChosenOpponentType() {
		return gameChooser.getChosenOpponentType();
	}
	
	public boolean getWhoStarts() {
		return gameChooser.isPlayerStarts();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener observer) {
		observers.add(observer);
	}
	
	private void notifyObservers(PropertyChangeEvent event) {
		for(PropertyChangeListener observer : observers)
			observer.propertyChange(event);
	}
	
	private void handleGameOver(int gameState) {
		String msg = gameState == 2 ? "You Lost!\nPlay again?" : 
			gameState == 3 ? "You WIN!\nPlay again?" : "It's a Tie\nPlay again?";
		
		String[] options = {"Yes", "No", "Choose Diffrent Game"};
		
		int i = JOptionPane.showOptionDialog(gameFrame, msg, "Game is over", JOptionPane.DEFAULT_OPTION
				, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		
		PropertyChangeEvent event = new PropertyChangeEvent(this, i==0 ? "RESTART_GAME" :  "END_GAME" , 0, 0);
		drawingPanel.setVisible(false);
		notifyObservers(event);	
		if(i == 2) 
			showGameChooser();
		
	}

	@Override
	public void updateViewNewGame(char[][] board) {
		drawingPanel.updateGameboard(board);
	}

	@Override
	public void updateViewGameMove(int gameState, char[][] board) {
		drawingPanel.updateGameboard(board);
		if(gameState > 1) {
			//game over..
			handleGameOver(gameState);
		}
	}
}