package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class HamburgerDietNextPermutation_5215 {
	static int T;
	static int n, l, ans;
	static int[][] data;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		StringBuilder sb = new StringBuilder();

		T = Integer.parseInt(st.nextToken());
		for (int test_case = 1; test_case <= T; test_case++) {
			st = new StringTokenizer(br.readLine());
			n = Integer.parseInt(st.nextToken());
			l = Integer.parseInt(st.nextToken());
			data = new int[n][2];
			ans = 0;

			for (int i = 0; i < n; i++) {
				st = new StringTokenizer(br.readLine());
				data[i][0] = Integer.parseInt(st.nextToken());
				data[i][1] = Integer.parseInt(st.nextToken());
			}

			int limit = 1 << n;
			for (int k = 1; k <= n; k++) {
				int mask = (1 << k) - 1;
				while (mask < limit) {
					int sumCal = 0, sumTaste = 0;

					// 현재 mask 평가
					for (int i = 0; i < n; i++) {
						if ((mask & (1 << i)) != 0) {
							sumCal += data[i][1];
							if (sumCal > l) { // 칼로리 초과 → 가지치기
								sumTaste = Integer.MIN_VALUE;
								break;
							}
							sumTaste += data[i][0];
						}
					}
					ans = Math.max(ans, sumTaste);

					// 다음 조합 (Gosper’s hack: 같은 popcount 유지)
					int next = nextCombination(mask);
					if (next >= limit) break; // N비트 범위 넘어가면 종료
					mask = next;
				}
			}


			sb.append('#').append(test_case).append(' ').append(ans).append('\n');
		}
		System.out.print(sb);
	}

	static int nextCombination(int x) {
		int c = x & -x;      // 최하위 1비트
		int r = x + c;       // 그 비트를 올림
		return (((r ^ x) >>> 2) / c) | r;
	}
}
