import java.io.*;
import java.util.*;

public class GridAnalyzer {
    char[][] grid;

    // Load grid from file
    public void load(String filename) throws IOException {
        List<char[]> rows = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while((line = br.readLine()) != null) {
                rows.add(line.toCharArray());
            }
        }
        grid = rows.toArray(new char[0][]);
    }

    // Print grid dimensions
    public void printSize() {
        System.out.println("Grid size: " + grid.length + " rows x " + grid[0].length + " columns");
    }

    // Count regions of a given character
    public int countRegions(char target) {
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        int regions = 0;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}}; // 4 directions

        for(int i=0;i<grid.length;i++) {
            for(int j=0;j<grid[0].length;j++) {
                if(!visited[i][j] && grid[i][j]==target) {
                    regions++;
                    Queue<int[]> q = new LinkedList<>();
                    q.add(new int[]{i,j});
                    visited[i][j] = true;
                    while(!q.isEmpty()) {
                        int[] cell = q.poll();
                        for(int[] d:dirs) {
                            int nx=cell[0]+d[0], ny=cell[1]+d[1];
                            if(nx>=0 && nx<grid.length && ny>=0 && ny<grid[0].length) {
                                if(!visited[nx][ny] && grid[nx][ny]==target) {
                                    visited[nx][ny]=true;
                                    q.add(new int[]{nx,ny});
                                }
                            }
                        }
                    }
                }
            }
        }
        return regions;
    }

    // Flood-fill operation
    public void floodFill(int x, int y, char newChar) {
        char target = grid[x][y];
        if(target == newChar) return;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{x,y});
        while(!stack.isEmpty()) {
            int[] cell = stack.pop();
            int cx=cell[0], cy=cell[1];
            if(grid[cx][cy]==target) {
                grid[cx][cy]=newChar;
                for(int[] d:dirs) {
                    int nx=cx+d[0], ny=cy+d[1];
                    if(nx>=0 && nx<grid.length && ny>=0 && ny<grid[0].length) {
                        if(grid[nx][ny]==target) stack.push(new int[]{nx,ny});
                    }
                }
            }
        }
    }

    // Build histogram
    public Map<Character,Integer> histogram() {
        Map<Character,Integer> hist = new HashMap<>();
        for(char[] row:grid) {
            for(char c:row) hist.put(c,hist.getOrDefault(c,0)+1);
        }
        return hist;
    }

    // Print grid
    public void printGrid() {
        for(char[] row:grid) System.out.println(new String(row));
    }

    public static void main(String[] args) throws Exception {
        GridAnalyzer analyzer = new GridAnalyzer();
        analyzer.load("grid.txt"); 

        analyzer.printSize();
        System.out.println("Histogram: " + analyzer.histogram());
        System.out.println("Regions of 'a': " + analyzer.countRegions('a'));
        System.out.println("Regions of 'b': " + analyzer.countRegions('b'));
        System.out.println("Regions of 'c': " + analyzer.countRegions('c'));
        System.out.println("Regions of 'd': " + analyzer.countRegions('d'));

        System.out.println("\nFlood-fill at (0,0) with 'x':");
        analyzer.floodFill(0,0,'x');
        analyzer.printGrid();
        System.out.println("New Histogram: " + analyzer.histogram());
    }
}
