import java.io.FileNotFoundException;
import java.io.IOException;

public class Test {
	
	static String pathFile = "/Users/marcogoette/Documents/Hochschule Ludwigshafen/4. Semester/4601 Introduction to Atrificial Intelligence (Dr. Reichherzer)/Project/Test Classes";
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		IRTester test = new IRTester(pathFile);
		test.run();
		
	}
}
