import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class KeywordCounter {

	public static void main(String[] args) throws FileNotFoundException {
		ArrayList<String> inpline = new ArrayList<String>();
		HashMap<String, Integer> keyfreq = new HashMap<String, Integer>();
		
		String inFile = "keywords.txt";
		String fileLine = null;
		Scanner sc = new Scanner(new File(inFile));
		
		while(sc.hasNextLine()){
			String readline = sc.nextLine();
			inpline.add(readline);
			System.out.println(readline);
			
		}
		
		for(String str: inpline){
			if(str.charAt(0) == '$'){
				String[] splitline = str.split(" ");
				keyfreq.put(splitline[0], Integer.parseInt(splitline[1]));
				System.out.println(keyfreq.get(splitline[0]));
			}
			
			else{
				//Perform remove max n times
				if(Character.isDigit(str.charAt(0))){
					int num = Integer.parseInt(str);
					for(int i = 0; i < num; i++){
						//Call remove max
					}
				}
			}
		}
		
		
	}

}
