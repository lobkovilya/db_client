package ru.nsu.fit.lobkov.view;

import pro.batalin.ddl4j.model.Column;
import pro.batalin.ddl4j.model.alters.constraint.AddConstraintForeignKeyAlter;
import pro.batalin.ddl4j.model.constraints.ForeignKey;
import ru.nsu.fit.lobkov.controllers.CreateTableFrameController;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ilya on 04.06.2017.
 */
public class CreateTableFrame extends JFrame {
    private CreateTableFrameController controller;

    private JPanel mainPanel;
    private JButton addButton;
    private JButton removeButton;
    private JScrollPane columnScrollPane;
    private JPanel columnPanel;
    private JTextField tableNameField;
    private JButton CANCELButton;
    private JButton OKButton;
    private JTabbedPane tabbedPane1;
    private JPanel foreignKeysPanel;

    private List<ColumnCreatorElement> columnCreators = new LinkedList<>();
    private List<ForeignKeyCreatorElement> foreignKeyCreators = new LinkedList<>();

    public CreateTableFrame(CreateTableFrameController controller) {
        this.controller = controller;
        setContentPane(mainPanel);
        setSize(650, 500);

        addButton.addActionListener(e -> controller.addColumn());
        removeButton.addActionListener(e -> {
            if (getActiveTab() == 0) {
                deleteColumnCreators();
            } else {
                deleteForeignKeyCreators();
            }
        });
        OKButton.addActionListener(controller::okBtnPressed);
        CANCELButton.addActionListener(e -> controller.close());
    }

    public void deleteColumnCreators() {
        Iterator<ColumnCreatorElement> iter = columnCreators.iterator();
        while (iter.hasNext()) {
            ColumnCreatorElement current = iter.next();
            if (current.isSelected()) {
                columnPanel.remove(current);
                columnPanel.updateUI();
                iter.remove();
            }
        }
    }


    public void addColumnCreatorView() {
        ColumnCreatorElement element = new ColumnCreatorElement();
        element.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                element.getMinimumSize().height
        ));
        columnPanel.add(element);
        columnCreators.add(element);
        columnPanel.updateUI();

    }

    public void deleteForeignKeyCreators() {
        Iterator<ForeignKeyCreatorElement> iter = foreignKeyCreators.iterator();
        while (iter.hasNext()) {
            ForeignKeyCreatorElement current = iter.next();
            if (current.isSelected()) {
                foreignKeysPanel.remove(current);
                foreignKeysPanel.updateUI();
                iter.remove();
            }
        }
    }

    public void addForeignKeyCreatorView() {
        ForeignKeyCreatorElement element = new ForeignKeyCreatorElement();
        element.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                element.getMinimumSize().height
        ));
        foreignKeysPanel.add(element);
        foreignKeyCreators.add(element);
        foreignKeysPanel.updateUI();
    }

    private void createUIComponents() {
        columnPanel = new JPanel();
        columnPanel.setLayout(new BoxLayout(columnPanel, BoxLayout.Y_AXIS));

        foreignKeysPanel = new JPanel();
        foreignKeysPanel.setLayout(new BoxLayout(foreignKeysPanel, BoxLayout.Y_AXIS));
    }

    public List<Column> buildColumnList() {
        return columnCreators.stream()
                    .map(ColumnCreatorElement::buildColumn)
                    .collect(Collectors.toList());
    }

    public List<ForeignKeyCreatorElement> getForeignKeyCreators() {
        return foreignKeyCreators;
    }

    public String getTableName() {
        return tableNameField.getText();
    }

    public int getActiveTab() {
        return tabbedPane1.getSelectedIndex();
    }


}
