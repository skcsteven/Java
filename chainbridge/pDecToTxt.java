package chainbridgeTest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.IOException;
import java.util.*;

public class pDecToTxt {
	public static ArrayList<ArrayList<String>> getRecordLayout() throws FileNotFoundException {
		// this method reads the record layout file and returns list of lists of field names/size
		// list of lists has format [[name,size],[name,size],[name,size],[]...]
		File recordLayout = new File("C://Users//fakeAdmin//Desktop//chainbridgeTest//record layout.txt");
		Scanner reader = new Scanner(recordLayout);
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>(); 
		while (reader.hasNextLine()) {
			// iterate through each line in record file
			String line = reader.nextLine();
			String[] lineSplit = line.split("\\s+");
			ArrayList<String> singleList = new ArrayList<String>(); //create arrayList to store splitted line
			singleList.add(lineSplit[0]);
			singleList.add(lineSplit[1]);
			result.add(singleList); //add splitted line arrayList to list of lists
		}
		reader.close();
		return result;
	}
	public static String convertPDec(String line, int[] fieldSizes){
		// this method will take a line from the packed decimal file as an input
		//     as well as an array containing the field sizes and return the converted
		//     packed decimal line in text format
		//split up line into string array based on fieldSize array
				ArrayList<String> pDec = new ArrayList<String>();
				int startInd = 0;
				for (int i : fieldSizes) {
					//iterate through fieldSize array to base the division off of
					pDec.add(line.substring(startInd,startInd+i));
					startInd += i;
				}
				
				//convert values that are packed decimal to readable format
				ArrayList<String> cnvrted = new ArrayList<String>();
				for (String j : pDec) {
					//System.out.println(j);
					
					if (j.substring(j.length()-1).matches("[a-zA-Z]+") || j.substring(j.length()-1).charAt(0) == '}' 
							|| j.substring(j.length()-1).charAt(0)=='{' ) {
						// test if packed decimal type
						if (j.substring(j.length()-1).matches("[ABCDEFGHI]") || j.substring(j.length()-1) == "{") {
							//test if packed decimal is positive
							j = j.substring(0,j.length()-1).replaceFirst("^0+(?!$)", ""); // remove leading zeros/end character
							cnvrted.add(j);
						} else {
							//negative packed decimal
							j = j.substring(0,j.length()-1).replaceFirst("^0+(?!$)", "");
							cnvrted.add("-"+j);
						}
					} else {
						//j already text format
						cnvrted.add(j);
					}
				}
				
				// convert string ArrayList into string ready to output to file
				String[] cleanList = new String[fieldSizes.length];
				for (int i = 0;i<fieldSizes.length;i++) {
					cleanList[i] = cnvrted.get(i);
				}
				String str1 = String.join("|", cleanList);
				return str1;
	}
	public static void main(String[] args) throws IOException {
		//first create the output file
		PrintStream wrt = new PrintStream("C://Users//fakeAdmin//Desktop//chainbridgeTest//out.txt");
		
		ArrayList<ArrayList<String>> fields = getRecordLayout(); 
		
		// output field names (header) to new file		
		String[] fieldNames = new String[fields.size()];
		for (int i = 0; i < fields.size();i++) {	
			fieldNames[i] = fields.get(i).get(0);
		}
		String str = String.join("|", fieldNames);
		wrt.println(str);
		
		// extract integer of only field sizes
		int[] fieldSize = new int[fields.size()];
		for (int i = 0; i < fields.size();i++) {	
			fieldSize[i] = Integer.parseInt(fields.get(i).get(1));
		}
		
		// output line by line of converted packed decimal file
		File packedDecimal = new File("C://Users//fakeAdmin//Desktop//chainbridgeTest//packed decimal test file.txt");
		Scanner reader = new Scanner(packedDecimal);
		while (reader.hasNextLine()) {
			String line = reader.nextLine();
			wrt.println(convertPDec(line,fieldSize));
		}
		wrt.close();
		reader.close();
	}
}
