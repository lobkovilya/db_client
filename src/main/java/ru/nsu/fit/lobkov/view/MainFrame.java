package ru.nsu.fit.lobkov.view;

import pro.batalin.ddl4j.DatabaseOperationException;
import pro.batalin.ddl4j.model.Column;
import pro.batalin.ddl4j.model.Schema;
import pro.batalin.ddl4j.model.Table;
import ru.nsu.fit.lobkov.controllers.MainFrameController;
import ru.nsu.fit.lobkov.models.CustomTableModel;
import ru.nsu.fit.lobkov.models.DatabaseModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ilya on 21.05.2017.
 */
public class MainFrame extends BaseFrame {
    private MainFrameController controller;

    private JList tableList;
    private JPanel mainPanel;
    private JComboBox schemaComboBox;
    private JTable tableView;
    private JScrollPane listScrollPanel;
    private JScrollPane tableScrollPanel;
    private JButton addButton;
    private JButton removeButton;
    private JButton updateButton;

    private DatabaseModel dbModel;
    private DefaultListModel<Table> tableListModel;

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
        tableList.addListSelectionListener(e -> {
            JList source = (JList)e.getSource();
            controller.onTableSelectionChanged((Table)source.getSelectedValue());
        });

        addButton.addActionListener(controller::onAddBtnPressed);
        updateButton.addActionListener(e -> {
            CustomTableModel ctm = (CustomTableModel)tableView.getModel();
            try {
                ctm.commitChanges();
            } catch (DatabaseOperationException e1) {
                e1.printStackTrace();
            }
        });
        removeButton.addActionListener(e -> {
            int[] selectedRows = tableView.getSelectedRows();
            CustomTableModel ctm = (CustomTableModel)tableView.getModel();
            for (int i : selectedRows) {
                ctm.removeRow(i);
            }
        });
    }

    public void setTableList(List<Table> tableList) {
        tableListModel.clear();
        for (Table t : tableList) {
            tableListModel.addElement(t);
        }
    }

    public void setTableView(Table table) {
        CustomTableModel model = null;
        try {
            model = new CustomTableModel(dbModel, table);
            tableView.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createMenuBar() {
        JMenu databaseMenu = createMenu("Database");
        JMenuItem updateItem = createMenuItem(databaseMenu, "Update", controller::updateTableList);
        JMenuItem createTableItem = createMenuItem(databaseMenu, "Create table", controller::createTable);
        JMenuItem editTalbeItem = createMenuItem(databaseMenu, "Edit table", controller::createTable);
        JMenuItem removeTalbeItem = createMenuItem(databaseMenu, "Remove table", controller::createTable);
    }

    private void createUIComponents() {
        schemaComboBox = new JComboBox<>(dbModel.getSchemasList().toArray());

        tableListModel = new DefaultListModel<>();
        tableList = new JList<>(tableListModel);

        tableView = new JTable();
        tableScrollPanel = new JScrollPane(tableView);
        tableView.setFillsViewportHeight(true);
    }

    public void insertEmptyRowToTable() {
        CustomTableModel customTableModel = (CustomTableModel)tableView.getModel();
        int columnCount = tableView.getColumnCount();
        Object[] data = new Object[columnCount];
        for (int i = 0; i < columnCount; i++) {
            data[i] = "";
        }
        customTableModel.addRow(data);
    }

}
