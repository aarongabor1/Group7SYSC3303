/***
 * This class takes in the file and uses the text to fill out the variable needed to
 * generate a floor event, time, current floor, direction and car button.
 * 
 * @author lynnmehyou, Aaron Gabor
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import javax.swing.JFileChooser;
import java.sql.Time;

public class Parser {
	private File file;
	private FloorEvent fe ;
	
	/***
	 * Asks user to pick the text file that condenses the floor events then stores it in a File object.
	 */
	public Parser() {
		//Start of code from https://www.codejava.net/java-se/swing/show-simple-open-file-dialog-using-jfilechooser
		JFileChooser fileChooser = new JFileChooser();
	    int result = fileChooser.showOpenDialog(null);
	    if (result == JFileChooser.APPROVE_OPTION) 
	    {
	      this.file = fileChooser.getSelectedFile();
	    }
	    //end of code from https://www.codejava.net/java-se/swing/show-simple-open-file-dialog-using-jfilechooser
	}
	/***
	 * Assigns the file input text to the correct variables. Changes their type from string to the type they should have to satisfy the 
	 * FloorEvent method.
	 * 
	 * @return a floor event object 
	 * @throws ParseException
	 */
	public FloorEvent parseFile() throws ParseException {
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			scanner.useDelimiter(" ");
			String time;
			String currentFloor;
			String direction;
			String carButton;
			Time time1;
			int currentFloor1;
			Direction direction1;
			int carButton1;
			DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss.mmm");
			while(scanner.hasNext()) {
				time = scanner.next();
				currentFloor = scanner.next();
				direction = scanner.next();
				carButton = scanner.next();
				long ms = dateFormat.parse(time).getTime();
				time1 = new Time(ms);
				currentFloor1 = Integer.parseInt(currentFloor);
				direction1 = Direction.valueOf(direction.toUpperCase());
				carButton1 = Integer.parseInt(carButton);
				fe = new FloorEvent(time1, currentFloor1, direction1, carButton1);
				
			}
			scanner.close();
			
		} catch (FileNotFoundException e) {
			System.out.print("Error file not found");
			e.printStackTrace();
		}
		
		return fe;
		
	}
	
}
