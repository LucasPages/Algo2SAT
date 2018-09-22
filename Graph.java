package com.company;

import java.io.File;
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
        result.concat(cardinal + "\n");
        for (int i = 0; i<cardinal;i++) {
            for (Edge e : incidency.get(i)) {
                result.concat(e.source + " " + e.destination + " "
                        + e.label.toString() + "\n");
            }
        }
        return result;
    }
    
    /**
     * Crée le graphe d'implications à partir d'un fichier dans le répertoire courant
     * 
     * @return Le graphe d'implication construit
     * @throws FileNotFoundException Si le fichier n'est pas trouvé
     */
    public Graph<Integer> buildImplicationGraph() throws FileNotFoundException {
    	File file = new File("formula1.txt");
    	Scanner scan = new Scanner(file);
    	
    	int size = scan.nextInt();
    	Graph<Integer> newGraph = new Graph<Integer>(size * 2);
    	
    	int label = 0;
    	while(scan.hasNextLine()) {
    		int vertice1 = scan.nextInt();
    		int vertice2 = scan.nextInt();
    		
    		newGraph.addArc(verticeIndex(-1 * vertice1), verticeIndex(vertice2), label);
    		label++;
    		newGraph.addArc(verticeIndex(-1 * vertice2), verticeIndex(vertice1), label);
    		label++;
    	}
    	
    	scan.close();
    	return newGraph;
    }
    
    /**
     * Renvoie l'indice du sommet vertice dans le tableau incidency d'un objet Graph
     * 
     * @param size Le nombre de variables du problème 2-SAT
     * @param vertice Le numéro du sommet
     * @return L'indice du sommet dans incidency
     */
    public int verticeIndex(int vertice) {
    	return vertice + (cardinal / 2);
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
