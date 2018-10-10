
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
                result += vertexNumber(e.source) + " " + vertexNumber(e.destination) + " "
                        + e.label.toString() + "\n";
            }
        }
        return result;
    }
    
    public void printResult(ArrayList<LinkedList<Integer>> stronglyConnectedComponents) {
    	
    	System.out.println("Il y a " + stronglyConnectedComponents.size() + " composantes fortement connexes\n");
		
		for(int index = 0; index < stronglyConnectedComponents.size(); index++) {
			System.out.println("Composante fortement connexe " + (index + 1));
			
			for(int vertex : stronglyConnectedComponents.get(index)){
				System.out.print(vertexNumber(vertex) + " ");
			}
			
			System.out.println();
		}
		
		System.out.println(this.isSatisfiable(stronglyConnectedComponents) ? "Ce problème 2-SAT est satisfaisable":"Ce problème 2-SAT n'est pas satisfaisable");

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
    		String str = scan.nextLine();
    		
    		int vertice1 = Integer.valueOf(str.split(" ")[0]); //scan.nextInt();
    		int vertice2 = Integer.valueOf(str.split(" ")[1]);
    		
    		this.addArc(vertexIndex(-1 * vertice1), vertexIndex(vertice2), label);
    		this.addArc(vertexIndex(-1 * vertice2), vertexIndex(vertice1), label);
    	}
    }
    
    
    /**
     * Effectue un parcours en profondeur du graphe
     * 
     * @return Les dates de fin de parcours des sommets
     */
    public ArrayList<Integer> fullDfs() {
    	boolean visited[] = new boolean[cardinal];
    	ArrayList<Integer> endDates = new ArrayList<Integer>();
    	
    	for(int index = 0; index < cardinal; index++) {
    		visited[index] = false;
    	}
    	
    	for(int index = 0; index < cardinal; index++) {
    		if(!visited[index])
    			dfsOnVertex(index, visited, endDates);
    	}
    	
    	return endDates;
    }
    
    /**
     * Effectue un parcours en profondeur depuis un sommet spécifique du graphe
     * 
     * @param vertexIndex Le sommet source du parcours
     * @param date La date de début du parcours
     * @param visited Tableau de booléens indiquant quels sommets ont été visités
     * @param endDates	Tableau des dates de fin de parcours pour chaque sommet
     * 
     * @return La date à la fin du parcours
     */
    public void dfsOnVertex(int vertexIndex, boolean[] visited, ArrayList<Integer> endDates) {
    
    	visited[vertexIndex] = true;
    	
    	for(Edge e : incidency.get(vertexIndex)) {
    		if(!visited[e.destination])
    			dfsOnVertex(e.destination, visited, endDates);
    	}

    	endDates.add(vertexIndex);

    }
    
    /**
     * Calcule les composantes fortements connexes du graphe
     * 
     * @param endDates Les dates de fin de chaque sommet dans le parcours en profondeur calculé
     * @return Les composantes fortements connexes
     */
    public ArrayList<LinkedList<Integer>> dfsOnTranspose(ArrayList<Integer> endDates){
    	
    	ArrayList<LinkedList<Integer>> stronglyConnectedComponents = new ArrayList<LinkedList<Integer>>();
    	
    	boolean visited[] = new boolean[cardinal];
    	for(int index = 0; index < cardinal; index++)
    		visited[index] = false;
    	
    	for(int index = cardinal - 1; index >= 0; index--) {
    		if(!visited[endDates.get(index)]) {
    			LinkedList<Integer> stronglyConnectedVertices = new LinkedList<Integer>();
    			dfsOnTransposeVertex(endDates.get(index), visited, stronglyConnectedVertices);	
    			stronglyConnectedComponents.add(stronglyConnectedVertices);
    		}
    	}
    	
    	return stronglyConnectedComponents;
    	
    }
    
    /**
     * Parcours en profondeur dans le graphe transposé à partir d'un sommet spécifique
     * 
     * @param vertexIndex Indice du sommet source
     * @param visited Tableau de booléens indiquant quels sommets ont été visités
     * @param stronglyConnectedVertices Liste à remplir avec des sommets fortements connectés
     */
    public void dfsOnTransposeVertex(int vertexIndex, boolean[] visited, LinkedList<Integer> stronglyConnectedVertices){
    	visited[vertexIndex] = true;
    	stronglyConnectedVertices.add(vertexIndex);
    	
    	for(Edge edge : incidency.get(vertexIndex)) {
    		if(!visited[edge.destination])
    			dfsOnTransposeVertex(edge.destination, visited, stronglyConnectedVertices);
    	}	
    }
        
    public boolean isSatisfiable(ArrayList<LinkedList<Integer>> stronglyConnectedComponents) {
    	for(LinkedList<Integer> component : stronglyConnectedComponents) {
    		if(!componentIsSatisfiable(component))
    			return false;
    	}
    	return true;
    }
    
    public boolean componentIsSatisfiable(LinkedList<Integer> component) {
    	if(componentContainsOppositeElements(component))
    		return false;
    	return true;
    }
    
    public boolean componentContainsOppositeElements(LinkedList<Integer> component) {
    	for(int index = 0; index < component.size(); index++) {
    		if(componentContainsElement(component, index) && componentContainsElement(component, oppositeVertexIndex(index))) {
    			System.out.println("coucou:"+component);
    			System.out.println(index);
    			System.out.println(oppositeVertexIndex(index));
    			return true;
    			
    		}
    	}
    	return false;
    }
    
    public boolean componentContainsElement(LinkedList<Integer> component, int element) {
    	for(int value : component) {
    		if(value == element)
    			return true;
    	}
    	return false;
    }
    
    public int oppositeVertexIndex(int vertexIndex) {
    	int vertexNumber = vertexNumber(vertexIndex);
    	return vertexIndex(vertexNumber * -1);
    }
    
    /**
     * Renvoie l'indice du sommet vertice dans le tableau incidency d'un objet Graph
     * 
     * @param vertice Le numéro du sommet
     * @return L'indice du sommet dans incidency
     */
    public int vertexIndex(int vertice) {
    	if(vertice < 0) return vertice + (cardinal/2);
    	else return vertice + (cardinal/2 - 1);
    }
    
    /**
     * Renvoie le numéro du sommet correspondant à l'indice index dans le tableau incidency
     * 
     * @param index Indice du sommet
     * @return Le numéro du sommet 
     */
    public int vertexNumber(int index) {
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
