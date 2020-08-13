package lessonFromZuo.basic_class_03;

public class islandNumber {
    public static int countIslandNumber(int[][] m) {
        if (m == null) {
            return 0;
        }

        int h = m.length;
        int l = m[0].length;
        int result = 0;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < l; j++) {
                if (m[i][j] == 1) {
                    result++;
                    infect(m, i, j, h, l);
                }
            }
        }
        return result;
    }

    public static void infect(int[][] m, int i, int j, int h, int l) {
        if (i >= h || j >= l || i < 0 || j < 0 || m[i][j] != 1) {
            return;
        }
        m[i][j] = 2;
        infect(m, i + 1, j, h, l);
        infect(m, i - 1, j, h, l);
        infect(m, i, j + 1, h, l);
        infect(m, i, j - 1, h, l);
    }

    public static void main(String[] args) {
        int[][] m1 = {{0, 0, 0, 0, 0, 0, 0, 0, 0},
                      {0, 1, 1, 1, 0, 1, 1, 1, 0},
                      {0, 1, 1, 1, 0, 0, 0, 1, 0},
                      {0, 1, 1, 0, 0, 0, 0, 0, 0},
                      {0, 0, 0, 0, 0, 1, 1, 0, 0},
                      {0, 0, 0, 0, 1, 1, 1, 0, 0},
                      {0, 0, 0, 0, 0, 0, 0, 0, 0},};
        System.out.println(countIslandNumber(m1));

        int[][] m2 = {{0, 0, 0, 0, 0, 0, 0, 0, 0},
                      {0, 1, 1, 1, 1, 1, 1, 1, 0},
                      {0, 1, 1, 1, 0, 0, 0, 1, 0},
                      {0, 1, 1, 0, 0, 0, 1, 1, 0},
                      {0, 0, 0, 0, 0, 1, 1, 0, 0},
                      {0, 0, 0, 0, 1, 1, 1, 0, 0},
                      {0, 0, 0, 0, 0, 0, 0, 0, 0},};
        System.out.println(countIslandNumber(m2));

    }
}
