package sysc3303proj;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.sql.Time;
public class Parser {
	private File file;
	private FloorEvent fe ;
	
	public Parser() {
		file = new File("text.txt");
		
	}
	
	public FloorEvent parseFile() throws ParseException {
		
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			scanner.useDelimiter(",");
			String time;
			String currentFloor;
			String direction;
			String carButton;
			Time time1;
			int currentFloor1;
			boolean direction1;
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
				direction1 = Boolean.valueOf(direction);
				carButton1 = Integer.parseInt(carButton);
				fe = new FloorEvent(time1, currentFloor1, direction1, carButton1);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fe;
		
		
	}
	
	
}
