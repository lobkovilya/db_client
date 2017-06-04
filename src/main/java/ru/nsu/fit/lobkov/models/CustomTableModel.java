package ru.nsu.fit.lobkov.models;

import pro.batalin.ddl4j.model.Column;
import pro.batalin.ddl4j.model.Table;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
 * Created by Ilya on 04.06.2017.
 */
public class CustomTableModel extends DefaultTableModel {
    private Table table;
    private DatabaseModel dbModel;

    public CustomTableModel(DatabaseModel dbModel, Table table) throws SQLException {
        this.table = table;
        this.dbModel = dbModel;

        List<Column> columns = table.getColumns();
        for (Column c : columns) {
            addColumn(c.getName());
        }

        loadRows();
    }

    private void loadRows() throws SQLException {
        ResultSet rs = dbModel.loadRows(table);
        int columnCount = table.getColumns().size();

        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            addRow(vector);
        }
    }

}
