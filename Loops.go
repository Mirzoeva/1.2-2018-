
package main

import (
	"fmt"
	."strconv"
)

type cmd struct {
	mark int
	s string
	to int
}

type vertex struct {
	i int
	com *cmd

	a, d []*vertex
	parent *vertex

	ok,ok2 bool
	bucket,bwards []*vertex
	ancestor,mark,sdom,dom *vertex
}

func check (a []int, b int) (i int) {
	for i=0;i<len(a);i++ {
		if a[i]==b {break}
	}
	return
}

func findMin(v *vertex) *vertex {
	if v.ancestor != nil {
		var s[]*vertex
		u:=v
		for ; u.ancestor.ancestor!=nil;u=u.ancestor {
			s=append(s, u)
		}
		for true{
			if len(s)==0{ break }
			lens:=len(s)-1
			v=s[lens]
			s = s[:lens]
			if v.ancestor.mark.sdom.i<v.mark.sdom.i {
				v.mark=v.ancestor.mark
			}
			v.ancestor=u.ancestor
		}
		return v.mark
	}
	return v
}

func dfs (po *[]*vertex,v *vertex) {
	v.i=len(*po)
	*po=append((*po), v)
	for _,w:=range v.d {
		if w.i == -1 {
			w.parent = v
			dfs(po,w)
		} else if !w.ok {
			w.bwards = append(w.bwards,v)
		}
	}
	v.ok=true
}

func lcount(g *map[int]*vertex, r *vertex) int {
	for _, v := range (*g) {
		v.i=-1
		v.ok=false
		v.sdom=v
		v.mark=v
		v.ancestor=nil
	}
	var po []*vertex
	dfs(&po,r)

	for i:=len(po)-1;i>0;i--{
		w:=po[i]
		for _, v:=range w.a {
			if v.i!=-1{
				u:=findMin(v)
				if u.sdom.i<w.sdom.i {
					w.sdom=u.sdom
				}
			}
		}
		w.ancestor=w.parent
		w.sdom.bucket=append(w.sdom.bucket,w)
		for _, v := range w.parent.bucket {
			u := findMin(v)
			if u.sdom==v.sdom {
				v.dom=w.parent
			} else {
				v.dom=u
			}
		}
		w.parent.bucket=make([]*vertex ,0)
	}

	for i:=1;i<len(po);i++ {
		w:=po[i]
		if w.dom!=w.sdom { w.dom=w.dom.dom }
	}

	count:=0
	for i:=len(po)-1;i>=0;i-- {
		w:=po[i]
		for _,v:=range w.bwards {
			u:=v.dom
			for u.i>w.i {
				u=u.dom
			}
			if u.i==w.i {
				count++
				w.ok=true
				break
			}
		}
	}
	return count
}

func main() {
	var n int
	var r *vertex
	fmt.Scan(&n)
	m:=make([]cmd, n)
	for i:=0;i<n;i++ {
		fmt.Scan(&m[i].mark)
		s:=fmt.Gets()
		if s[1]=='A' {
			m[i].s="ACTION"
		} else if s[1]=='B' {
			m[i].s="BRUNCH"
			m[i].to,_=Atoi(s[8:])
		} else if s[1]=='J' {
			m[i].s="JUMP"
			m[i].to,_=Atoi(s[6:])
		}
	}
	g := make(map[int]*vertex, n)
	for i:=0;i<len(m);i++ {
		v := &vertex{com: &m[i]}
		if i == 0 {
			r = v
		}
		g[m[i].mark] = v
	}
	for i:=0;i<n;i++ {
		des:=make([]int,0)
		u:=g[m[i].mark]
		if m[i].s!="ACTION"{
			des=append(des, m[i].to)
		}
		if m[i].s!="JUMP" && i!=n-1 {
			des=append(des, m[i+1].mark)
		}
		for _,x:=range des {
			v:=g[x]
			u.d=append(u.d,v)
			v.a=append(v.a,u)
		}
	}
	x:=lcount(&g,r)
	fmt.Print("%d",x)
}
