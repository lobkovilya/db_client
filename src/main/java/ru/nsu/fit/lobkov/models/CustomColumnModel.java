package ru.nsu.fit.lobkov.models;

import pro.batalin.ddl4j.model.Column;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Created by Ilya on 04.06.2017.
 */
public class CustomColumnModel extends DefaultTableModel {
    private List<Column> columns;

    public CustomColumnModel() {
        String[] cols = new String[] {
                "Name",
                "Type",
                "Default"
        };
        for (String s : cols) {
            addColumn(s);
        }
    }

    public void addDBColumn(Column dbColumn) {
        addRow(new Object[] {
                dbColumn.getName(),
                dbColumn.getType(),
                dbColumn.getDefaultValue()
        });
    }
}
