package main

import "fmt"

type Dsu struct {
	parent []int
	depth []int
	i int
}
type Mealy struct {
	n, m, q int
	delta [][]int
	phi [][]string
	used []bool
	number []int
}

func dfs(f Mealy, start int, ind int, buf []int) int {
	f.number[start] = ind
	ind = ind + 1
	f.used[start] = true
	for i := 0; i < f.m; i++ {
		if !f.used[buf[i]] {
			ind = dfs(f, buf[i], ind, f.delta[buf[i]])
		}
	}
	return ind
}

func Canon(f Mealy) Mealy {
	var buf Mealy
	buf.n = f.n
	buf.m = f.m
	buf.q = 0
	buf.delta = make([][]int, buf.n)
	buf.phi = make([][]string, buf.n)
	buf.used = make([]bool, buf.n)
	buf.number = make([]int, buf.n)
	for i := 0; i < buf.n; i++{
		buf.delta[i] = make([]int, f.m)
		buf.phi[i] = make([]string, f.m)
		buf.used[i] = false
		buf.number[i] = -1
	}
	for i:= 0; i < buf.n; i++{
		for j:= 0; j < buf.m; j++{
			buf.delta[i][j] = 0
			buf.phi[i][j] = "x"
		}
	}
	var index int
	index = 0
	index = dfs(f, f.q, index, f.delta[f.q])
	buf.n = index
	for i := 0; i < f.n; i++{
		if f.used[i] && f.number[i] != -1{
			buf.phi[f.number[i]] = f.phi[i]
			for j := 0; j < f.m; j++ {
			buf.delta[f.number[i]][j] = f.number[f.delta[i][j]]
			}
		}
	}
	return buf
}

func Find(q Dsu, x int) (Dsu, int) {
	var root int
	if x == q.parent[x]{
		return q,x
	} else {
		q, q.parent[x] = Find(q, q.parent[x])
		root = q.parent[x]
	}
	return q,root
}

func Union(q Dsu, x int, y int) Dsu {
	q,x = Find(q, x)
	q,y = Find(q, y)
	if x != y{
		if q.depth[x] < q.depth[y]{
			x, y = y, x //такой вот swap :-)
		}
		q.parent[y] = x
		if q.depth[x] == q.depth[y]{
			q.depth[x] = q.depth[x] + 1
		}
	}
	return q
}


func split(pi []int, m int, f Mealy) ([]int, int){
	m = f.n
	var q Dsu
	q.parent = make([]int, f.n)
	q.depth = make([]int, f.n)
	q.i = f.n
	for j := 0; j < f.n; j++{
		q.parent[j] = j
	}
	q.depth = make([]int, f.n)
	var w, s int
	for i := 0; i < f.n; i++{
		for j := i+1; j < f.n; j++{
			q,w = Find(q,i)
			q,s = Find(q,j)
			if pi[i] == pi[j] && w != s{
				eq := true
				for k := 0; k < f.m; k++{
					w1 := f.delta[i][k];
					w2 := f.delta[j][k];
					if pi[w1] != pi[w2]{
						eq = false
						break;
					}
				}
				if eq{
					q = Union(q, i, j)
					m--
				}
			}
		}
	}
	for i := 0; i < f.n; i++{
		q,pi[i] = Find(q, i)
	}
	return pi, m
}

func split1(pi []int, m int, f Mealy) ([]int, int){
	m =	f.n
	var q Dsu
	var w,s int
	q.depth = make([]int, f.n)
	q.parent = make([]int, f.n)
	q.i = f.n
	for i := 0; i < q.i; i++{
		q.parent[i] = i
		q.depth[i] = 0
	}
	for i := 0; i < f.n; i++{
		for j := i+1; j < f.n; j++{
			q, w = Find(q,i)
			q, s = Find(q,j)
			if  w != s{
				eq := true
				for k := 0; k < f.m; k++{
					if f.phi[i][k] != f.phi[j][k]{
						eq = false
						break;
					}
				}
				if eq{
					q = Union(q, i, j)
					m--
				}
			}
		}
	}
	for i := 0; i < f.n; i++{
		q, pi[i] = Find(q, i)
	}
	return pi, m
}



