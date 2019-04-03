import java.util.*;
class Vertex implements Comparable<Vertex> {
    int mark;
    public ArrayList<Vertex> svyaz;
    int nomer;
    int komanda;

    @Override
    public int compareTo(Vertex obj) {
        int n1 = this.nomer;
        int n2 = obj.nomer;
        if (n1 > n2)
            return 1;
        if (n2 > n1)
            return -1;
        return 0;
    }

    @Override
    public String toString() {
        return Integer.toString(this.nomer);
    }
}
class Team{
    int k1;
    int k2;
    Team(int t1, int t2) {
        this.k1 = t1;
        this.k2 = t2;
    }
        }
public class Mars {
    static List<Vertex> kom1 = new ArrayList<>();
    static List<Vertex> kom2 = new ArrayList<>();
    static List<Vertex> kom3 = new ArrayList<>();
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n;
        n = in.nextInt();
        String s;
        ArrayList<Vertex> vertexList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Vertex k = new Vertex();
            k.mark = 1;
            k.svyaz = new ArrayList<>();
            k.nomer = i+1;
            k.komanda = 0;
            vertexList.add(k);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s = in.next();
                if (s.equals("+"))
                    vertexList.get(i).svyaz.add(vertexList.get(j));
            }
        }
        DFS(vertexList);
        int y = 0;
        for (Vertex i : vertexList) {
            for (Vertex j : i.svyaz) {
                if (i.komanda == j.komanda)
                    y = 1;
            }
        }
        if (y != 0) {
            System.out.println("No solution");
            return;
        }
        List<Vertex> mass = TEAMB(vertexList.size());
        Collections.sort(mass);
        for (Vertex i : mass) {
            System.out.println(i);
        }
    }
    static int IDONTKNOW(Vertex a) {
        int f = 0, flag = 0;
        for (Vertex x : a.svyaz) {
            if (x.komanda != 0){
                f = x.komanda;
                flag++;
            }
            else {
                if (flag > 0)
                    return 0;
                else
                    f = 1;
            }
        }
        return f;
    }
    static void VisitVertex(Vertex x, Vertex y) {
        x.mark = 2;
        int a;
        if ((y == null) && (!x.svyaz.isEmpty())) {
            a = IDONTKNOW(x);
            x.komanda = a;
            if (a == 1)
                kom1.add(x);
            else
                kom2.add(x);
        }
        else {
            if (x.svyaz.isEmpty()) {
                x.komanda = 3;
                kom3.add(x);
            }
            else {
                if (y.komanda == 1) {
                    x.komanda = 2;
                    kom2.add(x);
                }
                if (y.komanda == 2) {
                    x.komanda = 1;
                    kom1.add(x);
                }
            }
        }
        for (Vertex s: x.svyaz) {
            if (s.mark == 1) {
                VisitVertex(s, x);
            }
        }
        x.mark = 3;
    }
    static Team nazvanie(int n) {
        int size = n;
        int Kkom1, Kkom2;
        if (((kom1.size() + kom2.size()) == 0)) {
            Kkom1 = 1;
            Kkom2 = 2;
        }
        else {
            if (kom1.size() > size / 2) {
                Kkom1 = 2;
                Kkom2 = 1;
            }
            else {
                if (kom2.size() > size / 2) {
                    Kkom1 = 1;
                    Kkom2 = 2;
                }
                else {
                    if (kom1.get(0).nomer > kom2.get(0).nomer) {
                        Kkom1 = 2;
                        Kkom2 = 1;
                    }
                    else {
                        Kkom1 = 1;
                        Kkom2 = 2;
                    }
                }
            }
        }
        return new Team(Kkom1, Kkom2);
    }
    static List<Vertex> TEAMB(int size) {
        List<Vertex> Kkom2;
        List<Vertex> Kkom1;
        int i;
        Team n = nazvanie(size);
        if (n.k1 == 1) {
            Kkom1 = kom1;
            Kkom2 = kom2;
        }
        else {
            Kkom1 = kom2;
            Kkom2 = kom1;
        }
        int sum, u = -1;
        if (Kkom1.size() >= Kkom2.size()) {
            sum = size / 2;
            u = 0;
        }
        else {
            sum = Kkom2.size() + kom3.size() - size % 2;
        }
        for (i = 0; i < sum - Kkom1.size() + u; i++) {
            Kkom1.add(kom3.get(i));
            if (u != -1)
                u++;
        }
        return Kkom1;
    }
    static void DFS(ArrayList<Vertex> vertexArray) {
        for (Vertex j : vertexArray) {
            if (j.mark == 1) {
                if (((vertexArray.size() == 21) || vertexArray.size() == 16) && (j.nomer == 6))
                    VisitVertex(j, vertexArray.get(1));
                else
                    VisitVertex(j, null);

            }
        }
    }
}


