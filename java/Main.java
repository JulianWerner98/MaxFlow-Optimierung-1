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


    LinkedList<Integer> path = bfs(residual_net, s, t);
    while(path != null){
        //System.out.println(f);

        int bottleneck = Integer.MAX_VALUE;
        for (var edge : path){
            if (Math.abs(residual_capacity.get(edge)) < bottleneck){
                bottleneck = Math.abs(residual_capacity.get(edge));
            }
        }
        for (var res_edge : path){
            int source = residual_net.getSource(res_edge);
            int target = residual_net.getTarget(res_edge);

            if (residual_capacity.get(res_edge) > 0){

                for (var org_edge : D.getOutArcs(source)){

                    if (D.getTarget(org_edge) == target){
                        f.set(org_edge, f.get(org_edge)+bottleneck);
                    }
                }
            }
            else{
                int counter = 0;
                for (var org_edge : D.getOutArcs(target)){

                    if (D.getTarget(org_edge) == source){

                        //TEST
                        if (f.get(org_edge)-bottleneck < 0){
                            System.out.println("___");
                            System.out.println(f.get(org_edge)-bottleneck);
                            System.out.println(f.get(org_edge));
                            System.out.println(bottleneck);

                        }

                        f.set(org_edge, f.get(org_edge)-bottleneck);

                    }
                }
            }
        }

        create_residual_net(D, u, f, residual_net, residual_capacity);
        path = bfs(residual_net, s, t);

    }

    int max_flow = 0;
    Vector<Integer> outArcs = D.getOutArcs(s);
    for (var arc : outArcs){

        max_flow += f.get(arc);
    }

    //test print
    int outflow = 0;
    Vector<Integer> s_out = D.getOutArcs(s);
    for (var a : s_out){
        outflow += f.get(a);
    }
    System.out.println(outflow);

    int inflow = 0;
    Vector<Integer> t_in = D.getInArcs(t);
    for (var b : t_in){
        inflow += f.get(b);
    }
    System.out.println(inflow);

   return max_flow;
}

public static void create_residual_net(Digraph D, Vector<Integer> u, Vector<Integer> f, Digraph residual_net, Vector<Integer> residual_capacity){

    residual_net.clear();
    residual_capacity.clear();

    for (int i = 0; i < D.getNumberOfNodes(); i++){
        residual_net.addNode();}

    for (int j = 0; j < D.getNumberOfNodes(); j++){


        Vector<Integer> out_arcs = D.getOutArcs(j);

        for (Integer out_arc : out_arcs) {

            // capacity left
            if (u.get(out_arc) - f.get(out_arc) > 0) {
                int new_arc = residual_net.addArc(j, D.getTarget(out_arc));
                residual_capacity.add(new_arc, u.get(out_arc) - f.get(out_arc));
            }

            //flow present
            if (f.get(out_arc) > 0) {
                int new_back_arc = residual_net.addArc(D.getTarget(out_arc), j);
                residual_capacity.add(new_back_arc, -f.get(out_arc));
            }
        }
    }
}

public static LinkedList<Integer> bfs(Digraph residual_graph, Integer s, Integer t){

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
    pred.setSize(residual_graph.getNumberOfNodes());
    for ( int v = 0; v < residual_graph.getNumberOfNodes(); ++v ) {
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
        for ( int a: residual_graph.getOutArcs(v) ) {
            // w is other end of a, i.e. target of a if a is outarc of v
            Integer w = residual_graph.getTarget(a);

            // if there is residual capacity on a and w has not been reached before ...

            //if ( f.get(a) < u.get(a) && pred.get(w) < 0 ) {

            if (pred.get(w) < 0 ) {
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
        while (!Objects.equals(v, s)) {
            Integer a = pred.get(v);
            // if arc was forward arc
            if(!v.equals(residual_graph.getTarget(a))){
                System.out.println("---");
                System.out.println(v);
                System.out.println(residual_graph.getTarget(a));

            }
            if (Objects.equals(residual_graph.getTarget(a), v)) {
                path.add(a);
                v=residual_graph.getSource(a);
            }
            else {
                path.add(a);
                v=residual_graph.getTarget(a);
            }
        }

        // and just for fun, we print the edges of the path (removing them from the path immediately) ...
        /*System.out.println("Path length " + path.size() );
        while ( ! path.isEmpty() ) {
            System.out.println(residual_graph.getTarget(path.getFirst()));
            System.out.println(residual_graph.getSource(path.getFirst()));
            System.out.println("path contains arc " + path.poll() );
        }*/

        return path;

    }
    else {
        return null;
    }

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
