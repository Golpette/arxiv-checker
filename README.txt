This program is to automatically check the arXiv for papers that may be of interest.

It reads in two files: 
    (1) keywords.txt which contains a list of all words or phrases 
        you want to search for, and
    (2) arxivPages.txt which contains the list of arXiv pages you 
        wish to search in.
        
The keywords will be searched for within the title, author and abstract fields
of every paper on each arxiv page on the *present day*.
This code can be called from a script which automatically runs it every morning
and creates a file on your desktop.



The java files are:
    (1) Paper.java just holds a "Paper" object which consists of the fields title,
        authors, link and abstract
    (2) CheckPage.java which holds one large and ugly method to take an URL for an
        arxiv page and the list of keywords and check all papers on that page to see
        if any contain any of the keywords in their title, authors or abstract field
    (3) Arxiv.java, the main program which reads in the two files at the top, calls
        the check method and prints out the papers of interest to a file.
     

Program relies on the standard arXiv page format, true as of Dec 14th 2013.
