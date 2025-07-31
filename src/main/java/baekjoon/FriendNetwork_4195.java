package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class FriendNetwork_4195 {
	
	static int t;
	static Map<String, Integer> cnt;
	static Map<String, String> parent;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		t = Integer.parseInt(st.nextToken());
		
		for (int test = 0; test < t; test++) {
			st = new StringTokenizer(br.readLine());
			int n = Integer.parseInt(st.nextToken());
			parent = new HashMap<>();
			cnt = new HashMap<>();
			
			for (int i=0; i < n; i++) {
				st = new StringTokenizer(br.readLine());
				String a = st.nextToken();
				String b = st.nextToken();
				if (!parent.containsKey(a)){
					parent.put(a, a);
					cnt.put(a, 1);
				}
				
				if (!parent.containsKey(b)) {
					parent.put(b, b);
					cnt.put(b, 1);
				}

				union(a, b);

				System.out.println(cnt.get(find(a)));

			}

		}
	}
	
	static String find(String name) {
		if (parent.get(name).equals(name))
			return name;
		String root = find(parent.get(name));
		parent.put(name, root);
		return root;
	}
	
	static void union(String a, String b) {
		a = find(a);
		b = find(b);
		
		if (a.compareTo(b) < 0) {
			parent.put(b, a);
			cnt.put(a, cnt.get(a) + cnt.get(b));
			cnt.put(b, cnt.get(a));
		} else if (a.compareTo(b) > 0) {
			parent.put(a, b);
			cnt.put(b, cnt.get(b) + cnt.get(a));
			cnt.put(a, cnt.get(b));
		}
	}

}
