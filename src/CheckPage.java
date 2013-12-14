import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

// This class has a method to take a list of keywords and an arxiv page and
// print out the Title, Authors and Abstract of any papers containing your buzz words.

// Layout of each page must list info for each paper in the order: link, title, authors, abstract.
// This is true as of Dec 2013
// The final papers in the page have always been previously submitted but adjusted. They have no abstract.

public class CheckPage {
	
	
	// Rather large method which will take in an URRL of a webpage and the list of keywords and
	// check, for each paper on the page, if any of the keywords we are looking for are in the
	// title, authors or abstract.
	public static ArrayList<Paper> CheckTitlesAuthorsAndAbstracts(String webPage, ArrayList<String> words){
		
		ArrayList<Paper> papers = new ArrayList<Paper>();
				
		URL url;
	    InputStream is = null;  
	    BufferedReader br;
	    String line;
	    
	    boolean foundTitle = false;
	    boolean foundAuthors = false;
	    boolean foundAbstract = false;
	    boolean foundLink = false;

        
	    try {
	    	url = new URL( webPage );
	        is = url.openStream();  // throws an IOException
	        br = new BufferedReader(new InputStreamReader(is));
	      
	        String title="";   String authors="";   String abstrct="";  String link="";

	        //List of found titles so as not to get papers that have
	        //been listed in multiple categories.
	        ArrayList<String> holdTitles = new ArrayList<String>();
	        
        	boolean desiredPaper = false;
	        
	        while ((line = br.readLine()) != null) {	        	
	        	
	        		        	
	        	// GET LINK
	        	if( line.contains("title=\"Abstract\"") ){
	        		foundLink = true;
	        	}
	        	if( foundLink ){
	        		
	        		//Awkward line, need to find the path section,
	        		//immediately preceded by "abs".
	        		int strtfrom = line.lastIndexOf( "abs" );
	        		line = line.substring( strtfrom );
	        		
	        		String path="";
	        			
	                for( int c=0; c<line.length(); c++){
	               		if( line.charAt( c ) == '\"' ){
	               			//Path ends with a quotation mark
	               			break;
	               		}
	               		path = path + line.charAt(c);
	               	}     
	        			  
	                // Create full link:
	        		link = "arxiv.org/" + path;

	        		foundLink = false;  		
	        	}
	        	
	        	
	        	
	        	// GET TITLE
	        	if( line.contains("Title:") ){
	        		foundTitle=true;
	        	} 	
	        	if( foundTitle ){
	        		foundTitle=false;
	        		String ttt = line.replace("<span class=\"descriptor\">Title:</span>", "");
	        		title = ttt.trim();
	        		//replace any double spaces (that seem to occur sometimes...) with a single space
	        		title = title.replace("  ", " ");
	        		
	        		holdTitles.add(title);
	        		
//	        		Check if title contains keywords ignoring case:
	        		for( int y=0; y<words.size(); y++ ){
	        			if( title.toLowerCase().contains( words.get(y).toLowerCase() ) ){
	        				desiredPaper = true;
	        			}
	        			else{
	        				//This needs to be set so that once we get to the papers
	        				//at then end (altered ones) which have no abstract, we
	        				//do not keep printing them out. Stupid...
	        				desiredPaper = false;
	        			}
	        		}  		
	        	}
	        	
	        	
	        	
	        	// GET AUTHORS
	        	if( line.contains("Authors:") ){     		
	        		foundAuthors = true;
	        	}
	        	if( foundAuthors ){
	        		foundAuthors = false;
	        		boolean foundDiv = false;
	        		authors = "";
	        		while( !foundDiv ){
	        			line = br.readLine();
	        			if( line.contains("div>") ){
	        				foundDiv = true;
	        			}
	        			else{
	        				String auth = line.replace("</a>", "");
	        				String name = "";
	        				boolean deleteBit=true;
	        				for( int i=0; i<auth.length(); i++){
	        					
	        					if( !deleteBit ){
	        						name = name + auth.charAt(i);
	        					}
	        					if( auth.charAt( i ) == '>'){
	        						deleteBit = false;
	        					}
	        					
	        				}
	         				authors = authors + name;
	        			}
	        		}
//	        		Check if authors contains keywords ignoring case:
	        		for( int y=0; y<words.size(); y++ ){
	        			if( authors.toLowerCase().contains( words.get(y).toLowerCase() ) ){
	        				desiredPaper = true;
	        			}
	        		} 
	        	}
	        	
	        	// GET ABSTRACT
	        	if( line.contains("<p>") ){
	        		foundAbstract = true;
	        	}
	        	if( foundAbstract ){
	        		foundAbstract = false;
	        		
	        		boolean endAbst = false;
	        	    abstrct = line.replace("<p>", "");
	        		while( !endAbst ){
	        			line = br.readLine();
	        			if( line.contains("p>") ){
	        				endAbst = true;
	        			}
	        			else{
	        				abstrct = abstrct + " " + line;
	        			}
	        		}
	        		//replace any double spaces in abstract
	        		abstrct = abstrct.replace("  ", " ");
	        			        		
	        		//Check if abstract contains keywords ignoring case:
	        		for( int y=0; y<words.size(); y++ ){
	        			if( abstrct.toLowerCase().contains( words.get(y).toLowerCase() ) ){
	        				desiredPaper = true;
	        			}
	        		}

	        		
		        	// If either title, authors or abstract contains keywords, add em to list.
	        		if( desiredPaper == true ){
	        			
	        			//Check for valid abstract
	        			if( abstrct.contains("<a href=") ){
	        				abstrct = "Modified version of previously submitted paper. See link for abstract.";
	        			}
	        			
	        			Paper thispaper = new Paper(title, authors, link, abstrct);
	        			papers.add( thispaper );
	        			
	        			desiredPaper=false;
	        		}
	        		
	        	}
	        	        		       	
	        }// End while.
	        
	    } catch (MalformedURLException mue) {
	    	System.out.println(" *******   MalformedURLException  *********");
	        mue.printStackTrace();
	    } catch (IOException ioe){	    	
	    	System.out.println(" *******   IOException   ********");
	    	ioe.printStackTrace();
	    } finally {
	    	try {
	            is.close();
	        } catch (IOException ioe) {
	            
	        } catch (NullPointerException e){
	        	
	        }      
	    }
						
	    
	   return papers;
	   
	}// end CheckAbstract
	
	
	

}//end class

