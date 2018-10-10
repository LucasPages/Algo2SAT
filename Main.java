
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		File file = new File("/amuhome/p16031405/Documents/L3/Algo/TP1/src/formula.txt");
    	Scanner scan = new Scanner(file);
    	
    	int size = Integer.valueOf(scan.nextLine());
		
		Graph<Integer> graph = new Graph<Integer>(size*2);
		
		graph.buildImplicationGraph(0, scan);
	    System.out.println(graph);
		
		//System.out.println("Le graphe transpose : \n" + graph.buildTranspose());
		
		ArrayList<Integer> endDates = graph.fullDfs();
		
		Graph<Integer> transpose = graph.buildTranspose();
		ArrayList<LinkedList<Integer>> scc = transpose.dfsOnTranspose(endDates);
		
		
		graph.printResult(scc);
		
		scan.close();	
	}
}
