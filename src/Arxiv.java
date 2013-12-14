import java.util.*;
import java.io.*;

public class Arxiv {


	public static void main(String args[])throws IOException{
		
		// Get list of arXiv pages to check
		ArrayList<String> webPages = new ArrayList<String>();
		BufferedReader in1 = new BufferedReader(new FileReader("arxivPages.txt") );
		while(true){
			try{	
				String linexx = in1.readLine();
				String line = linexx.trim();
				if( !line.contains("#")  &&  !line.equals("") ){
					webPages.add( line );
				}
			}catch(NullPointerException e){
				break;
			}
		}		
		// Get list of user-defined keywords
		ArrayList<String> keywords = new ArrayList<String>();
		BufferedReader in2 = new BufferedReader(new FileReader("keywords.txt") );
		while(true){
			try{	
				String linexx = in2.readLine();
				String line = linexx.trim();
				if( !line.contains("#")  &&  !line.equals("") ){
					keywords.add( line );
				}
			}catch(NullPointerException e){
				break;
			}
		}

		
		ArrayList<Paper> papers = new ArrayList<Paper>();
		
		// Take each arxiv page and check titles, authors and abstracts for keywords		
		for( int i=0; i<webPages.size(); i++){
			
			// Get list of desired Paper objects from this arxiv page
			ArrayList<Paper> hold = CheckPage.CheckTitlesAuthorsAndAbstracts(webPages.get(i), keywords);
			
			// Ensure we only take papers not seen in another category already.
			for( int h=0; h < hold.size(); h++ ){
				boolean holdInPapersAlready = false;
				for( int p=0; p < papers.size(); p++  ){
					if( papers.get(p).isSame( hold.get(h) ) ){
						holdInPapersAlready = true;
					}
				}
				if( !holdInPapersAlready ){
					papers.add( hold.get(h) );
				}
			}
		}
		
		// Print out all the papers
		PrintWriter out = new PrintWriter( new FileWriter("papersFound.txt") );
		for( int pp=0; pp < papers.size(); pp++ ){
			System.out.println( papers.get( pp ).toString() );
			out.println( papers.get(pp).toString() );
		}
        out.close();
		
	
		
		
		
	}
	
	
}
