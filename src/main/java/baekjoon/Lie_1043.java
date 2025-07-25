package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Lie_1043 {
	static int n, m, ans;
	static int[][] party;
	static int[] parent;
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		party = new int[m][];
		parent = new int[n+1];
		
		for (int i = 1; i <= n; i++) {
			parent[i] = i;
		}
		
		st = new StringTokenizer(br.readLine());
		int k = Integer.parseInt(st.nextToken());
		int[] know = new int[k];
		for (int i = 0; i < k; i++) {
			know[i] = Integer.parseInt(st.nextToken());
		}
		
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			int partySize =  Integer.parseInt(st.nextToken());
			party[i] = new int[partySize];
			for(int j=0; j<partySize; j++) {
				party[i][j] = Integer.parseInt(st.nextToken());
			}
			
			// 파티 참여한 사람들 그룹화 
			for(int j=1; j<partySize; j++) {
				union(party[i][0], party[i][j]);
			}
		}
		
		//파티를 돌면서
		for (int i = 0; i < m; i++) {
			int res = party[i][0];
			boolean isNoJoin = true;
			// 진실 아는 사람 돌며
			for (int j=0; j < k; j++) {
				// 진실 아는 사람이 파티에 참여 했다
				if (find(know[j]) == find(res)) {
					isNoJoin = false;
					break;
				}
			}
			if (isNoJoin)
				ans++;
		}
		System.out.println(ans);
		
	}
	
	static int find(int a) {
		if (parent[a] == a)
			return a;
		return parent[a] = find(parent[a]);
	}
	
	static void union(int a, int b) {
		a = find(a);
		b = find(b);
		if (a < b) {
			parent[b] = a;
		} else if (b < a) {
			parent[a] = b;
		}
	}

}
