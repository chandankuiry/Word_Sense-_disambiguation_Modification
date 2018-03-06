import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class WSD {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		
	LeskWSD value  = new LeskWSD();
	String target = null;
	final String filename = "files/test1.txt";    // Location of the input file
	BufferedReader br = null;
	FileReader fr = null;
	
	try {

		fr = new FileReader(filename);
		br = new BufferedReader(fr);

		String sCurrentLine;
		
	

		br = new BufferedReader(new FileReader(filename));

		while ((sCurrentLine = br.readLine()) != null) {
			String[] words = sCurrentLine.split(";");
			String sentence = words[0];
			target = words[1];
			//System.out.println(sentence);
			//System.out.println(target);
			System.out.println(sentence + " TARGET WORD: " +target+ "\t SENSE :  " +value.preprocessing(sentence, target));
		}

	} catch (IOException e) {

		e.printStackTrace();

	}
	
	
	

	}

}
