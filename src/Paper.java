/**
Class to hold Paper object, represented by fields for the paper
title, authors, arxiv link and abstract.
*/

public class Paper {

	
	private String title; private String authors; 
	private String link;  private String abstrct;
	
	
	// Constructor
	public Paper(String title2, String authors2,
			String link2, String abstrct2){
		
		title = title2;
		authors = authors2;
		link = link2;
		abstrct = abstrct2;	
	}
	
	
	// Output for Paper object
	public String toString(){
		return title+"\n"+authors+"\n"+link+"\n"+abstrct+"\n";
	}
	
	
	// Method to check if it's same as another paper found already
	// Here we just check the title.
	public boolean isSame(Paper paper){
		boolean same = false;
		if( this.title.equals( paper.title ) ){
			same = true;
		}
		return same;
	}
	
	
	
	
}
