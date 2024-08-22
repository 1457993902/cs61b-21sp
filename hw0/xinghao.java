public class xinghao {
    public static void main(String[] args) {
        int col;
        int row = 0;
        while (row < 5) {
            col = 0;
            while (col <= row) {
                System.out.print('*');
                col += 1;
            }
            System.out.println();
            row += 1;
        }
    }
}