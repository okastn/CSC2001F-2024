package parallelAbelianSandpile;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;


class SandpileUpdateTask extends RecursiveAction {
    private int[][] grid;
    private int[][] update;
    private int startRow, endRow;
    private static final int CUT = 100; // Adjust based on performance testing

    public SandpileUpdateTask(int[][] grid, int[][] update, int startRow, int endRow) {
        this.grid = grid;
        this.update = update;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    protected void compute() {
        if (endRow - startRow <= CUT) {
            update();
        } else {
            int midRow = (startRow + endRow) / 2;
            SandpileUpdateTask left = new SandpileUpdateTask(grid,update, startRow, midRow);
            SandpileUpdateTask right = new SandpileUpdateTask(grid,update, midRow, endRow);
            left.fork();
            right.compute();
            left.join();
            
        }
    }

    private void update() {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 1; j < grid[i].length - 1; j++) {
                update[i][j] = (grid[i][j] % 4) + 
                    (grid[i-1][j] / 4) +
                    (grid[i+1][j] / 4) +
                    (grid[i][j-1] / 4) + 
                    (grid[i][j+1] / 4);
            }
        }
    }
}