import java.util.*;
import java.util.function.*;

class Element {
    protected String name;
    protected Element next;
    public Element(String s) {
        name = s;
        next = null;
    }

    public void add(String s) {
        Element temp = new Element(s);
        Element t = this;
        while (t.next != null)
            t = t.next;
        t.next = temp;
    }
}
public class FormulaOrder {
    private static ArrayList<Element> left = new ArrayList<>();
    private static HashSet<String> allnames = new HashSet<>();
    private static Graph g;
    protected static int commas;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String buf;
        while (in.hasNextLine()) {
            String x = in.nextLine();
            commas = 0;
            if (x.equals("")) break;
            String[] parts = x.split("=");

            if (((parts.length == 2) && (!allnames.contains(parts[0].trim())) && !(parts[0].equals("")) && !(parts[1].equals("")))) allnames.add(parts[0].trim());
            else {
                System.out.println("syntax error");
                return;
            }
            String name = parts[0].trim();
            Element temp = new Element(name);

            for (String a: parts[0].split(",")) {
                buf = a.trim();
                commas++;
                if (a.equals(parts[0])) break;

                if (!allnames.contains(buf)) {
                    allnames.add(buf);
                    temp.add(buf);
                }
                else {
                    System.out.println("syntax error");
                    return;
                }
            }

            try {
                if (parts[0].matches("^[a-zA-Z][a-zA-Z0-9,\\s]*"))
                    left.add(temp);
                else {
                    System.out.println("syntax error");
                    return;
                }

                HashSet<String> names = new HashSet<>();
                names.add(x);
                Calc.formula.put(temp, names);
                Calc.setCur(names);
                if (parts.length == 2)
                    Calc.parse(parts[1]);
                if (commas - 1 != 0)  {
                    System.out.println("syntax error");
                    return;
                }
            }
            catch (SyntaxError e) {
                System.out.println(e.getMessage());
                return;
            }
        }

        int counter = -1;
        g = new Graph(left.size());
        for (Element s: left) {
            counter++;
            HashSet<String> buffer = Calc.formula.get(s);

            for (String x: buffer) {
                if ((!allnames.contains(x)) && (!x.contains("="))) {
                    System.out.println("syntax error");
                    return;
                }

                int n = -1;
                for (Element a: left) {
                    Element temp = a;
                    while (temp != null) {
                        if (temp.name.equals(x)) {
                            n = left.indexOf(a);
                            break;
                        }
                        temp = temp.next;
                    }
                }
                if (n == -1) {
                    g.get(counter).set(x);
                    continue;
                }
                g.add(counter, n);
            }
        }

        boolean cycle;
        for (Element key: left) {
            for (String x: Calc.formula.get(key)) {
                if (x.contains("=")) continue;
                if (!allnames.contains(x)) {
                    System.out.println("syntax error");
                    return;
                }
            }
        }

        for (int i = 0; i < g.size(); i++) {
            cycle = g.dfs(i);
            if (cycle) {
                System.out.println("cycle");
                return;
            }
        }

        g.order.stream().forEach((x) -> { System.out.println(x);});
    }
}

class Position {
    protected String text;
    protected int index;

    public Position(String text) { this(text, 0); }

    private Position(String text, int index) {
        this.text = text;
        this.index = index;
    }

    public int getChar() {
        return index < text.length() ? text.codePointAt(index) : -1;
    }

    public boolean satisfies(IntPredicate p) { return p.test(getChar()); }

    public Position skip() {
        int c = getChar();
        if (c == -1) return this;
        return new Position(text, index + (c > 0xFFFF ? 2 : 1));
    }

    public Position skipWhile(IntPredicate p) {
        Position pos = this;
        while (pos.satisfies(p)) pos = pos.skip();
        return pos;
    }
}

class SyntaxError extends Exception {
    public SyntaxError(Position pos, String msg) { super("syntax error"); }
}

enum Tag { MUL, DIV, SUB, ADD, LPAREN, RPAREN, NUMBER, IDENT, COMMA, END_OF_TEXT; }

class Token {
    protected Tag tag;
    protected Position start, follow;

    public Token(String text) throws SyntaxError { this(new Position(text)); }
    private Token(Position cur) throws SyntaxError {
        start = cur.skipWhile(Character::isWhitespace);
        follow = start.skip();
        if (start.index >= follow.text.length()) tag = Tag.END_OF_TEXT;
        else {
            switch (start.getChar()) {
                case '(':
                    tag = Tag.LPAREN;
                    break;
                case ')':
                    tag = Tag.RPAREN;
                    break;
                case '+':
                    tag = Tag.ADD;
                    break;
                case '-':
                    tag = Tag.SUB;
                    break;
                case '*':
                    tag = Tag.MUL;
                    break;
                case '/':
                    tag = Tag.DIV;
                    break;
                case ',':
                    tag = Tag.COMMA;
                    break;
                default:
                    if (start.satisfies(Character::isLetter)) {
                        follow = follow.skipWhile(Character::isLetterOrDigit);
                        tag = Tag.IDENT;
                    } else if (start.satisfies(Character::isDigit)) {
                        follow = follow.skipWhile(Character::isDigit);
                        if (follow.satisfies(Character::isLetter)) {
                            throw new SyntaxError(follow, "delimiter expected");
                        }
                        tag = Tag.NUMBER;
                    } else {
                        throwError("invalid character");
                    }
            }
        }
    }

