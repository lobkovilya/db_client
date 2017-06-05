package ru.nsu.fit.lobkov.controllers;

import pro.batalin.ddl4j.DatabaseOperationException;
import pro.batalin.ddl4j.model.Column;
import pro.batalin.ddl4j.model.Table;
import ru.nsu.fit.lobkov.models.DatabaseModel;
import ru.nsu.fit.lobkov.view.CreateTableFrame;

import java.awt.event.ActionEvent;
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
        createTableFrame.addColumnCreatorView();
    }

    public void okBtnPressed(ActionEvent e) {
        try {
            Table table = new Table();
            table.setName(createTableFrame.getTableName());
            List<Column> columns = createTableFrame.buildColumnList();

            for (Column column : columns) {
                table.addColumn(column);
            }

            dbModel.createTable(table);

            List<Column> primaryKey = columns
                    .stream()
                    .filter(Column::isPrimaryKey)
                    .collect(Collectors.toList());
            dbModel.createPrimaryKey(table, primaryKey);

            createTableFrame.setVisible(false);
            createTableFrame.dispose();

        } catch (DatabaseOperationException e1) {
            e1.printStackTrace();
        }
    }
}
