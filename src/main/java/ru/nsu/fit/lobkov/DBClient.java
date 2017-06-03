package ru.nsu.fit.lobkov;

import ru.nsu.fit.lobkov.controllers.MainFrameController;
import ru.nsu.fit.lobkov.models.DatabaseModel;
import ru.nsu.fit.lobkov.view.MainFrame;

import javax.swing.*;
import java.util.Locale;

/**
 * Created by Ilya on 21.05.2017.
 */
public class DBClient {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            DatabaseModel dbModel = new DatabaseModel();
            MainFrameController controller = new MainFrameController(dbModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
