import java.util.LinkedList;
import java.util.Queue;

public class WordConvert {
	public int solution(String begin, String target, String[] words) {
		int answer = 0;
		boolean[] visited = new boolean[words.length];
		Queue<String> q = new LinkedList<>();
		q.offer(begin);

		while (!q.isEmpty()) {
			int size = q.size();
			for(int i=0; i < size; i++){
				String word = q.poll();

				if (word.equals(target)) {
					return answer;
				}

				for (int j=0; j < words.length; j++){
					if (!visited[j] && isPossibleConvert(word, words[j])) {
						visited[j] = true;
						q.offer(words[j]);
					}
				}
			}
			answer++;
		}

		return 0;
	}

	private boolean isPossibleConvert(String s1, String s2) {
		int cnt = 0;
		for (int i=0; i < s1.length(); i++) {
			if (s1.charAt(i) != s2.charAt(i)) {
				cnt++;
			}
		}
		return cnt == 1;
	}
}
