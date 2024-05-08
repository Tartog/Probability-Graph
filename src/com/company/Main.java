package com.company;


public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph(5);
        graph.generateGraph();
        //graph.printGraph();
        //graph.printGraphProbabilities();
        System.out.println("Вероятность появления полного графа (моделирование) = " +
                graph.completeGraphProbability(10000));
        System.out.println("Вероятность появления полного графа (полный перебор) = " +
                graph.exhaustiveSearch(false));
    }
}
