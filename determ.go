package main
import "fmt"
type Stack struct{
	cap, top int
	array [] int
}

func bubblesort(a[]int, n int) []int{
	var i, j int
	for i = n; i >= 0; i--{
		for j = 0; j < i; j++{
			if (a[j] > a[j+1]){
				a[j], a[j+1] = a[j+1], a[j]
				}
		}
	}
	return a
}
func rar(a [][] Stack, q int, stack Stack) Stack{
	var color = "white"
	for i := 0; i < stack.top; i++{
		if stack.array[i] == q{
			color = "black"
			break
		}
	}
	if color == "white"{
		stack.array[stack.top] = q
		stack.top++
		for i := 0; i < a[q][0].top; i++{
			stack = rar(a, a[q][0].array[i], stack)
		}
	}
	return stack
}
func help(help []int, raz int, a [][]Stack, a1 [][] Stack, res [] int, rf int, n int) ([]Stack, []Stack){
	var i, j, k, l int
	var h = 1
	var one Stack
	var all []Stack
	res1 := make([] Stack, n)
	all = make([] Stack, len(a)*10)
	one.array = make([]int, len(help))
	one.cap = len(help)
	one.top = 0
	one = rar(a, rf, one)
	all[0] = one
	var stack []Stack
	stack = make([]Stack, 1000)
	stack[0] = one
	var x = 1
	var lf int
	for ; x != 0; {
		x--
		var t = stack[x]
		for i = 0; i < t.top; i++{
			var color, one_color = "white", "white"
			if res[t.array[i]] == 1 {
				for j = 0; j < lf; j++{
					if res1[j].top != t.top{
						continue
					}
					color = "white"
					for k = 0; k < t.top; k++{
						if res1[j].array[k] != t.array[k]{
							color = "black"
							break
						}
					}
					if color == "white"{
						one_color = "black"
						break
					}
				}
				if one_color == "white"{
					res1[lf] = t
					lf++
				}
			}
		}
		for i = 1; i < raz; i++{
			var bn [] int
			var dlina int
			var color = "white"
			bn = make([] int, len(a))
			for j = 0; j < t.top; j++{
				for k = 0; k < a[t.array[j]][i].top; k++{
					for l = 0; l < dlina; l++{
						if bn[l] == a[t.array[j]][i].array[k]{
							color = "black"
							break
						}
					}
					if color == "white"{
						bn[dlina] = a[t.array[j]][i].array[k]
						dlina++
					}
				}

			}
			var buf Stack
			buf.array = make([]int, len(help))
			buf.cap = len(help)
			buf.top = 0
			for i := 0; i < dlina; i++{
				buf = rar(a, bn[i], buf)
			}
			color = "white"
			var one_color = "white"
			for j = 0; j < h; j++ {
				color = "white"
				if all[j].top != buf.top {
					continue
				}
				for k = 0; k < all[j].top; k++ {
					if buf.array[k] != all[j].array[k] {
						color = "black"
						break
					}
				}
				if color == "white"{
					one_color = "black"
					break
				}
			}
			if one_color =="white" {
				all[h] = buf
				h++
				stack[x]= buf
				x++
			}
			color = "white"
			for k = 0; k < h; k++ {
				if all[k].top != t.top {
					continue
				}
				color = "white"
				for l = 0; l < all[k].top; l++{
					if all[k].array[l] != t.array[l]{
						color = "black"
						break
					}
				}
				if color == "white"{
					break
				}
			}
			if one_color == "white"{
				a1[k][h-1].array[a1[k][h-1].top] = i
				a1[k][h-1].top++
			} else{
				a1[k][j].array[a1[k][j].top] = i
				a1[k][j].top++
			}
		}
	}
	return all, res1
}
func cicly_or_double(finals_new []Stack, Q []Stack, i int){
	var j, k int
	var c_or_d = "circle"
	var color = "white"
	for j = 0; finals_new[j].cap != 0; j++ {
		if finals_new[j].top != Q[i].top {
			continue
		}
		color = "white"
		for k = 0; k < finals_new[j].top; k++ {
			if finals_new[j].array[k] != Q[i].array[k] {
				color = "black"
				break
			}
		}
		if color == "white" {
			c_or_d = "doublecircle"
			break
		}
	}

	if c_or_d == "circle" {
		fmt.Printf("circle]\n")
	} else {
		fmt.Printf("doublecircle]\n")
	}
}
func main() {
	var n, m, q, j, k, x, or, of int
	var color, s string
	fmt.Scan(&n)
	fmt.Scan(&m)
	b := make([]int, n)
	a := make([][]Stack, n)
	for i := 0; i < n; i++{
		b[i] = i
		a[i] = make([] Stack, m + 1)
		for j = 0; j <= m; j++{
			a[i][j].array = make([]int, n)
			a[i][j].cap = n
			a[i][j].top = 0
		}
	}
	lambdaa := make(map[int] string)
	lambda := "lambda"
	x = 1
	for i := 0; i < m; i++{
		fmt.Scan(&or)
		fmt.Scan(&of)
		fmt.Scan(&s)
		color = "white"
		for j = 0; j < x; j++{
			if lambda == s{
				color = "black"
				break
			}
			if lambdaa[j] == s{
				color = "black"
				break
			}
		}
		if color == "white"{
			lambdaa[x] = s
			a[or][x].array[a[or][x].top] = of
			a[or][x].top++
			x++
		}
		if color == "black"{
			a[or][j].array[a[or][j].top] = of
			a[or][j].top++
		}
	}
	var res [] int
	res = make([] int, n)
	for i := 0; i < n; i++{
		fmt.Scan(&res[i])
	}
	fmt.Scan(&q)
	n *= 3
	a1 := make([][]Stack, n)
	for i := 0; i < n; i++{
		a1[i] = make([] Stack, n)
		for j = 0; j < n; j++{
			a1[i][j].array = make([]int, n)
			a1[i][j].cap = n
			a1[i][j].top = 0
		}
	}
	whrez, result := help(b, x, a, a1, res, q, n)
	fmt.Printf("digraph {\n\trankdir = LR\n\tdummy [label = \"\", shape = none]\n")
	for i := 0; whrez[i].cap != 0; i++ {
		fmt.Printf("\t%d [label = \"[", i)
		if whrez[i].top == 0{
			fmt.Printf("]\",  shape = ")
		} else {
			whrez[i].array = bubblesort(whrez[i].array, whrez[i].top - 1)
			for j = 0; j < whrez[i].top - 1; j++ {
				fmt.Printf("%d ", whrez[i].array[j])
			}
			fmt.Printf("%d]\", shape = ", whrez[i].array[whrez[i].top - 1])
		}
		cicly_or_double(result, whrez, i)
	}
	fmt.Printf("\tdummy -> %d\n", 0)
	for i := 0; whrez[i].cap != 0; i++{
		for j = 0; whrez[j].cap != 0; j++{
			if a1[i][j].top == 0 {
				continue
			}
			fmt.Printf("\t%d -> %d [label = \"", i, j)
			for k = 0; k < a1[i][j].top - 1; k++{
				fmt.Printf("%s, ", lambdaa[a1[i][j].array[k]])
			}
			fmt.Printf("%s\"]\n", lambdaa[a1[i][j].array[a1[i][j].top-1]])
		}
	}
	fmt.Printf("}")
}