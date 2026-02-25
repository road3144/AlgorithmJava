package swea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


public class BigImage_25342 {
    private static BigImage_UserSolution2 userSolution = new BigImage_UserSolution2();

    private final static int CMD_INIT     = 100;
    private final static int CMD_ADD      = 200;
    private final static int CMD_PRESS    = 300;
    private final static int CMD_GET      = 400;

    private static int pixel[][] = new int[10000][3];

    private static boolean run(BufferedReader br) throws Exception
    {
        boolean ok = false;

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        int Q = Integer.parseInt(st.nextToken());
        for (int q = 0; q < Q; q++) {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == CMD_INIT) {
                int N = Integer.parseInt(st.nextToken());
                userSolution.init(N);
                ok = true;
            }
            else if (cmd == CMD_ADD) {
                int id = Integer.parseInt(st.nextToken());
                int size = Integer.parseInt(st.nextToken());
                int cnt = Integer.parseInt(st.nextToken());
                for (int i = 0; i < cnt; i++) {
                    st = new StringTokenizer(br.readLine(), " ");
                    pixel[i][0] = Integer.parseInt(st.nextToken());
                    pixel[i][1] = Integer.parseInt(st.nextToken());
                    pixel[i][2] = Integer.parseInt(st.nextToken());
                }
                userSolution.addPrint(id, size, cnt, pixel);
            }
            else if (cmd == CMD_PRESS) {
                int id = Integer.parseInt(st.nextToken());
                int row = Integer.parseInt(st.nextToken());
                int col = Integer.parseInt(st.nextToken());
                int dir = Integer.parseInt(st.nextToken());
                userSolution.pressPrint(id, row, col, dir);
            }
            else if(cmd == CMD_GET) {
                int row = Integer.parseInt(st.nextToken());
                int col = Integer.parseInt(st.nextToken());
                int ans = Integer.parseInt(st.nextToken());
                int ret = userSolution.getDepth(row, col);
                if(ans != ret) {
                    ok = false;
                }
            }
            else ok = false;
        }

