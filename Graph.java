
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Graph<Label> {

    private class Edge {
        public int source;
        public int destination;
        public Label label;

        public Edge(int from, int to, Label label) {
            this.source = from;
            this.destination = to;
            this.label = label;
        }
    }

    private int cardinal;
    private ArrayList<LinkedList<Edge>> incidency;


    public Graph(int size) {
        cardinal = size;
        incidency = new ArrayList<LinkedList<Edge>>(size+1);
        for (int i = 0;i<cardinal;i++) {
            incidency.add(i, new LinkedList<Edge>());
        }
    }

    public int order() {
        return cardinal;
    }

    public void addArc(int source, int dest, Label label) {
        incidency.get(source).addLast(new Edge(source,dest,label));
    }


    public String toString() {
    	
        String result = new String("");
        result = result.concat(cardinal + "\n");
        
        for (int i = 0; i<cardinal;i++) {
            for (Edge e : incidency.get(i)) {
                result += verticeNumber(e.source) + " " + verticeNumber(e.destination) + " "
                        + e.label.toString() + "\n";
            }
        }
        return result;
    }
    
    public Graph<Label> buildTranspose() {
    	Graph<Label> transpose = new Graph<Label>(cardinal);
    	
    	for (int i= 0 ; i < cardinal ; i++) {
    		for (Edge edge : incidency.get(i)) {
    			transpose.addArc(edge.destination, edge.source, edge.label);
    		}
    	}
    	return transpose;
    }
    
    /**
     * AJoute au graphe les arcs définissant le graphe d'implications à partir du fichier fourni
     * 
     * @throws FileNotFoundException Si le fichier n'est pas trouvé
     */
    public void buildImplicationGraph(Label label, Scanner scan) throws FileNotFoundException {
    	
    	while(scan.hasNextLine()) {
    		int vertice1 = scan.nextInt();
    		int vertice2 = scan.nextInt();
    		
    		this.addArc(verticeIndex(-1 * vertice1), verticeIndex(vertice2), label);
    		this.addArc(verticeIndex(-1 * vertice2), verticeIndex(vertice1), label);
    	}
    }
    
    
    /**
     * Effectue un parcours en profondeur du graphe
     * 
     * @return Les dates de fin de parcours des sommets
     */
    public ArrayList<Integer> Dfs() {
    	boolean visited[] = new boolean[cardinal];
    	ArrayList<Integer> endDates = new ArrayList<Integer>();
    	
    	for(int index = 0; index < cardinal; index++) {
    		visited[index] = false;
    	}
    	
    	int date = 0;
    	
    	for(int index = 0; index < cardinal; index++) {
    		if(!visited[index])
    			date = DfsVertice(index, date, visited, endDates);
    	}
    	
    	return endDates;
    }
    
    /**
     * Effectue un parcours en profondeur depuis un sommet spécifique du graphe
     * 
     * @param verticeIndex Le sommet source du parcours
     * @param date La date de début du parcours
     * @param visited Tableau de booléens indiquant quels sommets ont été visités
     * @param endDates	Tableau des dates de fin de parcours pour chaque sommet
     * 
     * @return La date à la fin du parcours
     */
    public int DfsVertice(int verticeIndex, int date, boolean[] visited, ArrayList<Integer> endDates) {
    
    	visited[verticeIndex] = true;
    	date++;
    	
    	for(Edge e : incidency.get(verticeIndex)) {
    		if(!visited[e.destination])
    			date = DfsVertice(e.destination, date, visited, endDates);
    	}
    	
    	date++;
    	endDates.add(verticeIndex);
    	
    	return date;
    }
    
    public ArrayList<LinkedList<Integer>> DfsTranspose(ArrayList<Integer> endDates){
    	
    	ArrayList<LinkedList<Integer>> stronglyConnectedComponents = new ArrayList<LinkedList<Integer>>();
    	
    	return stronglyConnectedComponents;
    	
    }
    
    /**
     * Renvoie l'indice du sommet vertice dans le tableau incidency d'un objet Graph
     * 
     * @param vertice Le numéro du sommet
     * @return L'indice du sommet dans incidency
     */
    public int verticeIndex(int vertice) {
    	if(vertice < 0) return vertice + (cardinal/2);
    	else return vertice + (cardinal/2 - 1);
    }
    
    /**
     * Renvoie le numéro du sommet correspondant à l'indice index dans le tableau incidency
     * 
     * @param index Indice du sommet
     * @return Le numéro du sommet 
     */
    public int verticeNumber(int index) {
    	if(index < cardinal/2) return index - (cardinal/2);
    	else return index - (cardinal/2 - 1);
    }

    public interface ArcFunction<Label,K> {
        public K apply(int source, int dest, Label label, K accu);
    }

    public interface ArcConsumer<Label> {
        public void apply(int source, int dest, Label label);
    }

    public <K> K foldEdges(ArcFunction<Label,K> f, K init) {
        for (LinkedList<Edge> adj : this.incidency) {
            for (Edge e : adj) {
                init = f.apply(e.source, e.destination, e.label, init);
            }
        };
        return init;
    }

    public void iterEdges(ArcConsumer<Label> f) {
        for (LinkedList<Edge> adj : this.incidency) {
            for (Edge e : adj) {
                f.apply(e.source, e.destination, e.label);
            }
        }
    }



}
