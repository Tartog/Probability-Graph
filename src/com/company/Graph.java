package com.company;

import java.util.ArrayList;
import java.util.PriorityQueue;

class Graph {
    private final double[][] graphProbabilities;
    private final int[][] graph;
    private final int numberOfVertices;

    public Graph()
    {
        numberOfVertices = 5;
        graphProbabilities = new double[][]{
                {0,   0.9, 0.9, 0.7, 0.1},
                {0.9, 0,   1, 0,   0.1},
                {0.9, 1, 0,   0.1, 0.2},
                {0.7, 0,   0.1, 0,   0.3},
                {0.1, 0.1, 0.2, 0.3, 0}
                /*{0,   0,   0,   0.7, 0},
                {0,   0,   0.5, 0,   0.1},
                {0,   0.5, 0,   0,   0.2},
                {0.7, 0,   0,   0,   0.3},
                {0,   0.1, 0.2, 0.3, 0}*/
                /*{0,   0.5, 0.5, 0.5, 0.5},
                {0.5, 0,   0.5, 0.5, 0.5},
                {0.5, 0.5, 0,   0.5, 0.5},
                {0.5, 0.5, 0.5, 0,   0.5},
                {0.5, 0.5, 0.5, 0.5, 0}*/
        };
        graph = new int[numberOfVertices][numberOfVertices];
    }

    public Graph(int numberOfVertices)
    {
        this.numberOfVertices = numberOfVertices;
        graphProbabilities = new double[numberOfVertices][numberOfVertices];
        for(int i = 0;i < numberOfVertices;i++)
        {
            for(int j = 0;j < numberOfVertices;j++)
            {
                graphProbabilities[j][i] = graphProbabilities[i][j] = Math.random();
            }
            graphProbabilities[i][i] = 0;
        }
        graph = new int[numberOfVertices][numberOfVertices];
    }

    private boolean bfs(int start, int end)
    {
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>();
        ArrayList<Integer> visited = new ArrayList<Integer>();
        ArrayList<Integer> neighbour = new ArrayList<Integer>();
        queue.add(start);
        visited.add(start);

        while(queue.size() > 0)
        {
            int current = queue.poll();
            for(int i = 0;i < numberOfVertices;i++)
            {
                if(graph[current][i] != 0)
                    neighbour.add(i);
            }
            for (Integer integer : neighbour) {
                if (!visited.contains(integer)) {
                    queue.add(integer);
                    visited.add(integer);
                    if (integer == end)
                        return true;
                }
            }
        }
        return false;
    }

    public void printGraphProbabilities()
    {
        for(int i = 0;i < numberOfVertices;i++)
        {
            for(int j = 0;j < numberOfVertices;j++)
            {
                System.out.print(graphProbabilities[i][j]);
                if(j + 1 != numberOfVertices)
                    System.out.print(" | ");
            }
            System.out.println("\n");
        }
    }

    public void printGraph()
    {
        for(int i = 0;i < numberOfVertices;i++)
        {
            for(int j = 0;j < numberOfVertices;j++)
            {
                System.out.print(graph[i][j]);
                if(j + 1 != numberOfVertices)
                    System.out.print(" | ");
            }
            System.out.println();
        }
    }

    public void generateGraph()
    {
        for(int i = 0;i < numberOfVertices;i++)
        {
            for(int j = 0;j < numberOfVertices;j++)
            {
                if(Math.random() <= graphProbabilities[i][j])
                    graph[j][i] = graph[i][j] = 1;
                else
                    graph[j][i] = graph[i][j] = 0;
            }
            graph[i][i] = 0;
        }
    }

    public void generateGraphWithNewEdges(ArrayList<Integer> edges)
    {
        int temp = 1;
        int count = 0;
        //System.out.println(edges);
        for(int i = 0;i < numberOfVertices;i++) {
            for (int j = temp; j < numberOfVertices; j++) {
                if (edges.get(count) == 1)
                    graph[j][i] = graph[i][j] = 1;
                else
                    graph[j][i] = graph[i][j] = 0;
                count++;
            }
            graph[i][i] = 0;
            temp++;
        }
    }

    public boolean completeGraphCheck()
    {
        for(int i = 0;i < numberOfVertices;i++)
        {
            for(int j = 0;j < numberOfVertices;j++)
            {
                if (!bfs(i, j) && i != j)
                    return false;
            }
        }
        return true;
    }

    public double completeGraphProbability(double numberOfTests)
    {
        double successfulTests = 0;
        for(int i = 0;i < numberOfTests;i++)
        {
            generateGraph();
            if(completeGraphCheck())
                successfulTests++;
        }
        return successfulTests / numberOfTests;
    }

    public double exhaustiveSearch(boolean showSteps)
    {
        double result = 0;
        ArrayList<Integer> graphEdges = new ArrayList<Integer>();
        int temp = 1;
        for(int i = 0;i < numberOfVertices;i++)
        {
            for (int j = temp;j < numberOfVertices;j++)
            {
                if(graphProbabilities[i][j] > 0)
                    graphEdges.add(0);
                else
                    graphEdges.add(-1);
            }
            temp++;
        }
        int count = 1;
        double allGraph = 0;
        double success = 0;
        while(graphEdges.contains(0))
        {
            generateGraphWithNewEdges(graphEdges);
            if(completeGraphCheck()) {
                result += connectedGraphProbability();
                //System.out.println(connectedGraphProbability());
                success++;
            }
            enumeration(graphEdges);

            if(showSteps)
            {
                System.out.println("Граф №" + count);
                printGraph();
                System.out.println();
                count++;
            }

            allGraph++;
        }
        generateGraphWithNewEdges(graphEdges);
        if(completeGraphCheck()) {
            result += connectedGraphProbability();
            success++;
        }
        if(showSteps) {
            System.out.println("Граф №" + count);
            printGraph();
            System.out.println();
        }
        allGraph++;

        //return success / allGraph;
        return result;
    }

    private void enumeration(ArrayList<Integer> edges)
    {
        int remainder = 1;
        int i = edges.size() - 1;
        while(remainder != 0)
        {
            if(edges.get(i) == 0)
            {
                edges.set(i, 1);
                remainder = 0;
            }
            else if (edges.get(i) == 1)
                edges.set(i, 0);
            i--;
        }
    }

    private double connectedGraphProbability()
    {
        double res = 1;
        int temp = 1;
        for(int i = 0;i < numberOfVertices;i++)
        {
            for(int j = temp;j < numberOfVertices;j++)
            {
                if(graphProbabilities[i][j] != 0)
                {
                    if(graph[i][j] == 1)
                    {
                       res *= graphProbabilities[i][j];
                    }
                    else
                    {
                        res *= 1 - graphProbabilities[i][j];
                    }
                }
            }
            temp++;
        }
        return res;
    }
}
