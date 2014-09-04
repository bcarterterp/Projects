import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class WordCheck {

	public boolean checkWord(String type,String word){
		
		String file_name = "/resource/"+type+"_"+word.charAt(0)+".txt";
		
		InputStream stream = WordCheck.class.getResourceAsStream(file_name);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line;    
        
        try {
        	
			while((line = br.readLine()) != null){
				
				if(line.equalsIgnoreCase(word)){
					
					return true;
					
				}
				
			}
			
		} catch (IOException e) {
			System.out.println("Problem with stream");
			e.printStackTrace();
		}
        
        return false;
        
	}


}
