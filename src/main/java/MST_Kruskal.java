import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MST_Kruskal {
	
	static class Edge implements Comparable<Edge> {
		int start;
		int end;
		int weight;

		Edge(int start, int end, int weight) {
			this.start = start;
			this.end = end;
			this.weight = weight;
		}

		@Override
		public int compareTo(Edge o) {
			return Integer.compare(weight, o.weight);
		}
	}

	static Edge[] edgeList;
	static int[] parents;
	static int V, E;

	public static void make() {
		for (int i = 0; i < V; i++) {
			parents[i] = i;
		}
	}

	public static int find(int a) {
		if (parents[a] == a) return a;
		return parents[a] = find(parents[a]);
	}

	public static boolean union(int a, int b) {
		a = find(a);
		b = find(b);
		if (a == b) return false;
		if (a < b) parents[b] = a;
		else parents[a] = b;
		return true;
	}

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		br = new BufferedReader(new StringReader(src));
		StringTokenizer st = new StringTokenizer(br.readLine().trim());
		
		V = Integer.parseInt(st.nextToken());//정점 수
		E = Integer.parseInt(st.nextToken());//간선 수

		parents = new int[V];
		edgeList = new Edge[E];
		make();
		for (int i = 0; i < E; ++i) {
			st = new StringTokenizer(br.readLine().trim());
			int from = Integer.parseInt(st.nextToken());//시작정점
			int to = Integer.parseInt(st.nextToken());//끝정점
			int weight = Integer.parseInt(st.nextToken());//가중치
			edgeList[i] = new Edge(from, to, weight);
		}
		Arrays.sort(edgeList);

		int minCost = 0;
		int edgeCount = 0;
		
		for (Edge edge : edgeList) {
			int a = edge.start;
			int b = edge.end;
			if (find(a) != find(b)) {
				union(a, b);
				minCost += edge.weight;
				edgeCount++;
			}
			if (edgeCount == V-1) break;
		}
		
		System.out.println(minCost);
	}
	
	static String src = "5 10\r\n" + 
			"0 1 5\r\n" + 
			"0 2 10\r\n" + 
			"0 3 8\r\n" + 
			"0 4 7\r\n" + 
			"1 2 2\r\n" + 
			"1 3 3\r\n" + 
			"1 4 6\r\n" + 
			"2 3 4\r\n" + 
			"2 4 9\r\n" + 
			"3 4 1";
	
	static String src2 = "7 11\n"
			+ "0 1 3\n"
			+ "0 2 17\n"
			+ "0 3 6\n"
			+ "1 3 5\n"
			+ "1 6 12\n"
			+ "2 4 10\n"
			+ "2 5 8\n"
			+ "3 4 9\n"
			+ "4 5 4\n"
			+ "4 6 2\n"
			+ "5 6 14";
}// end class
/*
크루스칼 시간 복잡도 

1. makeSet()      ==> 시간 복잡도   O(v)      
2. Edge정렬       ==> 시간 복잡도   O(ElogE)  
3. union         ==> 시간 복잡도   O(E)      
                                        
결국 O(v+ElogE+E)  => O(ElogE) 
 */