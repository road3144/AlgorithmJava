import java.util.*;

public class FarthestNode {
	ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
	boolean[] visited;
	public int solution(int n, int[][] edge) {
		visited = new boolean[n+1];
		for (int i=0; i < n+1; i++) graph.add(new ArrayList<>());

		for (int i[] : edge) {
			graph.get(i[0]).add(i[1]);
			graph.get(i[1]).add(i[0]);
		}
		return bfs();
	}

	private int bfs() {
		int answer = 0;
		Queue<int[]> queue = new LinkedList<>();
		int maxDepth = 0;

		visited[1] = true;
		queue.offer(new int[] {1, 0});
		while (!queue.isEmpty()) {
			int[] now = queue.poll();
			int v = now[0];
			int depth = now[1];

			if (maxDepth == depth) answer++;
			else if (maxDepth < depth) {
				maxDepth = depth;
				answer = 1;
			}

			for (int i : graph.get(v)) {
				if (!visited[i]) {
					queue.offer(new int[] {i, depth+1});
					visited[i] = true;
				}
			}
		}
		return answer;
	}
}
