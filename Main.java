package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		File file = new File("formula1.txt");
    	Scanner scan = new Scanner(file);
    	
    	int size = scan.nextInt();
		
		Graph<Integer> graph = new Graph<Integer>(size*2);
		graph.buildImplicationGraph(new Integer(0), scan);
		
		System.out.println(graph);

	}

}
