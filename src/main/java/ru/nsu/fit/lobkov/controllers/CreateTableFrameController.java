package ru.nsu.fit.lobkov.controllers;

import ru.nsu.fit.lobkov.view.CreateTableFrame;

/**
 * Created by Ilya on 04.06.2017.
 */
public class CreateTableFrameController {
    private CreateTableFrame createTableFrame;

    public CreateTableFrameController() {
        createTableFrame = new CreateTableFrame(this);
        createTableFrame.setVisible(true);
    }

    public void addColumn() {
        createTableFrame.addColumnCreatorView();
    }
}
