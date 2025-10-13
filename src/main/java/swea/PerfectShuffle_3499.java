package swea;

import java.util.*;

public class PerfectShuffle_3499 {

    public static List<String> perfectShuffle(int N, List<String> cards) {
        int half = (N + 1) / 2;

        List<String> firstHalf = cards.subList(0, half);
        List<String> secondHalf = cards.subList(half, N);

        List<String> shuffled = new ArrayList<>();
        for (int i = 0; i < half; i++) {
            shuffled.add(firstHalf.get(i));
            if (i < secondHalf.size()) {
                shuffled.add(secondHalf.get(i));
            }
        }
        return shuffled;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int T = sc.nextInt();
        sc.nextLine();

        for (int t = 0; t < T; t++) {
            int N = sc.nextInt();
            sc.nextLine();

            List<String> cards = new ArrayList<>();
            String[] cardNames = sc.nextLine().split(" ");
            for (String card : cardNames) {
                cards.add(card);
            }

            List<String> shuffledCards = perfectShuffle(N, cards);
            System.out.print("#" + (t+1) + " ");
            System.out.println(String.join(" ", shuffledCards));
        }

        sc.close();
    }
}
