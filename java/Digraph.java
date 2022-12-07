import java.util.Vector;

/**
 * Very simple directed graph datastructure. 
 * 
 * Nodes and arcs are represented by integers [0,...,numberOfNodes-1]
 * and [0,...,numberOfArcs] respectively.
 * 
 * The methods getOutEdges(v) and getInEdges(v) return all directed edges 
 * out of or into vertex v, respectively. The methods getSource(e) and getTarget(e)
 * return the source and the destination of the directed edge e. 
 * 
 * @author Andreas Bley
 * @version 1.0
 */
public class Digraph {

   /** Number of nodes*/
   protected Integer numberOfNodes;

   /** Number of arcs*/
   protected Integer numberOfArcs;

   /** For each node: Arcs out of each node */
   protected Vector<Vector<Integer>> outArcs;

   /** For each node: Arcs into each node */
   protected Vector<Vector<Integer>> inArcs;

   /** source nodes of arcs */
   protected Vector<Integer> sources;

   /** target nodes of arcs */
   protected Vector<Integer> targets;

   /**
    * Creates an empty digraph
    */
   public Digraph() 
   {
      clear();
   }
    
   public void clear() {
      numberOfNodes = 0;
      numberOfArcs = 0;
      outArcs = new Vector<Vector<Integer>>(0);
      inArcs  = new Vector<Vector<Integer>>(0);
      sources  = new Vector<Integer>(0);
      targets  = new Vector<Integer>(0);
   }
    
   /**
    * @return Number of Nodes
    */
   public Integer getNumberOfNodes() 
   {
      return numberOfNodes;
   }

   /**
    * @return Number of Arcs
    */
   public Integer getNumberOfArcs() 
   {
      return numberOfArcs;
   }

   /**
    * @return source node of Arc a
    */
   public Integer getSource(Integer a) 
   {
      return sources.get(a);
   }

   /**
    * @return target node of Arc a
    */
   public Integer getTarget(Integer a) 
   {
      return targets.get(a);
   }
        
   /**
    * @param v vertex
    * @return Arcs emanating from v
    */
   public Vector<Integer> getOutArcs(Integer v) 
   {
      return outArcs.get(v);
   }

   /**
    * @param v vertex
    * @return Arcs entering into v
    */
   public Vector<Integer> getInArcs(Integer v) 
   {
      return inArcs.get(v);
   }
    
   /**
    * Adds a new node to the graph.
    * @return index of new node
    */
   public Integer addNode()
   {
      Integer n = numberOfNodes++;
      assert( outArcs.size() == n );
      assert( inArcs.size()  == n );
      outArcs.add( new Vector<Integer>(0) );
      inArcs.add ( new Vector<Integer>(0) );
      assert( outArcs.size() == numberOfNodes );
      assert( inArcs.size()  == numberOfNodes );	
      return n;
   }
   /**
    * Adds a new arc to the graph.
    * @param source source-vertex of new arc
    * @param target target-vertex of new arc
    * @return index of new arc
    */
   public Integer addArc(Integer source, Integer target)
   {
      assert( source != target );
      Integer e = numberOfArcs++;
      assert( sources.size() == e );
      assert( targets.size() == e );
      sources.addElement(source);
      targets.addElement(target);
      assert( sources.size() == e+1 );
      assert( targets.size() == e+1 );
      outArcs.get(source).addElement(e);
      inArcs. get(target).addElement(e);
      return e;
   }
}
