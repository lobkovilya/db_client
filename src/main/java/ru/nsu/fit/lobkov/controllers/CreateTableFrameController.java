package ru.nsu.fit.lobkov.controllers;

import pro.batalin.ddl4j.DatabaseOperationException;
import pro.batalin.ddl4j.model.Column;
import pro.batalin.ddl4j.model.Table;
import ru.nsu.fit.lobkov.models.DatabaseModel;
import ru.nsu.fit.lobkov.view.ColumnCreatorElement;
import ru.nsu.fit.lobkov.view.CreateTableFrame;
import ru.nsu.fit.lobkov.view.ForeignKeyCreatorElement;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ilya on 04.06.2017.
 */
public class CreateTableFrameController {
    private CreateTableFrame createTableFrame;
    private DatabaseModel dbModel;

    public CreateTableFrameController(DatabaseModel dbModel) {
        this.dbModel = dbModel;
        createTableFrame = new CreateTableFrame(this);
        createTableFrame.setVisible(true);
    }

    public void addColumn() {
        if (createTableFrame.getActiveTab() == 0) {
            createTableFrame.addColumnCreatorView();
        } else {
            createTableFrame.addForeignKeyCreatorView();
        }
    }

    public void okBtnPressed(ActionEvent e) {
        try {
            Table table = new Table();
            table.setName(createTableFrame.getTableName());
            List<Column> columns = createTableFrame.buildColumnList();

            if (columns.isEmpty()) {
                close();
                return;
            }

            for (Column column : columns) {
                table.addColumn(column);
            }

            dbModel.createTable(table);

            List<Column> primaryKey = columns
                    .stream()
                    .filter(Column::isPrimaryKey)
                    .collect(Collectors.toList());

            if (!primaryKey.isEmpty()) {
                dbModel.createPrimaryKey(table, primaryKey);
            }


            List<Column> unique = columns
                    .stream()
                    .filter(Column::isUnique)
                    .collect(Collectors.toList());

            if (!unique.isEmpty()) {
                dbModel.createUnique(table, unique);
            }

            List<ForeignKeyCreatorElement> foreignKeyCreators = createTableFrame.getForeignKeyCreators();

            if (!foreignKeyCreators.isEmpty()) {
                for (ForeignKeyCreatorElement element : foreignKeyCreators) {
                    dbModel.createForeignKey(
                            table,
                            element.getFirstColumnName(),
                            element.getForeignTable(),
                            element.getForeignColumnName()
                    );
                }
            }

            close();

        } catch (DatabaseOperationException e1) {
            JOptionPane.showMessageDialog(createTableFrame, e1.getMessage());
        }
    }


    public void close() {
        createTableFrame.setVisible(false);
        createTableFrame.dispose();
    }
}
