import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class keywordcounter {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		ArrayList<String> inpline = new ArrayList<String>();
		HashMap<String, Node> keyNode = new HashMap<String, Node>();
		Node n, remMax, reNode;
		InitializeFibHeap ifh = new InitializeFibHeap();
		ArrayList<Node> nMaxList = new ArrayList<Node>();
		
		//set the filename from command line argument
		String inFile = args[0];
		Scanner sc = new Scanner(new File(inFile));
		
		//while the file has a line to read in
		while(sc.hasNextLine()){
			String readline = sc.nextLine();
			//add the line to the arraylist
			inpline.add(readline);
			//System.out.println(readline);
			
		}
		
		//Specify the output file for the print writer to write in
		PrintWriter pw = new PrintWriter("output_file.txt", "UTF-8");
		
		//iterate through the arraylist
		for(String str: inpline){
			
			//if the line starts with $
			if(str.charAt(0) == '$'){
				
				//split the line by space
				String[] splitline = str.split(" ");
				
				//Assign the word after $ to keyword
				String keyword = splitline[0].substring(1);
				//assign the word after the space to keyval
				int keyval = Integer.parseInt(splitline[1]);
				
				//if the hashmap already contains the keyword, call increase key
				if(keyNode.containsKey(keyword)){
					//get the node pointer from hashmap
					n = keyNode.get(keyword);
					//call increase key
					ifh.increaseKey(n, keyval);
				}
				
				else{
					//if the keyword is not in hashmap, insert it into hashmap and the fibonacci heap
					n = ifh.insert(keyword, keyval);
					keyNode.put(keyword, n);
				}
				
				
				//System.out.println(keyfreq.get(splitline[0]));
			}
			
			else{
				//Perform remove max n times, if the line is an integer
				if(Character.isDigit(str.charAt(0))){
					int num = Integer.parseInt(str);
					
					//throw exception if the integer is greater than the size of the heap
					if(num > ifh.getSize())
						throw new ArrayIndexOutOfBoundsException("ZGiven value exceeds the size of the heap");
					
					
					//get the max node form the heap
					n = ifh.peekMax();
					
					//call remove max num times
					for(int i = 0; i < num; i++){
						//Call remove max
						remMax = ifh.removeMax();
						//add it to a list to reinsert later
						nMaxList.add(remMax);
					}
					
					//if the list size is greater than zero, print the keywords to output file
					if(nMaxList.size() >= 1){
						String word = nMaxList.get(0).key;
						pw.write(word);
					}
					
					//print subsequent words to the output
					for(int j = 1; j < nMaxList.size(); j++){
						String word = nMaxList.get(j).key;
						pw.write(","+ word);
					}
					
					pw.write("\n");
					
					
					//reinsert all the nodes that were removed
					for(int k = 0; k < nMaxList.size(); k++){
						reNode = nMaxList.get(k);
						//update the hashmap with the new node pointer
						Node updated = ifh.insert(reNode.key, reNode.val);
						
						//should you insert it into hashmap
						keyNode.put(updated.key, updated);
					}
					
					nMaxList.clear();
					
				}
				
				else{
					//if the line is 'stop', terminate the program
					if(str.toLowerCase().equals("stop")){
						pw.flush();
						pw.close();
						break;
					}
				}
			}
		}
		
		sc.close();
	}

}
