package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Remocon_1107 {
	static int n, m;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(br.readLine());
		m = Integer.parseInt(st.nextToken());
		if (m != 0)
			st = new StringTokenizer(br.readLine());
		boolean[] broken = new boolean[10];
		for (int i = 0; i < m; i++) {
			broken[Integer.parseInt(st.nextToken())] = true;
		}
		int ans = Math.abs(100 - n);

		for (int i = 0; i <= 999999; i++) {
			String num = String.valueOf(i);
			boolean isBroken = false;

			for (int j = 0; j < num.length(); j++) {
				if (broken[num.charAt(j) - '0']) {
					isBroken = true;
					break;
				}
			}
			if (!isBroken) {
				ans = Math.min(ans, Math.abs(n - i) + num.length());
			}
		}

		System.out.println(ans);
	}

}
