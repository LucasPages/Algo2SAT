
import java.io.File;
import java.io.FileNotFoundException;
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
		
		int[] endDates = graph.DFS();
		
		System.out.println("Dates de fin du parcours de graph" + "\n");
		
		for(int index = 0; index < graph.order(); index++){
			System.out.println(graph.verticeNumber(index) + " " + endDates[index]);
		}
		
		scan.close();	
	}
}