        return ok;
    }

    public static void main(String[] args) throws Exception {

        System.setIn(new java.io.FileInputStream("./BType/BigImage/sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line = new StringTokenizer(br.readLine(), " ");

        int T = Integer.parseInt(line.nextToken());
        int MARK = Integer.parseInt(line.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }

        br.close();
    }
}

class BigImage_UserSolution {
    static int n;
    static Map<Integer, Image> imageMap;
    static List<Stamp> stamps;


    void init(int N) {
        n = N;
        imageMap = new HashMap<>();
        stamps = new ArrayList<>();
    }

    void addPrint(int mID, int mSize, int mCnt, int mPixel[][]) {
        List<Pixel> pixels = new ArrayList<>();
        for (int i = 0; i < mCnt; i++) {
            pixels.add(new Pixel(mPixel[i][0], mPixel[i][1], mPixel[i][2]));
        }
        imageMap.put(mID, new Image(mID, mSize, pixels));
    }

    void pressPrint(int mID, int mRow, int mCol, int mDir) {
        stamps.add(new Stamp(mID, mRow, mCol, imageMap.get(mID).size, mDir));
    }

    int getDepth(int mRow, int mCol){
        int ans = 0;
        for (Stamp stamp : stamps) {
            if (stamp.contain(mRow, mRow + 24, mCol, mCol + 24)) {
                List<Pixel> pixels = stamp.getPixels();
                for (Pixel pixel: pixels) {
                    if (pixel.in(mRow, mRow + 24, mCol, mCol + 24))
                        ans += pixel.v;
                }
            }
        }
        return ans;
    }

    static class Stamp {
        int mid, row, col, size, dir;

        Stamp(int mid, int row, int col, int size, int dir) {
            this.mid = mid;
            this.row = row;
            this.col = col;
            this.size = size;
            this.dir = dir;
        }

        public boolean contain(int r1, int r2, int c1, int c2) {
            if (row + size - 1 < r1) return false;
            if (r2 < row) return false;
            if (col + size - 1 < c1) return false;
            if (c2 < col) return false;
            return true;
        }

        public List<Pixel> getPixels() {
            Image image = imageMap.get(mid);
            List<Pixel> moved = new ArrayList<>();
            for (Pixel pixel : image.getTurned(dir)){
                moved.add(new Pixel(pixel.x + row, pixel.y + col, pixel.v));
            }
            return moved;
        }
    }

    static class Image {
        int mid, size;
        List<Pixel> pixels;

        Image(int mid, int size, List<Pixel> pixels) {
            this.mid = mid;
            this.size = size;
            this.pixels = pixels;
        }

        List<Pixel> getTurned(int dir) {
            List<Pixel> turned = new ArrayList<>();
            if (dir == 0) {
                for (Pixel pixel : pixels) {
                    int x = pixel.x, y = pixel.y, v = pixel.v;
                    turned.add(new Pixel(x, y, v));
                }
            }
            else if (dir == 1) {
                for (Pixel pixel : pixels) {
                    int x = pixel.x, y = pixel.y, v = pixel.v;
                    turned.add(new Pixel(size - 1 - y, x, v));
                }
            } else if (dir == 2) {
                for (Pixel pixel : pixels) {
                    int x = pixel.x, y = pixel.y, v = pixel.v;
                    turned.add(new Pixel(size - 1 - x, size - 1 - y, v));
                }
            } else if (dir == 3) {
                for (Pixel pixel : pixels) {
                    int x = pixel.x, y = pixel.y, v = pixel.v;
                    turned.add(new Pixel(y, size - 1 - x, v));
                }
            }
            return turned;
        }
    }

    static class Pixel {
        int x, y, v;

        Pixel(int x, int y, int v) {
            this.x = x;
            this.y = y;
            this.v = v;
        }
        boolean in(int r1, int r2, int c1, int c2) {
            if (x < r1) return false;
            if (r2 < x) return false;
            if (y < c1) return false;
            if (c2 < y) return false;
            return true;
        }
    }
}

// 픽셀 누적합 적용
class BigImage_UserSolution2 {
    static int n;
    static Map<Integer, Image> imageMap;
    static List<Stamp> stamps;


    void init(int N) {
        n = N;
        imageMap = new HashMap<>();
        stamps = new ArrayList<>();
    }

    void addPrint(int mID, int mSize, int mCnt, int mPixel[][]) {
        int[][][] image = new int[4][mSize][mSize];
        int[][] origin = new int[mSize][mSize];
        for (int i = 0; i < mCnt; i++) {
            origin[mPixel[i][0]][mPixel[i][1]] = mPixel[i][2];
        }
        int[][] tmp = new int[mSize][mSize];

        //0
        for (int i = 0; i < mSize; i++) {
            int rowSum = 0;
            for (int j = 0; j < mSize; j++) {
                rowSum += origin[i][j];
                image[0][i][j] = rowSum + ((i > 0) ? image[0][i-1][j] : 0);
            }
        }
        //1
        for (int i = 0; i < mSize; i++) {
            for (int j = 0; j < mSize; j++) {
                tmp[i][j] = origin[j][mSize-1-i];
            }
        }
        for (int i = 0; i < mSize; i++) {
            int rowSum = 0;
            for (int j = 0; j < mSize; j++) {
                rowSum += tmp[i][j];
                image[1][i][j] = rowSum + ((i > 0) ? image[1][i-1][j] : 0);
            }
        }
        //2
        for (int i = 0; i < mSize; i++) {
            for (int j = 0; j < mSize; j++) {
                tmp[i][j] = origin[mSize-1-i][mSize-1-j];
            }
        }
        for (int i = 0; i < mSize; i++) {
            int rowSum = 0;
            for (int j = 0; j < mSize; j++) {
                rowSum += tmp[i][j];
                image[2][i][j] = rowSum + ((i > 0) ? image[2][i-1][j] : 0);
            }
        }
        //3
        for (int i = 0; i < mSize; i++) {
            for (int j = 0; j < mSize; j++) {
                tmp[i][j] = origin[mSize-1-j][i];
            }
        }
        for (int i = 0; i < mSize; i++) {
            int rowSum = 0;
            for (int j = 0; j < mSize; j++) {
                rowSum += tmp[i][j];
                image[3][i][j] = rowSum + ((i > 0) ? image[3][i-1][j] : 0);
            }
        }


        imageMap.put(mID, new Image(mID, mSize, image));
    }

    void pressPrint(int mID, int mRow, int mCol, int mDir) {
        stamps.add(new Stamp(mID, mRow, mCol, imageMap.get(mID).size, mDir));
    }

    int getDepth(int mRow, int mCol){
        int ans = 0;
        for (Stamp stamp : stamps) {
            if (stamp.contain(mRow, mRow + 24, mCol, mCol + 24)) {
                // 픽셀 누적합 검사
                int r = stamp.row, c = stamp.col, size = stamp.size;
                int tr1 = Math.max(mRow, r);
                int tr2 = Math.min(mRow + 24, r + size -1);
                int tc1 = Math.max(mCol, c);
                int tc2 = Math.min(mCol + 24, c + size - 1);
                ans += stamp.getSums(tr1, tr2, tc1, tc2);
            }
        }
        return ans;
    }

    static class Stamp {
        int mid, row, col, size, dir;

        Stamp(int mid, int row, int col, int size, int dir) {
            this.mid = mid;
            this.row = row;
            this.col = col;
            this.size = size;
            this.dir = dir;
        }

        public boolean contain(int r1, int r2, int c1, int c2) {
            if (row + size - 1 < r1) return false;
            if (r2 < row) return false;
            if (col + size - 1 < c1) return false;
            if (c2 < col) return false;
            return true;
        }

        public int getSums(int r1, int r2, int c1, int c2) {
            Image image = imageMap.get(mid);
            int[][][] pixels = image.pixels;
            r1 -= row;
            r2 -= row;
            c1 -= col;
            c2 -= col;
            int sum = pixels[dir][r2][c2];
            if (r1 > 0) sum -= pixels[dir][r1-1][c2];
            if (c1 > 0) sum -= pixels[dir][r2][c1-1];
            if (r1 > 0 && c1 > 0) sum += pixels[dir][r1-1][c1-1];
            return  sum;
        }
    }

    static class Image {
        int mid, size;
        int[][][] pixels;

        Image(int mid, int size, int[][][] pixels) {
            this.mid = mid;
            this.size = size;
            this.pixels = pixels;
        }
    }
    
}