func AufenkampHohn(f Mealy) (Mealy){
	var pi = make([]int, f.n)
	var m, m_ = -1, -1
	pi, m = split1(pi, m, f)
	for{
		if m == m_ {
			break;
		}
		m_ = m
		pi, m = split(pi, m, f)
	}
	var buf Mealy
	buf.n = m
	buf.m = f.m
	buf.q = 0
	buf.delta = make([][]int, buf.n)
	buf.phi = make([][]string, buf.n)
	buf.used = make([]bool, buf.n)
	buf.number = make([]int, buf.n)
	for i := 0; i < buf.n; i++{
		buf.delta[i] = make([]int, buf.m)
		buf.phi[i] = make([]string, buf.m)
		buf.used[i] = false
		buf.number[i] = -1
	}
	var new_vertex []int
	new_vertex = make([]int, f.n)
	for i := 0; i < f.n; i++{
		new_vertex[i] = -1
	}
	var vertex []int
	vertex = make([]int, f.n)
	q1 := 0
	for i := 0; i < f.n; i++{
		if pi[i] == i{
			vertex[q1] = i
			new_vertex[i] = q1
			q1 = q1 + 1
		}
	}
	buf.q = new_vertex[pi[f.q]]
	for i := 0; i < buf.n; i++{
		for j := 0; j < buf.m; j++{
			buf.delta[i][j] = new_vertex[pi[f.delta[vertex[i]][j]]]
			buf.phi[i][j] = f.phi[vertex[i]][j]
		}
	}
	return buf
}

func Equal(t Mealy, s Mealy) {
	if t.n != s.n || t.m != s.m || t.q != s.q{
		fmt.Print("NOT EQUAL")
		return
	}
	res := 1
	for i := 0; i < t.n; i++{
		for j := 0; j < t.m; j++{
			if t.delta[i][j] != s.delta[i][j]{
				fmt.Print("NOT EQUAL")
				res = 0
				break
			}
			if t.phi[i][j] != s.phi[i][j]{
				fmt.Print("NOT EQUAL")
				res = 0
				break
			}
		}
		if res == 0{
			break
		}
	}
	if res == 0{
		return
	}
	fmt.Print("EQUAL")
	return
}

func main() {
	var n, m, q int
	fmt.Scan(&n)
	fmt.Scan(&m)
	fmt.Scan(&q)
	var f Mealy
	f.m = m
	f.n = n
	f.q = q
	f.delta = make([][]int, n)
	f.phi = make([][]string, n)
	f.used = make([]bool, n)
	f.number = make([]int, n)
	for i := 0; i < n; i++{
		f.delta[i] = make([]int, m)
		f.phi[i] = make([]string, m)
		f.used[i] = false
		f.number[i] = -1
	}
	for i := 0; i < n; i++{
		for j:= 0; j < m; j++{
			var buf int
			fmt.Scan(&buf)
			f.delta[i][j] = buf

		}
	}
	for i := 0; i < n; i++{
		for j:= 0; j < m; j++{
			fmt.Scan(&f.phi[i][j])
		}
	}
	var n1, m1, q1 int
	fmt.Scan(&n1)
	fmt.Scan(&m1)
	fmt.Scan(&q1)
	var f1 Mealy
	f1.m = m1
	f1.n = n1
	f1.q = q1
	f1.delta = make([][]int, n1)
	f1.phi = make([][]string, n1)
	f1.used = make([]bool, n1)
	f1.number = make([]int, n1)
	for i := 0; i < n1; i++{
		f1.delta[i] = make([]int, m1)
		f1.phi[i] = make([]string, m1)
		f1.used[i] = false
		f1.number[i] = -1
	}
	for i := 0; i < n1; i++{
		for j:= 0; j < m1; j++{
			var buf int
			fmt.Scan(&buf)
			f1.delta[i][j] = buf

		}
	}
	for i := 0; i < n1; i++{
		for j:= 0; j < m1; j++{
			fmt.Scan(&f1.phi[i][j])
		}
	}
	var buf, buf1 Mealy
	buf = AufenkampHohn(f)
	buf1 = Canon(buf)
	var buf_, buf1_ Mealy
	buf_ = AufenkampHohn(f1)
	buf1_ = Canon(buf_)
	Equal(buf1, buf1_)
	return

}