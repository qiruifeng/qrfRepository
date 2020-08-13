package leetcode;

public class maxAreaOfIsland {
    public static int maxAreaOfIsland(char[][] grid) {
        if (grid == null) {
            return 0;
        }
        int ans = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    ans++;
                   infect(grid, i, j);
                }
            }
        }
        return ans;
    }

    /**
     * 感染函数
     *
     * @param matrix
     * @param i
     * @param j
     */
    private static void infect(char[][] matrix, int i, int j) {
        if (i < 0 || i >= matrix.length || j < 0 || j >= matrix[0].length || matrix[i][j] != '1') {
            return;
        }
        matrix[i][j] = '2';

        infect(matrix, i - 1, j);
        infect(matrix, i + 1, j);
        infect(matrix, i, j - 1);
        infect(matrix, i, j + 1);

    }

    public static void main(String[] args) {
        char[][] a  = {{'1', '1', '0', '0', '0'},
                       {'1', '1', '0', '0', '0'},
                       {'0', '0', '0', '1', '1'},
                       {'0', '0', '0', '1', '1'}};
        System.out.println(maxAreaOfIsland(a));


    }
}
