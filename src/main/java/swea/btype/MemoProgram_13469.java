package swea.btype;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MemoProgram_13469 {

    private final static int CMD_INIT       = 100;
    private final static int CMD_INSERT     = 200;
    private final static int CMD_MOVECURSOR = 300;
    private final static int CMD_COUNT      = 400;

    private final static MemoProgram_UserSolution usersolution = new MemoProgram_UserSolution();

    private static void String2Char(char[] buf, String str, int maxLen)
    {
        for (int k = 0; k < str.length(); k++)
            buf[k] = str.charAt(k);

        for (int k = str.length(); k <= maxLen; k++)
            buf[k] = '\0';
    }

    private static char[] mStr = new char[90001];

    private static boolean run(BufferedReader br) throws Exception
    {
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        int queryCnt = Integer.parseInt(st.nextToken());
        boolean correct = false;

        for (int q = 0; q < queryCnt; q++)
        {
            st = new StringTokenizer(br.readLine(), " ");

            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == CMD_INIT)
            {
                int H = Integer.parseInt(st.nextToken());
                int W = Integer.parseInt(st.nextToken());

                String2Char(mStr, st.nextToken(), 90000);

                usersolution.init(H, W, mStr);
                correct = true;
            }
            else if (cmd == CMD_INSERT)
            {
                char mChar = st.nextToken().charAt(0);

                usersolution.insert(mChar);
            }
            else if (cmd == CMD_MOVECURSOR)
            {
                int mRow = Integer.parseInt(st.nextToken());
                int mCol = Integer.parseInt(st.nextToken());

                char ret = usersolution.moveCursor(mRow, mCol);

                char ans = st.nextToken().charAt(0);
                if (ret != ans)
                {
                    correct = false;
                }
            }
            else if (cmd == CMD_COUNT)
            {
                char mChar = st.nextToken().charAt(0);

                int ret = usersolution.countCharacter(mChar);

                int ans = Integer.parseInt(st.nextToken());
                if (ret != ans)
                {
                    correct = false;
                }
            }
        }
        return correct;
    }

    public static void main(String[] args) throws Exception
    {
        int TC, MARK;

        System.setIn(new java.io.FileInputStream("./BType/MemoProgram/sample_input_20.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        TC = Integer.parseInt(st.nextToken());
        MARK = Integer.parseInt(st.nextToken());

        for (int testcase = 1; testcase <= TC; ++testcase)
        {
            int score = run(br) ? MARK : 0;

            System.out.println("#" + testcase + " " + score);
        }

        br.close();
    }
}

class MemoProgram_UserSolution {

    static final int BLOCK_SIZE = 300;

    static class Block {
        ArrayList<Character> list = new ArrayList<>();
        int[] cnt = new int[26];
    }

    static int h, w, total, cursor;
    static ArrayList<Block> blocks;

    void init(int H, int W, char mStr[]) {
        h = H;
        w = W;
        total = 0;
        cursor = 0;
        blocks = new ArrayList<>();

        Block block = new Block();

        for (int i = 0; mStr[i] != '\0'; i++) {
            block.list.add(mStr[i]);
            block.cnt[mStr[i] - 'a']++;
            total++;

            if (block.list.size() >= BLOCK_SIZE) {
                blocks.add(block);
                block = new Block();
            }
        }

        if (!block.list.isEmpty()) {
            blocks.add(block);
        }

        if (blocks.isEmpty()) {
            blocks.add(new Block());
        }
    }

    void insert(char mChar) {
        Pos p = findPos(cursor);
        Block b = blocks.get(p.blockIdx);

        b.list.add(p.innerIdx, mChar);
        b.cnt[mChar - 'a']++;
        total++;
        cursor++;

        if (b.list.size() > BLOCK_SIZE * 2) {
            splitBlock(p.blockIdx);
        }
    }

    char moveCursor(int mRow, int mCol) {
        int idx = (mRow - 1) * w + (mCol - 1);

        if (idx > total) idx = total;
        cursor = idx;

        if (cursor == total) return '$';

        Pos p = findPos(cursor);
        Block b = blocks.get(p.blockIdx);

        if (p.innerIdx == b.list.size()) {
            p.blockIdx++;
            p.innerIdx = 0;
        }

        return blocks.get(p.blockIdx).list.get(p.innerIdx);
    }

    int countCharacter(char mChar) {
        int target = mChar - 'a';
        int remain = cursor;
        int answer = 0;

        for (int i = 0; i < blocks.size(); i++) {
            Block b = blocks.get(i);
            int size = b.list.size();

            if (remain >= size) {
                remain -= size;
                continue;
            }

            for (int j = remain; j < size; j++) {
                if (b.list.get(j) - 'a' == target) answer++;
            }

            for (int j = i + 1; j < blocks.size(); j++) {
                answer += blocks.get(j).cnt[target];
            }
            break;
        }

        return answer;
    }

    static class Pos {
        int blockIdx, innerIdx;

        Pos(int blockIdx, int innerIdx) {
            this.blockIdx = blockIdx;
            this.innerIdx = innerIdx;
        }
    }

    Pos findPos(int idx) {
        int sum = 0;

        for (int i = 0; i < blocks.size(); i++) {
            int size = blocks.get(i).list.size();
            if (idx <= sum + size - 1) {
                return new Pos(i, idx - sum);
            }
            if (idx == sum + size) {
                return new Pos(i, size);
            }
            sum += size;
        }

        int last = blocks.size() - 1;
        return new Pos(last, blocks.get(last).list.size());
    }

    void splitBlock(int blockIdx) {
        Block cur = blocks.get(blockIdx);
        Block newBlock = new Block();

        int moveStart = cur.list.size() / 2;
        int originalSize = cur.list.size();

        for (int i = moveStart; i < originalSize; i++) {
            char ch = cur.list.get(i);
            newBlock.list.add(ch);
            newBlock.cnt[ch - 'a']++;
        }

        for (int i = originalSize - 1; i >= moveStart; i--) {
            char ch = cur.list.remove(i);
            cur.cnt[ch - 'a']--;
        }

        blocks.add(blockIdx + 1, newBlock);
    }
}
