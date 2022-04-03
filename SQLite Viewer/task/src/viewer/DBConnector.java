package viewer;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnector {
    List<String[]> tableData ;      // create a list to keep data from the queries
    String url = "jdbc:sqlite:";
    SQLiteDataSource dataSource = new SQLiteDataSource();

    DBConnector(String url) {
        this.url = this.url + url;    //initialize url of the database by object creation
        dataSource.setUrl(this.url);  //point datasource object to the database with the initialized url
        System.out.println(this.url);
        //get all table names in the database with this statement and store them temporarily in tableData Arraylist of string arrays
        tableData = getQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';");
    }

    //method for connecting to the datasource and getting updated tableData list, according the entered database statement
    public List<String[]> getQuery(String sqlStatement){
        List<String[]> resultStData = new ArrayList<>();

        try (Connection con = dataSource.getConnection()) {
            if (con.isValid(5)) {
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlStatement);
                System.out.println("Connection is valid.");

                resultStData = getRowDataList(resultSet);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableData = resultStData;
        return resultStData;
    }

    /*Method to get all the data in the rows of a database and store them in a list as arrays of strings.
    Each value in the list is meant to represent a row of data from the result set*/
    public List<String[]> getRowDataList (ResultSet resultSet){
        List<String[]> rowDataArraysList = new ArrayList<>();

        try {
            String[] rowColNames = new String[resultSet.getMetaData().getColumnCount()];
            for (int i = 0; i < rowColNames.length; i++) {
                rowColNames[i] = resultSet.getMetaData().getColumnName(i+1);
            }
            rowDataArraysList.add(rowColNames);

            while (resultSet.next()) {
                String[] row = new String[resultSet.getMetaData().getColumnCount()];
                for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                    row[i] = resultSet.getString(i + 1);
                }
                rowDataArraysList.add(row);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return rowDataArraysList;
    }

    public List<String[]> gatTableData(){
        return this.tableData;
    }
}
