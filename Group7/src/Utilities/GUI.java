package Utilities;

import javax.swing.*;
import java.awt.*;
import Elevator.ElevatorState;
/**
 * GUI is a class that creates and updates the graphical interface for the buildings elevator system.
 * 
 * @author Aaron Gabor
 * @version 1.4
 */
public class GUI 
{	
	//Basic frame variable
	private JFrame frame;
	private JLabel[] labels;
	private JTextArea[] textArea;
	private JScrollPane[] pane;
	
	//Variables for the top part of the frame
	private JPanel[] topPartPanel;
	private JPanel[] onorOffPanel;
	private JLabel[] onLabel;
	private JPanel[] onPanel;
	private JLabel[] offLabel;
	private JPanel[] offPanel;
	
	//Variable for the different fonts to be used on the display
	private final static Font font1 = new Font("Arial", Font.BOLD, 25);
	private final static Font font2 = new Font("Arial", Font.BOLD, 20);
	
	/**
	 * Constructor that creates the display
	 */
	public GUI()
	{
		this.textArea = new JTextArea[4];
		this.pane = new JScrollPane[4];
		
		this.frame = new JFrame("Group 7");
		this.frame.setLayout(new GridLayout(2, 4));
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setupTopDisplay();
		for(int i = 0; i < this.textArea.length; i++)
		{
			this.textArea[i] = new JTextArea(10, 20);
			this.textArea[i].setEditable(false);
			this.textArea[i].setLineWrap(true);
			this.pane[i] = new JScrollPane(this.textArea[i]);
			this.frame.add(this.pane[i]);
		}
		//Changes these values to make the window bigger or smaller on startup.
		this.frame.setSize(1000, 600); 
		this.frame.setVisible(true);	
	}
	
	/**
	 * setElevatorError is a method that will set an error for a given elevator,
	 * then updates the display.
	 * 
	 * @param elevatorNumber is an integer value for the elevator.
	 * @param error a string that represents the kind of error that the elevator experienced.
	 */
	public void setElevatorError(int elevatorNumber, String error)
	{
		this.textArea[elevatorNumber-1].append(error + "\n" + "\n");
		this.onPanel[elevatorNumber-1].setVisible(false);
		this.offPanel[elevatorNumber-1].setVisible(true);
		this.textArea[elevatorNumber-1].setCaretPosition(this.textArea[elevatorNumber-1].getDocument().getLength());
	}
	
	/**
	 * updateStates is a method that will update the display of a given elevator to show
	 * the most up to date information about that elevator.
	 * 
	 * @param elevatorNumber is an integer value for the elevator.
	 * @param state is a ElevatorState object with the information to be updated.
	 */
	public void updateState(int elevatorNumber, ElevatorState state, boolean softFailure)
	{
		this.textArea[elevatorNumber-1].append("Current floor: " + state.getFloor() + "\n");
		this.textArea[elevatorNumber-1].append("Current direction: " + state.getDirection() + "\n" + "\n");
		if(state.isShutDown())
		{
		    String error = "Elevator " + elevatorNumber +  " is stuck.";
		    setElevatorError(elevatorNumber, error);
		    /*
			this.onPanel[elevatorNumber-1].setVisible(false);
			this.offPanel[elevatorNumber-1].setVisible(true);
			*/
		}
		else
		{
			this.offPanel[elevatorNumber-1].setVisible(false);
			this.onPanel[elevatorNumber-1].setVisible(true);
		}
		
		if (softFailure) {
		    setElevatorError(elevatorNumber, "Door stuck");
		}
		
		this.textArea[elevatorNumber-1].setCaretPosition(this.textArea[elevatorNumber-1].getDocument().getLength());
	}
	
	/**
	 * setupTopDisplay is a private method that sets up the top part of the display.
	 */
	private void setupTopDisplay()
	{
		this.topPartPanel = new JPanel[4];
		this.onorOffPanel = new JPanel[4];
		this.onLabel = new JLabel[4];
		this.onPanel = new JPanel[4];
		this.offLabel = new JLabel[4];
		this.offPanel = new JPanel[4];
		this.labels = new JLabel[4];
		
		this.labels[0] = new JLabel("Elevator 1", SwingConstants.CENTER);
		this.labels[1] = new JLabel("Elevator 2",SwingConstants.CENTER);
		this.labels[2] = new JLabel("Elevator 3", SwingConstants.CENTER);
		this.labels[3] = new JLabel("Elevator 4", SwingConstants.CENTER);
		for(int i = 0; i < 4; i++)
		{
			this.topPartPanel[i] = new JPanel();
			this.topPartPanel[i].setLayout(new GridLayout(2, 1));
			this.topPartPanel[i].setBackground(Color.white);
			this.topPartPanel[i].setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
			this.topPartPanel[i].setVisible(true);
			
			this.onorOffPanel[i] = new JPanel();
			this.onorOffPanel[i].setBackground(Color.white);
			this.onorOffPanel[i].setVisible(true);
			
			this.labels[i].setFont(font1);
			
			this.topPartPanel[i].add(this.labels[i]);
			this.topPartPanel[i].add(this.onorOffPanel[i]);
						
			this.onPanel[i] = new JPanel();
			this.onPanel[i].setBackground(Color.green);
			this.onPanel[i].setVisible(true);
			this.onLabel[i] = new JLabel("Online", SwingConstants.CENTER);
			this.onLabel[i].setFont(font2);
			this.onPanel[i].add(this.onLabel[i]);
			
			this.offPanel[i] = new JPanel();
			this.offPanel[i].setBackground(Color.red);
			this.offPanel[i].setVisible(false);
			this.offLabel[i] = new JLabel("Offline", SwingConstants.CENTER);
			this.offLabel[i].setForeground(Color.white);
			this.offLabel[i].setFont(font2);
			this.offPanel[i].add(this.offLabel[i]);
			
			this.onorOffPanel[i].add(this.onPanel[i]);
			this.onorOffPanel[i].add(this.offPanel[i]);

			this.frame.add(this.topPartPanel[i]);
		}
	}
}
