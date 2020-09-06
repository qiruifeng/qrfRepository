package exam;

import java.util.ArrayList;
import java.util.Arrays;
public class TestCycle {
    private int n;
    private int[] visited;//节点状态,值为0的是未访问的
    private int[][] e;//有向图的邻接矩阵
    private ArrayList<Integer> trace=new ArrayList<Integer>();//从出发节点到当前节点的轨迹
    private boolean hasCycle=false;

    public TestCycle(int n,int[][] e){
        this.n=n;
        visited=new int[n];
        Arrays.fill(visited,0);
        this.e=e;
    }

    void findCycle(int v)   //递归DFS
    {
        if(visited[v]==1)
        {
            int j;
            if((j=trace.indexOf(v))!=-1)
            {
                hasCycle=true;
                System.out.print("Cycle:");
                while(j<trace.size())
                {
                    System.out.print(trace.get(j)+" ");
                    j++;
                }
                System.out.print("\n");
                return;
            }
            return;
        }
        visited[v]=1;
        trace.add(v);

        for(int i=0;i<n;i++)
        {
            if(e[v][i]==1)
                findCycle(i);
        }
        trace.remove(trace.size()-1);
    }

    public boolean getHasCycle(){
        return hasCycle;
    }

    public static void main(String[] args) {
        int n=5;
        int[][] e={
                {0,1,0,0,0},
                {0,0,1,1,0},
                {1,0,0,0,0},
                {0,0,0,0,1},
                {0,1,1,0,0}};//有向图的邻接矩阵,值大家任意改.

        int[][] q={{0,0,1,0,0},
                {1,0,0,0,1},
                {0,1,0,0,1},
                {0,1,0,0,0},
                {0,0,0,1,0}};

        TestCycle tc=new TestCycle(n,e);
        tc.findCycle(4);
        if(!tc.hasCycle)
            System.out.println("No Cycle.");
    }
}


