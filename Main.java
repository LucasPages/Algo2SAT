
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		File file = new File("/home/lucas/Documents/L3/Algo/TP1/src/formula1.txt");
    	Scanner scan = new Scanner(file);
    	
    	int size = scan.nextInt();
		
		Graph<Integer> graph = new Graph<Integer>(size*2);
		
		graph.buildImplicationGraph(0, scan);
		//System.out.println(graph);
		
		//System.out.println("Le graphe transpose : \n" + graph.buildTranspose());
		
		ArrayList<Integer> endDates = graph.dfs();
		
		Graph<Integer> transpose = graph.buildTranspose();
		ArrayList<LinkedList<Integer>> scc = transpose.dfsTranspose(endDates);
		
		System.out.println("Il y a " + scc.size() + " composantes fortement connexes\n");
		
		for(int index = 0; index < scc.size(); index++) {
			System.out.println("Composante fortement connexe " + (index + 1));
			for(int vertex : scc.get(index)){
				System.out.print(graph.verticeNumber(vertex) + " ");
			}
			System.out.println();
		}
		
		System.out.println(graph.isSatisfiable(scc) ? "Ce problème 2-SAT est satisfaisable":"Ce problème 2-SAT n'est pas satisfaisable");
		
		scan.close();	
	}
}
