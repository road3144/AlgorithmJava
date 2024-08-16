public class Fatigue {
	static int cnt = 0;
	static boolean[] visited;
	static int n;
	public int solution(int k, int[][] dungeons) {
		n = dungeons.length;
		visited = new boolean[n];
		dfs(0, dungeons, k);
		return cnt;
	}

	private void dfs(int depth, int[][] dungeons, int status){
		for (int i=0; i < n; i++) {
			if (!visited[i] && status >= dungeons[i][0]) {
				visited[i] = true;
				dfs(depth + 1, dungeons, status - dungeons[i][1]);
				visited[i] = false;
			}
		}
		cnt = Math.max(cnt, depth);
	}
}
