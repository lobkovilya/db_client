package ru.nsu.fit.lobkov.models;

import com.sun.javafx.image.IntPixelGetter;
import pro.batalin.ddl4j.DatabaseOperationException;
import pro.batalin.ddl4j.model.Column;
import pro.batalin.ddl4j.model.Table;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ilya on 04.06.2017.
 */
public class CustomTableModel extends DefaultTableModel {
    private Table table;
    private DatabaseModel dbModel;

    private Map<Integer, Set<Integer>> changes = new HashMap<>();

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

    @Override
    public void setValueAt(Object aValue, int row, int column) {
//        changes.computeIfAbsent(row, k -> new HashSet<>());
//        changes.get(row).add(column);
        super.setValueAt(aValue, row, column);
        if (row == getRowCount() - 1) {
//            System.out.println("Hello");
            return;
        }

        System.out.println(aValue);
        try {
            int columnCount = table.getColumns().size();


            List<String> conditions = new ArrayList<>();
            for (int i = 0; i < columnCount; i++) {
                if (i == column) {
                    continue;
                }
                String value = getValueAt(row, i).toString();
                String columnName = getColumnName(i);
                conditions.add(columnName + "='" + value + "'");
            }
            String strJoin = String.join(" AND ", conditions);
            String condition = " WHERE " + strJoin;

            StringBuilder queryBuilder = new StringBuilder("UPDATE ")
                    .append(table.getFullName())
                    .append(" SET ")
                    .append(getColumnName(column))
                    .append(" = ")
                    .append("'" + aValue + "'")
                    .append(condition);

            dbModel.executeQuery(queryBuilder.toString());
        } catch (DatabaseOperationException e) {
            e.printStackTrace();
        }
    }

    public void commitChanges() throws DatabaseOperationException {
        int rowToCommit = getRowCount() - 1;
        List<String> columnNames = table.getColumns().stream().map(Column::getName).collect(Collectors.toList());
        String columns = "(" + String.join(", ", columnNames) + ")";

        List<String> valueNames = new ArrayList<>();
        for (int i = 0; i < getColumnCount(); i++) {
            valueNames.add("'" + getValueAt(rowToCommit, i).toString() + "'");
        }

        String values = "(" + String.join(", ", valueNames) + ")";

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder
                .append(table.getFullName())
                .append(" ")
                .append(columns)
                .append(" VALUES ")
                .append(values);

        dbModel.executeQuery(queryBuilder.toString());

    }

    @Override
    public void removeRow(int row) {
        try {
            List<String> conditions = new ArrayList<>();
            for (int i = 0; i < getColumnCount(); i++) {
                String value = getValueAt(row, i).toString();
                String columnName = getColumnName(i);
                conditions.add(columnName + "='" + value + "'");
            }
            String strJoin = String.join(" AND ", conditions);
            String condition = " WHERE " + strJoin;

            StringBuilder queryBuilder = new StringBuilder("DELETE FROM ");
            queryBuilder
                    .append(table.getFullName())
                    .append(" ")
                    .append(condition);


            dbModel.executeQuery(queryBuilder.toString());
            super.removeRow(row);
        } catch (DatabaseOperationException e) {
            e.printStackTrace();
        }


    }
}
