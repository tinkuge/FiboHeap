import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class KeywordCounter {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		ArrayList<String> inpline = new ArrayList<String>();
		HashMap<String, Integer> keyfreq = new HashMap<String, Integer>();
		Node n, remMax;
		InitializeFibHeap ifh = new InitializeFibHeap();
		ArrayList<Node> nMaxList = new ArrayList<Node>();
		
		String inFile = "keywords.txt";
		String fileLine = null;
		Scanner sc = new Scanner(new File(inFile));
		
		while(sc.hasNextLine()){
			String readline = sc.nextLine();
			inpline.add(readline);
			//System.out.println(readline);
			
		}
		
		PrintWriter pw = new PrintWriter("output_file.txt", "UTF-8");
		
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
					
					n = ifh.peekMax();
					
					for(int i = 0; i < num; i++){
						//Call remove max
						remMax = ifh.removeMax();
						nMaxList.add(remMax);
					}
					
					if(nMaxList.size() >= 1){
						
					}
				}
			}
		}
		
		
	}

}
