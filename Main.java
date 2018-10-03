
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		File file = new File("/amuhome/p16031405/Documents/L3/Algo/TP1/src/formula1.txt");
    	Scanner scan = new Scanner(file);
    	
    	int size = scan.nextInt();
		
		Graph<Integer> graph = new Graph<Integer>(size*2);
		
		graph.buildImplicationGraph(0, scan);
		//System.out.println(graph);
		
		//System.out.println("Le graphe transpose : \n" + graph.buildTranspose());
		
		ArrayList<Integer> endDates = graph.Dfs();
		
		System.out.println("Sommets dans l'ordre croissant de dates de fin" + "\n");
		
		for(int index = 0; index < graph.order(); index++){
			System.out.println(endDates.get(index) + " ");
		}
		
		scan.close();	
	}
}
