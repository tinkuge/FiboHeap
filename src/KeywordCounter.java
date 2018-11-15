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
		HashMap<String, Node> keyNode = new HashMap<String, Node>();
		Node n, remMax, reNode;
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
				String keyword = splitline[0].substring(1);
				int keyval = Integer.parseInt(splitline[1]);
				if(keyNode.containsKey(keyword)){
					n = keyNode.get(keyword);
					ifh.increaseKey(n, keyval);
				}
				
				else{
					n = ifh.insert(keyword, keyval);
					keyNode.put(keyword, n);
				}
				
				
				//System.out.println(keyfreq.get(splitline[0]));
			}
			
			else{
				//Perform remove max n times
				if(Character.isDigit(str.charAt(0))){
					int num = Integer.parseInt(str);
					System.out.println(num);
					n = ifh.peekMax();
					
					for(int i = 0; i < num; i++){
						//Call remove max
						remMax = ifh.removeMax();
						nMaxList.add(remMax);
					}
					
					if(nMaxList.size() >= 1){
						String word = nMaxList.get(0).key;
						System.out.println(word);
						pw.write(word);
					}
					
					for(int j = 1; j < nMaxList.size(); j++){
						String word = nMaxList.get(j).key;
						System.out.println(word);
						pw.write(","+ word);
					}
					
					pw.write("\n");
					
					for(int k = 0; k < nMaxList.size(); k++){
						reNode = nMaxList.get(k);
						Node updated = ifh.insert(reNode.key, reNode.val);
						
						//should you insert it into hashmap
						keyNode.put(updated.key, updated);
					}
					
					nMaxList.clear();
					
				}
				
				else{
					if(str.toLowerCase().equals("stop")){
						pw.flush();
						pw.close();
						break;
					}
				}
			}
		}
		
		
	}

}
