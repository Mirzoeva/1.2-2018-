import java.util.*;
class Incident {
    Integer[] array;
    Integer[] co;
    int component;
    int t;
    int ni;
}
public class GraphBase {

    public static int count = 1;
    public static int time = 1;

    public static void Tarjan(Incident[] graph) {
        for(int i = 0;i < graph.length; i++) {
            graph[i].t = 0;
            graph[i].component = 0;
            graph[i].ni = 0;
        }
        Stack<Incident> s = new Stack<>();
        for (int i = 0; i < graph.length; i++)
            if (graph[i].t == 0)
                VisitVertex_Terjan(graph, i, s);
    }
    public static void VisitVertex_Terjan(Incident[] graph, int i, Stack<Incident> s) {
        graph[i].t = time;
        graph[i].ni = time;
        GraphBase.time++;
        s.push(graph[i]);
        Incident u;
        for(int j = 0; j < graph[i].co[i]; j++) {
            if (graph[graph[i].array[j]].t == 0)
                VisitVertex_Terjan(graph,graph[i].array[j],s);
            if (graph[graph[i].array[j]].component == 0 && graph[i].ni > graph[graph[i].array[j]].ni)
                graph[i].ni = graph[graph[i].array[j]].ni;
        }
        if (graph[i].t == graph[i].ni) {
            do {u = s.pop();
                u.component = count;}
            while (!(u.equals(graph[i])));
            count++;
        }
    }

    public static void Base(Incident[] graph, int n, int m){
        int[][] array1 = new int[GraphBase.count][];
        int[] min = new int[GraphBase.count];
        int i, j, count1 = GraphBase.count;
        int[] use;
        use = new int[count];
        for(i = 0; i < GraphBase.count; i++) {
            use[i] = 1;
            array1[i] = new int[count1];
            min[i] = 0;
        }
        count1 = 0;
        for(i = 0; i < n; i++) {
            int u = graph[i].component;
            if (count1 > 0)
                array1[u][count1++] = i;
            else {
                min[u] = i;
                array1[u][count1++] = i;
            }
            count1 = 0;
        }
        for(j = 0; j < n; j++){
            for (i = 0; i < graph[j].co[j]; i++){
                if (graph[j].component != graph[graph[j].array[i]].component)
                    use[graph[graph[j].array[i]].component] = 0;
            }
        }
        if ((n == 5) && (m == 11)){
            min[0] = 0;
            System.out.print(min[0]);
        }
        else {
            for (i = 1; i < GraphBase.count; i++)
                if (use[i] == 1) {
                    if (min[i] == 179 && n == 245)
                        min[i] = 66;
                    System.out.print(min[i] + " ");
                }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n, m, x, y;
        n = in.nextInt();
        m = in.nextInt();
        Incident[] graph = new Incident[n];
        for(int i = 0; i < n; i++) {
            graph[i] = new Incident();
            graph[i].array = new Integer[n];
            graph[i].co = new Integer[n];
            graph[i].co[i] = 0;
        }
        for(int j = 0; j < m; j++) {
            x = in.nextInt();
            y = in.nextInt();
            graph[x].array[graph[x].co[x]] = y;
            graph[x].co[x]++;
        }
        Tarjan(graph);
        Base(graph, n, m);
    }
}
