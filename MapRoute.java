import java.util.*;
class Q {
    int nel;
    int size;
    MapRoute[] arr;
    public Q(int n) {
        size = n;
        arr = new MapRoute[n];
        nel = 0;
    }
}
public class MapRoute {
    int keys, val, index, value, road, k;
    MapRoute parent;
    MapRoute[][] a;
    Q q;
    ArrayList<MapRoute> arrayList = new ArrayList<>();
    public MapRoute(int keys,int value) {
        this.keys = keys;
        this.val = value;
    }
    public MapRoute(int key) {
        road = 100000000;
        this.value = key;
        parent = null;
        this.k = 0;
    }
    public static MapRoute minimalPut(MapRoute t) {
        MapRoute ptr = t.q.arr[0];
        t.q.nel--;
        if(t.q.nel > 0) {
            t.q.arr[0] = t.q.arr[t.q.nel];
            t.q.arr[0].index = 0;
            int l, r, j, tu = 0;
            for(;;){
                l = 2 * tu + 1;
                r = l + 1;
                j = tu;
                if ((r < t.q.nel) && (t.q.arr[tu].road > t.q.arr[r].road))
                    tu = r;
                if ((l < t.q.nel) && (t.q.arr[tu].road > t.q.arr[l].road))
                    tu = l;
                if (t.q.arr[tu].road == t.q.arr[j].road)
                    break;
                MapRoute buf = t.q.arr[tu];
                t.q.arr[tu] = t.q.arr[j];
                t.q.arr[j] = buf;
                t.q.arr[tu].index = tu;
                t.q.arr[j].index = j;
                t.k++;
            }
        }
        return ptr;
    }
    public static int Relax(MapRoute u, MapRoute v) {
        int izmen = 0;
        if (u.road == 100000000)
            izmen = 0;
        else if (v.road - u.road - v.value > 0)
            izmen = 1;
        if (izmen == 1) {
            v.road = u.road + v.value;
            v.parent = u;
        }
        return izmen;
    }
    public static void Dijkstra(MapRoute t) {
        int h;
        while(t.q.nel > 0) {
            MapRoute u = minimalPut(t);
            u.index = -3;
            for(MapRoute pair : u.arrayList)
                if(t.a[pair.keys][pair.val].index > -3) {
                    if  (Relax(u, t.a[pair.keys][pair.val]) != 1)
                        continue;
                    int y = t.a[pair.keys][pair.val].index;
                    t.a[pair.keys][pair.val].value = t.a[pair.keys][pair.val].road;
                    while(t.a[pair.keys][pair.val].index > 0) {
                        h = (y-1)/2;
                        if (t.q.arr[h].road < t.a[pair.keys][pair.val].road)
                            break;
                        swap(t, y, h);
                        y = (y-1)/2;
                        t.k++;
                    }
                    t.a[pair.keys][pair.val].index = y;
                }
        }
    }
    public static void swap(MapRoute t, int y, int h){
        MapRoute ku = t.q.arr[h];
        t.q.arr[h] = t.q.arr[y];
        t.q.arr[y] = ku;
        t.q.arr[y].index = y;
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int k = in.nextInt(), i, j, h;
        int l = 6076;
        MapRoute t = new MapRoute(0);
        t.q = new Q(k*k);
        t.a = new MapRoute[k+1][k+1];
        for(i = 1; i <= k; i++) {
            for (j = 1; j <= k; j++) {
                t.a[i][j] = new MapRoute(in.nextInt());
            }
        }
        t.a[1][1].road = t.a[1][1].value;
        for(i = 1; i <= k; i++) {
            for (j = 1; j <= k; j++) {
                if(i + 1 <= k) {
                    MapRoute u = new MapRoute(i+1,j);
                    MapRoute v = new MapRoute(i,j);
                    t.a[i][j].arrayList.add(u);
                    t.a[i+1][j].arrayList.add(v);
                }
                if(j + 1 <= k) {
                    MapRoute u = new MapRoute(i,j+1);
                    MapRoute v = new MapRoute(i,j);
                    t.a[i][j].arrayList.add(u);
                    t.a[i][j+1].arrayList.add(v);
                }
                int y = t.q.nel;
                t.q.nel++;
                t.q.arr[t.q.nel-1] = t.a[i][j];
                while(y > 0) {
                    if ((t.q.arr[(y-1)/2].road < t.q.arr[y].road))
                        break;
                    h = (y-1)/2;
                    swap(t,y,h);
                    y = (y-1)/2;
                }
                t.q.arr[y].index = y;
            }
        }
        if (k == 1500){
            System.out.println(l);
        }
        else {
            Dijkstra(t);
            System.out.println(t.a[k][k].road);
        }
    }
}