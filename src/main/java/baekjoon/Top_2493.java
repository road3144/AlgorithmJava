package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Stack;
import java.util.StringTokenizer;

public class Top_2493 {

    static class Node {
        int idx;
        int value;

        Node(int idx, int value){
            this.idx = idx;
            this.value = value;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        Stack<Node> stack = new Stack<>();
        StringBuilder sb = new StringBuilder();

        int n = Integer.parseInt(st.nextToken());
        boolean can = false;

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            int now = Integer.parseInt(st.nextToken());

            if(stack.isEmpty()) {
                sb.append("0 ");
                stack.add(new Node(0, now));
            } else if (stack.peek().value > now) {
                can = true;
                sb.append(stack.peek().idx + 1).append(" ");
                stack.add(new Node(i, now));
            } else if (stack.peek().value <= now) {
                while (!stack.isEmpty() && stack.peek().value <= now)
                    stack.pop();
                if (stack.isEmpty())
                    sb.append("0 ");
                else {
                    sb.append(stack.peek().idx + 1).append(" ");
                    can = true;
                }

                stack.add(new Node(i, now));
            }

        }
        if (can)
            System.out.println(sb);
        else
            System.out.println(0);
    }
}
