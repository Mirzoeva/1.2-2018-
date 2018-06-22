import java.util.*;

public class Gauss {
    static int od(int x, int y) {
        return y == 0 ? Math.abs(x) : od(y, x % y);
    }
    public static void umn(int[][] array, int h, int i){
        int n = array.length + 1;
        for (int j = 0; j < n; j++)
            array[i + 1][j] *= h;
    }
    public static void min(int[][] array, int t, int i){
        int n = array.length + 1;
        for (int j = 0; j < n; j++)
            array[i + 1][j] -= array[t][j];
    }
    public static void sett(int[][] array, int[][] help, int t){
        int n = array.length + 1;
        for(int j = 0; j < n; j++)
            array[t][j] = help[t][j];

    }
    public static void one(int[][] array, int[][] help) {
        int g, n = array.length + 1, k, m = 0, l = 1, o = 2;
        for (int t = 0; t + 2 < n; t++) {
        G:    for (int i = t; i + 2 < n; i++) {
                if (array[t][t] == 0) {
                    swapp(array);
                    swapp(help);
                }
                if ((array[t][t] != 0) && (array[i+1][t] != 0))
                    g = array[t][t]/ od(array[t][t], array[i+1][t]) * array[i+1][t];
                else continue G;
                umn(array, g / array[t][t], t - 1);
                umn(array, g / array[i + 1][t], i);
                min(array, t, i);
                swap(m, l);
                //System.out.println(array[t][i]);
                if (array[i+1][t] == 0) {
                    sett(array, help, t);
                    //System.out.println("I'm here");
                }
            }
            for(int i = 0; i < n-1; i++) {
                sett(help, array, i);
            }
        }
        for (int f = n - 2; f > 0; f--) {
            P:    for (int i = f; i > 0; i--) {
                if ((array[f][f] != 0) && (array[i-1][f] != 0))
                    k = array[f][f]/ od(array[f][f], array[i-1][f]) * array[i-1][f];
                else continue P;
                umn(array, k / array[f][f], f-1);
                umn(array, k / array[i - 1][f], i-2);
                min(array, f, i - 2);
                if (array[i-1][f] == 0) {
                    sett(array, help, f);
                    swap(l, o);
                }
            }
            for(int i = 0; i < n - 1; i++) {
                sett(help, array, i);
            }
        }
    }

    public static void Solution(int[][] array) {
        int n = array.length + 1, i, l;
    G:    for (i = 0; i < n - 1; i++) {
            l = od(array[i][i],array[i][n-1]);
            if(l == 0) {
                i++;
                continue G;
            }
            array[i][i] /= l;
            array[i][n-1] /= l;
        }
    }
    public static void swap(int x, int y){
        int h = x;
        x = y;
        y = h;
    }
    public static void swapp(int[][] array) {
        int r, j;
    G:    for (int i = 0; i < array.length; i++) {
            if(array[i][i] != 0)
                continue G;
            for(j = i; array.length > j && array[j][i] == 0;j++);
                if (j == array.length)
                    continue G;
            for(int y = 0; y < array.length + 1; y++) {
                r = array[i][y];
                array[i][y] = array[j][y];
                array[j][y] = r;
            }
        }
    }

    public static int res(int[][] array) {
        int s = array.length, j;
        for (int i = 0; i < array.length; i++) {
            for(j = 0; j <= array.length; j++) {
                if (array[i][j] != 0)
                    break;
                if (j == array.length)
                    s--;
            }
        }
        return array.length - s;
    }
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(),i = 0, j = 0;
        int[][] array = new int [n][n+1];
        int[][] help = new int [n][n+1];
        int[][] help2 = new int[n][n+1];
        for(i = 0; i < n; i++) {
            for(j = 0; j < n + 1; j++) {
                array[i][j] = in.nextInt();
                help[i][j] = array[i][j];
                help2[i][j] = array[i][j];
            }
        }
        one(array, help);
        Solution(array);
        int y = 0;
        for(i = 0; i < n; i++) {
            if ((array[i][i] == 0 && array[i][n] != 0) || (res(array) > 0)) {
                y = 1;
            }
        }
        if (y == 1){
            System.out.println("No solution");
        }
        else {
            for (i = 0; i < n; i++) {
                if ((array[i][i] > 0 && array[i][n] > 0) || (array[i][i] < 0 && array[i][n] < 0) || array[i][i] == 0 || array[i][n] == 0) {
                    System.out.println(Math.abs(array[i][n]) + "/" + Math.abs(array[i][i]));
                } else System.out.println("-" + Math.abs(array[i][n]) + "/" + Math.abs(array[i][i]));
            }
        }
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
//        System.out.println(timeConsumedMillis);
    }
}