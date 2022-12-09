import java.util.Collections;
import java.util.*;
	
public class Main {


//------------------------
//  Computes a maximum flow from node s to node t in the directed graph D
//  with arc capacities u[e].
//  Return the max flow value
//-----------------------
public static Integer max_flow( 
	Digraph         D,    
	Vector<Integer> u,    
	Integer         s,    
	Integer         t,    
 	Vector<Integer> f )   
{

    Digraph residual_net = new Digraph();
    Vector<Integer> residual_capacity = new Vector<Integer>();
    create_residual_net(D, u, f, residual_net, residual_capacity);


  //-----------------------
  // TO DO: This method has to be implemented. 
  //
  // The current code fragments within this method are only 
  // to illustrate the use of the data structures and 
  // to provide an example for an BFS (using only forward arcs in the residual network) 
  // and the reconstruction of the path found by the BFS
  //------------------------

  //--------------------
  // The following block just prints the network, the capacities
  // and the out arcs of each node
  //--------------------

  System.out.println("Anzahl Knoten:    " + D.getNumberOfNodes() );
  System.out.println("Anzahl Boegen:    " + D.getNumberOfArcs() );
  System.out.println("Source node:      " + s );
  System.out.println("Target node:      " + t );
	
  // Show arcs with capacities
  for ( int e=0; e < D.getNumberOfArcs(); ++e ) {
     System.out.println( "Bogen " + e + " : ("+ D.getSource(e) + "," + D.getTarget(e) + 
                         ")  Kapazitaet: " + u.get(e) );
  }

  // Show all out-arcs of each node (in arcs are available analogously via D.getInArcs(v))
  for ( int v = 0; v < D.getNumberOfNodes(); ++v ) {
     for ( int e: D.getOutArcs(v) ) {
	System.out.println("Knoten " + v + "  Aus-Bogen " +
			    e + " (" + D.getSource(e) + "," + D.getTarget(e) +")" );
     }        	 
  }
         
  //--------------------
  // TO DO: 
  // If you implement FF-Algorithm, then you should augment the flow iteratively augment 
  // the flow f in a main loop. 
  // Within this loop, you need to search for s-t-paths in the "residual network", i.e. paths
  // that traverse arcs with positive residual capacity in the forward direction and arcs
  // with positive flow in the backwards direction. 
  //--------------------



   //-----------------
   // A simple BFS path search starting at s could be implemented using the following scheme:
   //-----------------

   // build a queue Q that holds the nodes already reached, but whose in- and out-arcs
   // have not been scanned yet
   Deque<Integer>   Q    = new LinkedList<Integer>();

   // Build vector pred, that contains for each node v
   // the id of its predecessor-arc if v has been reached or
   // a special tag (f.e. just a negativ value) if v has not been reached yet.
   // Initialize this vector to all nodes unreached.
   Vector<Integer>  pred = new Vector<Integer>();
   pred.setSize(D.getNumberOfNodes());
   for ( int v = 0; v < D.getNumberOfNodes(); ++v ) {
     pred.set(v,-2);
   }


   // Start the BFS at s
   Q.add(s);
   pred.set(s,s); // just any positive value, pred[s] will not be used in backtracking

   // Process all nodes that have been reached in this order
   while ( ! Q.isEmpty() ) {
     // v is current node, remove it from queue
     Integer v = Q.poll();

     // scan v'out arcs
     for ( int a: D.getOutArcs(v) ) {
        // w is other end of a, i.e. target of a if a is outarc of v
        Integer w = D.getTarget(a);

        // if there is residual capacity on a and w has not been reached before ...
        if ( f.get(a) < u.get(a) && pred.get(w) < 0 ) {
           // ... add w to queue and set its predecessor
           pred.set(w,a);
           Q.add(w);
        }
      }

      // TO DO: you should also scan the inarcs of v and if ....
    }

    //--------
    // This is the end of the BFS.
    // We now can check if t has been reached and, if so, backtrack the path
    // (in reverse direction from t to s) via the pred-arcs.
    // (Note that the path should traverse those arcs in reverse direction,
    // that are have been scanned in reverse direction (scanned as inarc of v above)
    //--------

    // if t has been reached
    if ( pred.get(t) >= 0 ) {

        // Just for illustration, the path is created as list of its arcs.
        // In a flow algorithm, you do not need to explicitly build the path just to iterate over its edges later....
        LinkedList<Integer> path = new LinkedList<Integer>();

        // create path by backtracking predecessors, starting at t,
        // until s is reached
        Integer v = t;
        while ( v != s ) {
            Integer a = pred.get(v);
            // if arc was forward arc
            if ( D.getTarget(a) == v ) {
                path.add(a);
                v=D.getSource(a);
            }
            else {
                path.add(a);
                v=D.getTarget(a);
            }
        }
        // and just for fun, we print the edges of the path (removing them from the path immediately) ...
        System.out.println("Path length " + path.size() );
        while ( ! path.isEmpty() ) {
            System.out.println("path contains arc " + path.poll() );
        }

    }


   // TO DO: you should return the computed max flow value here
   return 0;
}

public static void create_residual_net(Digraph graph, Vector<Integer> capacity, Vector<Integer> flow, Digraph residual_net, Vector<Integer> residual_capacity){

    System.out.println("Create Residual Network");

    for (int i = 0; i < graph.getNumberOfNodes(); i++){
        residual_net.addNode();}

    for (int j = 0; j < graph.getNumberOfNodes(); j++){

        residual_net.addNode();

        Vector<Integer> out_arcs = graph.getOutArcs(j);

        for (int k = 0; k < out_arcs.size(); k++){

            if (capacity.get(out_arcs.get(k)) - flow.get(out_arcs.get(k)) > 0){
                int new_arc = residual_net.addArc(j, graph.getTarget(out_arcs.get(k)));
                residual_capacity.add(new_arc, capacity.get(out_arcs.get(k)) - flow.get(out_arcs.get(k)));
            }

            if (flow.get(out_arcs.get(k)) > 0) {
                int new_back_arc = residual_net.addArc(graph.getTarget(out_arcs.get(k)), j);
                residual_capacity.add(new_back_arc, flow.get(out_arcs.get(k)));
            }

        }

    }

}

public static void bfs(Digraph residual_graph, Integer s, Integer t){






}




//----------- 
public static void main(String[] args) {
      try {
	  Digraph         D       = new Digraph();
	  Vector<Integer> caps    = new Vector<Integer>();
	  Vector<Integer> st_pair = new Vector<Integer>();

	  DimacsParser     parser = new DimacsParser();
         
	  parser.parse( args[0], D, caps, st_pair );
	  Integer s = st_pair.get(0);
	  Integer t = st_pair.get(1);

	  //--------------------
	  // Done reading network
	  // now D contains the digraph, caps is the capacity vector and s and t are source and target, respectively	
	  //--------------------

	  //--------------------
	  // Create flow vector of right size filled with 0
	  //--------------------
	  Vector<Integer> flow    = new Vector<Integer>();
	  flow.setSize( D.getNumberOfArcs() );
	  for(int a=0; a < D.getNumberOfArcs(); ++a) {
		flow.set(a,0);
      }


	  //--------------------
	  // Compute max flow
	  //--------------------
	  Integer value = max_flow(D, caps, s, t, flow );


	  System.out.println("Flusswert: " + value );

      }
      catch(Exception e) {
         System.out.println(e.getMessage());
      }

   }
};
