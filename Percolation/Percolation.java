import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int size;
    private int[][] board;
    private WeightedQuickUnionUF qfuf;
    private WeightedQuickUnionUF backwash;

    // create n-by-n grid, with all sites blocked
    /**
     * Create n-by-n grid with all sites blocked (0's)
     * 
     * @param n
     */
    public Percolation(int n) {

        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.size = n;
        // set up the board with all 0's
        this.board = new int[n][n];
        // create a weighted quick union type for every site in the board
        // the number of elements in the quick union is
        // total site + virtual top + virtual bottom
        this.qfuf = new WeightedQuickUnionUF(n * n + 2);
        // A backwash pervention uf with bottom site blocked
        this.backwash = new WeightedQuickUnionUF(n * n + 1);
        // connect top row to virtual top
        // for (int i = 0; i < n; i++) {
        // this.qfuf.union(n * n, i);
        // }

    }

    // open site (row, col) if it is not open already
    /**
     * Open site (row, col) if it is not open already Assume the row and col
     * passed in are indices starting at 1 i.e. for row = 1, the corresponding
     * index in the 2d array is 0
     * 
     * @param row
     * @param col
     */
    public void open(int row, int col) {

        if (row > this.size || row < 0 || col > this.size || col < 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        // open the site
        this.board[row - 1][col - 1] = 1;
        int p, q;
        // p is the current site
        p = (row - 1) * this.size + (col - 1);

        // connect to top
        if (row == 1) {
            this.qfuf.union(this.size * this.size, p);
            this.backwash.union(this.size * this.size, p);
        }

        // connect to bottom
        if (row == this.size) {
            this.qfuf.union(this.size * this.size + 1, p);
        }

        // connect the open site to it's neighboring sites
        if (row > 1 && (isOpen(row - 1, col))) {
            // q is the site above current site
            q = (row - 2) * this.size + (col - 1);
            this.qfuf.union(q, p);
            this.backwash.union(q, p);

        }
        if (row > 1 && col > 1 && (isOpen(row, col - 1))) {
            // q is the site to the left of current site
            q = (row - 1) * this.size + (col - 2);
            this.qfuf.union(q, p);
            this.backwash.union(q, p);
        }
        if (row < this.size && (isOpen(row + 1, col))) {
            // q is the site below current site
            q = (row) * this.size + (col - 1);
            this.qfuf.union(q, p);
            this.backwash.union(q, p);
        }
        if (row < this.size && col < this.size && (isOpen(row, col + 1))) {
            // q is the site to the right of current site
            q = (row - 1) * this.size + (col);
            this.qfuf.union(q, p);
            this.backwash.union(q, p);
        }
        // System.out.println("90 Parent is " + this.qfuf.find(90));
        // System.out.println("80 Parent is " + this.qfuf.find(80));
        // System.out.println("91 Parent is " + this.qfuf.find(91));
        // if (!isFull(row, col)) {
        // checkAndFull();
        // }
        return;
    }

    /**
     * Is site (row, col) open?
     * 
     * @param row
     * @param col
     * @return
     */
    public boolean isOpen(int row, int col) {

        if (row > this.size || row <= 0 || col > this.size || col <= 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        return this.board[row - 1][col - 1] == 1;
    }

    /**
     * Is site (row, col) full?
     * 
     * @param row
     * @param col
     * @return
     */
    public boolean isFull(int row, int col) {
        if (row > this.size || row <= 0 || col > this.size || col <= 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        // p is the current site
        int p = (row - 1) * this.size + (col - 1);
        return this.qfuf.connected(this.size * this.size, p) && this.backwash.connected(this.size * this.size, p);
    }

    /**
     * Return the number of open sites
     * 
     * @return
     */
    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.board[i][j] == 1) {
                    count++;
                }
            }
        }
        return count;

    }

    /**
     * Does this system percolate?
     * 
     * @return
     */
    public boolean percolates() {
        return this.qfuf.connected(this.size * this.size, this.size * this.size + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        return;
    }
}
