package ru.nsu.fit.lobkov.view;

import pro.batalin.ddl4j.model.Schema;
import ru.nsu.fit.lobkov.controllers.MainFrameController;
import ru.nsu.fit.lobkov.models.DatabaseModel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Ilya on 21.05.2017.
 */
public class MainFrame extends BaseFrame {
    private MainFrameController controller;

    private JList tableList;
    private JPanel mainPanel;
    private JComboBox schemaComboBox;

    private DatabaseModel dbModel;

    public MainFrame(MainFrameController controller, DatabaseModel dbModel) throws HeadlessException {
        this.controller = controller;
        this.dbModel = dbModel;
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        createMenuBar();
        setJMenuBar(menuBar);
        setVisible(true);

        schemaComboBox.addActionListener(e -> {
            JComboBox source = (JComboBox)e.getSource();
            controller.onSchemaChanged((Schema)source.getSelectedItem());
        });

    }


    private void createMenuBar() {
        JMenu fileMenu = createMenu("File");
        JMenuItem connectItem = createMenuItem(fileMenu, "Update", controller::updateTableList);
    }

    private void createUIComponents() {
        schemaComboBox = new JComboBox<>(dbModel.getSchemasList().toArray());

        DefaultListModel<String> tableListModel = new DefaultListModel<>();
        tableListModel.addElement("PEOPLE");
        tableListModel.addElement("COMPANY");
        tableListModel.addElement("SALARY");

        tableList = new JList<>(tableListModel);

    }
}
