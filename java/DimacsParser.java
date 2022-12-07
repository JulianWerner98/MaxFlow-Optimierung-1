import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Vector;

/**
 * Exception to be thrown by parser if file
 * does not match format specification for stp-files
 */
class DimacsParserException extends Exception {
	
   static final long serialVersionUID = 20111116;
	
   DimacsParserException(String message) {
      super(message);
   }
}

/**
 *  Parser for Dimacs format files.
 */
public class DimacsParser {
    
    StreamTokenizer tokenizer;

    private int    arc;
    private int    a;
    private int    b;
    private int    dummy;
    private int    c;
	
    private int numberOfNodes;
    private int numberOfArcs;
       
    public DimacsParser()
    {}

    /**
     * Parse file into digraph, a capacity vector and pair containing source and target node
     */
    public void parse( String fileName,
		       Digraph         digraph,
		       Vector<Integer> capacities,
		       Vector<Integer> st_pair ) 
      throws DimacsParserException, IOException 
   {
      File f = new File(fileName);
      tokenizer = new StreamTokenizer(new FileReader(f));
      tokenizer.eolIsSignificant(false);
      tokenizer.lowerCaseMode(true);
      tokenizer.parseNumbers();
      tokenizer.quoteChar('"');
      tokenizer.slashSlashComments(false);
      tokenizer.slashStarComments(false);
      tokenizer.commentChar       ('c');

      while ( tokenizer.nextToken() != StreamTokenizer.TT_EOF ) {

	  if ( tokenizer.sval.equals("p") ) {
	      
	      if ((tokenizer.nextToken() == StreamTokenizer.TT_WORD) &&
		  (tokenizer.sval.equals("max")) ) {
	      }
	      else {
		  throw new DimacsParserException("Problem type is not 'max'!");
	      }

	      if (tokenizer.nextToken() == StreamTokenizer.TT_NUMBER) {	      
		  numberOfNodes = (int)tokenizer.nval; 
	      }
	      else {
		  throw new DimacsParserException("Problem type is not 'max'!");
	      }
	      
	      if (tokenizer.nextToken() == StreamTokenizer.TT_NUMBER) {	      
		  numberOfArcs  = (int)tokenizer.nval;
	      }
	      else {
		  throw new DimacsParserException("Problem type is not 'max'!");
	      }

	      for ( int i = 0; i < numberOfNodes; ++i ) {
		  digraph.addNode();
	      }

	      capacities.setSize( numberOfArcs );
	      st_pair.   setSize( 2 );
	  }
	  else if ( tokenizer.sval.equals("n") ) {

	      if (tokenizer.nextToken() == StreamTokenizer.TT_NUMBER) {
		  dummy = (int) tokenizer.nval;
	      }
	      else {
		  throw new DimacsParserException("Node number expected after 'n'!");
	      }
	      
	      if ( tokenizer.nextToken() == StreamTokenizer.TT_WORD ) {
		  if ( tokenizer.sval.equals("s") ) {
		      st_pair.set( 0, dummy-1 );
		  }
		  else if ( tokenizer.sval.equals("t") ) {
		      st_pair.set( 1, dummy-1 );
		  }
		  else {
		      throw new DimacsParserException("Either 's' or 't' expected after 'n' and number!");
		  }
	      }
	  }

	  else if ( tokenizer.sval.equals("a") ) {
		
            if(tokenizer.nextToken() == StreamTokenizer.TT_NUMBER)
               a = (int)tokenizer.nval - 1;
            else 
               throw new DimacsParserException("Source of an arc cannot be read!");
            if(tokenizer.nextToken() == StreamTokenizer.TT_NUMBER)
               b = (int)tokenizer.nval - 1;
            else 
               throw new DimacsParserException("Target of an arcs cannot be read!");
            if(tokenizer.nextToken() == StreamTokenizer.TT_NUMBER)
		c = (int)tokenizer.nval;
            else 
               throw new DimacsParserException("Capacity of an arc cannot be read!");

	    arc = digraph.addArc(a,b);
	    capacities.set(arc,c);	
	  }
        
	      
      }
   }
}