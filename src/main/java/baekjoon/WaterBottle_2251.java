package baekjoon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Objects;

public class WaterBottle_2251 {
	static int a, b, c;
	static int[] arr = new int[3];
	static int[] total;
	static HashSet<Integer> ans = new HashSet<>();
	static HashSet<State> visited = new HashSet<>();
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		a = Integer.parseInt(st.nextToken());
		b = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		arr[2] = c;
		total = new int[] {a, b, c};
		dfs();
		TreeSet<Integer> sortedAns = new TreeSet<>(ans);
		for (int i : sortedAns) {
			System.out.print(i);
			System.out.print(" ");
		}
	}
	 
	static void dfs() {
		//방문 표사 후 a가 0이면 정답에 추가
		visited.add(new State(arr[0], arr[1], arr[2]));
		if (arr[0] == 0) {
			ans.add(arr[2]);
		}
		
		// 어디 있는 물을 따를지 선택
		for (int i = 0; i < 3; i++) {
			if (arr[i] == 0) continue;
			else {
				// 어디로 따를지
				for (int j=0; j < 3; j++) {
					// 셀프로는 못따름
					if (j == i) continue;
					// 얼마나 따를지
					int margin = Math.min(total[j] - arr[j], arr[i]);
					int[] nArr = Arrays.copyOf(arr, 3);
					nArr[j] += margin;
					nArr[i] -= margin;
					// 다음 상태가 방문한적 없다면 분기
					if (! visited.contains(new State(nArr[0], nArr[1], nArr[2]))) {
						arr[j] += margin;
						arr[i] -= margin;
						dfs();
						arr[j] -= margin;
						arr[i] += margin;
					}
				}
			}
		}
		
	}

}

class State {
	int a, b, c;
	
	State(int a, int b, int c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return a == state.a && b == state.b && c == state.c;
	}
	
	@Override
	public int hashCode() {
		 return Objects.hash(a, b, c);
	}
}
