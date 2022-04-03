import javax.swing.table.DefaultTableModel;
import java.util.Scanner;

class TriangleTable {
    public static void main(String[] args) {
        // implement me
        Scanner scanner = new Scanner(System.in);
        
        Object[] columns = new Object[] {"X", "Y"};
        Object[][] data = new Object[][] {
                {Integer.valueOf(scanner.next()), Integer.valueOf(scanner.next())},
                {Integer.valueOf(scanner.next()), Integer.valueOf(scanner.next())},
                {Integer.valueOf(scanner.next()), Integer.valueOf(scanner.next())},
        };

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        for (Object[] row : data) {
            model.addRow(row);
        }

        // do not remove the code below
        TableModelTest.test(model);
    }
}
