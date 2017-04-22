package LogisticsManager.Facilities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.TreeSet;
class GraphException extends RuntimeException
{
    public GraphException( String name )
    {
        super( name );
    }
}

class Edge
{
    public Vertex     dest;  
    public double     cost; 
    
    public Edge( Vertex d, double c )
    {
        dest = d;
        cost = c;
    }
}

class Path implements Comparable
{
    public Vertex     dest;   // w
    public double     cost;   // d(w)
    
    public Path( Vertex d, double c )
    {
        dest = d;
        cost = c;
    }
    
    public int compareTo( Object rhs )
    {
        double otherCost = ((Path)rhs).cost;
        
        return cost < otherCost ? -1 : cost > otherCost ? 1 : 0;
    }
}

class Vertex
{
    public String     name; 
    public List<Edge>       adj;  
    public double     dist; 
    public Vertex     prev; 
    public int        scratch;

    public Vertex( String nm )
      { name = nm; adj = new LinkedList<Edge>( ); reset( ); }

    public void reset( )
      { dist = Graph.INFINITY; prev = null; scratch = 0; }
}
public class Graph
{
    public static final double INFINITY = Double.MAX_VALUE;
    private Map<String, Vertex> vertexMap = new HashMap<String, Vertex>( );

    public void addEdge( String sourceName, String destName, double cost )
    {
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName );
        v.adj.add( new Edge( w, cost ) );
    }
    
    public void printPath( String destName )
    {
        Vertex w = (Vertex) vertexMap.get( destName );
        if( w == null )
            throw new NoSuchElementException( "Destination vertex not found" );
        else if( w.dist == INFINITY )
            System.out.println( destName + " is unreachable" );
        else
        {
            
            printPath( w );
        }
        System.out.println( " = "+w.dist+" miles");
        System.out.print("•  ");
        System.out.println(w.dist+" miles / (8 hours a day * 50 mph ) = "+(w.dist/(8*50))+" days");
        
    }
    public double distPath(String destName){
    	Vertex w = (Vertex) vertexMap.get( destName );
        if( w == null )
            throw new NoSuchElementException( "Destination vertex not found" );
        else if( w.dist == INFINITY )
            System.out.println( destName + " is unreachable" );
        else
        {
            
            distPath( w );
        }
        return w.dist;
    }
    
    private void distPath( Vertex dest )
    {
        if( dest.prev != null )
        {
            distPath( dest.prev );
            
        }
    }
    /**
     * If vertexName is not present, add it to vertexMap.
     * In either case, return the Vertex.
     */
    public Vertex getVertex( String vertexName )
    {
        Vertex v = (Vertex) vertexMap.get( vertexName );
        if( v == null )
        {
            v = new Vertex( vertexName );
            vertexMap.put( vertexName, v );
        }
        return v;
    }
    
    private void printPath( Vertex dest )
    {
        if( dest.prev != null )
        {
            printPath( dest.prev );
            System.out.print( " -> " );
        }
        System.out.print( dest.name );
    }
    
    
    private void clearAll( )
    {
        for( Iterator itr = vertexMap.values( ).iterator( ); itr.hasNext( ); )
            ( (Vertex)itr.next( ) ).reset( );
    }


    
    public void dijkstra( String startName )
    {
        PriorityQueue<Path> pq = new PriorityQueue<Path>( );

        Vertex start = (Vertex) vertexMap.get( startName );
        if( start == null )
            throw new NoSuchElementException( "Start vertex not found" );

        clearAll( );
        pq.add( new Path( start, 0 ) ); start.dist = 0;
        
        int nodesSeen = 0;
        while( !pq.isEmpty( ) && nodesSeen < vertexMap.size( ) )
        {
            Path vrec = (Path) pq.poll( );
            Vertex v = vrec.dest;
            if( v.scratch != 0 )  // already processed v
                continue;
                
            v.scratch = 1;
            nodesSeen++;

            for( Iterator itr = v.adj.iterator( ); itr.hasNext( ); )
            {
                Edge e = (Edge) itr.next( );
                Vertex w = e.dest;
                double cvw = e.cost;
                
                if( cvw < 0 )
                    throw new GraphException( "Graph has negative edges" );
                    
                if( w.dist > v.dist + cvw )
                {
                    w.dist = v.dist +cvw;
                    w.prev = v;
                    pq.add( new Path( w, w.dist ) );
                }
            }
        }
        
    }
    
}