    public void throwError(String msg) throws SyntaxError {
        throw new SyntaxError(start, msg);
    }
    public boolean matches(Tag ...tags) {
        return Arrays.stream(tags).anyMatch(t -> tag == t);
    }
    public Token next() throws SyntaxError {
        return new Token(follow);
    }
}

class Calc{
    private static Token sym;
    protected static Hashtable<Element, HashSet<String>> formula = new Hashtable<>();
    private static HashSet<String> cur;

    private static void expect(Tag tag) throws SyntaxError {
        if (!sym.matches(tag)) sym.throwError(tag.toString() + " expected");
        sym = sym.next();
    }

    public static void setCur(HashSet<String> cur) {Calc.cur = cur;}
    public static void parse(String txt) throws SyntaxError {
        sym = new Token(txt);
        parseE();
        expect(Tag.END_OF_TEXT);
    }

    private static void parseE() throws SyntaxError {
        parseT();
        parseEE();
    }

    private static void parseT() throws SyntaxError {
        parseF();
        parseTT();
    }

    private static void parseF() throws SyntaxError {
        String buf = sym.start.text.substring(sym.start.index, sym.follow.index);
        if (sym.matches(Tag.NUMBER)) {
            sym = sym.next();
            return;
        }

        if (sym.matches(Tag.LPAREN)) {
            expect(Tag.LPAREN);
            int count = 0;
            Token buffer = sym;
            for (int i = sym.start.index; i < sym.start.text.length(); i++) {
                if ((sym.start.text.charAt(i) == ')') && (count == 0)) {
                    parse(sym.start.text.substring(sym.start.index, i));
                    break;
                }
                if (sym.start.text.charAt(i) == ')') count--;
                if (sym.start.text.charAt(i) == '(') count++;
                if (i == sym.start.text.length() - 1) sym.throwError("paren");
            }

            count = 0;
            while (true) {
                if ((buffer.tag == Tag.RPAREN) && (count == 0)) break;
                if (buffer.tag == Tag.LPAREN) count++;
                if (buffer.tag == Tag.RPAREN) count--;
                buffer = buffer.next();
            }

            sym = buffer;
            expect(Tag.RPAREN);
            return;
        }
        if (sym.matches(Tag.IDENT)) {
            sym = sym.next();
            cur.add(buf);
            return;
        }
        if (sym.matches(Tag.SUB)) {
            sym = sym.next();
            parseF();
            return;
        }
        sym.throwError("F - problems");
    }

    private static void parseEE() throws SyntaxError {
        if (sym.matches(Tag.ADD, Tag.SUB)) {
            sym = sym.next();
            parseT();
            parseEE();
        }
    }

    private static void parseTT() throws SyntaxError{
        if (sym.matches(Tag.MUL, Tag.DIV, Tag.COMMA)) {
            if (sym.matches(Tag.COMMA)) FormulaOrder.commas--;
            sym = sym.next();
            parseF();
            parseTT();
        }
    }
}

class Vertex {
    protected Arc next = null, last = null;
    protected final int value;
    protected String label;
    protected int index = 0;
    public Vertex(int i) { value = i; }
    public void set(String s) {this.label = s; }
}

class Arc {
    protected Arc next = null;
    protected final int from, to;

    public Arc(int from, int to) {
        this.from = from;
        this.to = to;
    }
}

class Graph {
    private Vertex[] graph;
    private int vertex;
    protected ArrayList<String> order = new ArrayList<>();

    public Graph(int vert) {
        graph = new Vertex[vertex = vert];
        for (int i = 0; i < vertex; i++) graph[i] = new Vertex(i);
    }

    public Vertex get(int i) {
        return graph[i];
    }

    public int size() {
        return vertex;
    }

    public void add(int from, int to) {
        Arc temp = graph[from].next;
        Arc a = new Arc(from, to);
        if (graph[from].next != null) {

            while (temp.next != null) {
                if ((temp.to == to) && (temp.from == from)) return;
                temp = temp.next;
            }
            if ((temp.to == to) && (temp.from == from)) return;
            temp.next = a;
        } else graph[from].next = a;
    }

    public boolean dfs(int i) {
        if (graph[i].index == 1) return true;
        if (graph[i].index == 2) return false;
        graph[i].index = 1;

        for (Arc x = graph[i].next; x != null; x = x.next)
            if (dfs(x.to)) return true;

        order.add(graph[i].label);
        graph[i].index = 2;
        return false;
    }
}