public class ParcelBox {
	public int solution(int[] order) {
		int answer = 0;
		int[] sub = new int[order.length];
		int head = -1;
		int main = 1;
		while (main < order.length + 1) {
			if (order[answer] != main) {
				sub[++head] = main;
			} else {
				answer++;
			}
			while (head > -1 && sub[head] == order[answer]) {
				answer++;
				head--;
			}
			main++;
		}
		return answer;
	}
}
