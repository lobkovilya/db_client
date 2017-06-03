package ru.nsu.fit.lobkov.controllers;

import pro.batalin.ddl4j.model.Schema;
import ru.nsu.fit.lobkov.models.DatabaseModel;
import ru.nsu.fit.lobkov.view.MainFrame;

import java.awt.event.ActionEvent;

/**
 * Created by Ilya on 22.05.2017.
 */
public class MainFrameController {
    private MainFrame mainFrame;
    private DatabaseModel dbModel;

    public MainFrameController(DatabaseModel dbModel) {
        this.dbModel = dbModel;
        mainFrame = new MainFrame(this, dbModel);
    }

    public void updateTableList() {
        System.out.println("UPDATE");
    }

    public void onSchemaChanged(Schema schema) {
        System.out.println(schema.getName());
    }
}