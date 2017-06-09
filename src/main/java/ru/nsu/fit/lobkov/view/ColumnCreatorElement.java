package ru.nsu.fit.lobkov.view;

import pro.batalin.ddl4j.model.Column;
import pro.batalin.ddl4j.model.DBType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Ilya on 04.06.2017.
 */
public class ColumnCreatorElement extends JPanel {
    private JCheckBox isSelectedCheckBox = new JCheckBox();

    private JTextField nameField = new JTextField(15);
    private JTextField typeField = new JTextField(15);
    private JTextField sizeField = new JTextField(15);
    private JTextField defaultField = new JTextField(15);

    private JCheckBox isNotNull = new JCheckBox("Not null");
    private JCheckBox isUnique = new JCheckBox("Unique");
    private JCheckBox isPrimaryKey = new JCheckBox("Primary key");

    public ColumnCreatorElement() {
        JPanel checkPanel = new JPanel();
        checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));
        checkPanel.add(isSelectedCheckBox);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.add(new JLabel("Name:"));
        namePanel.add(nameField);

        JPanel typePanel = new JPanel();
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.Y_AXIS));
        typePanel.add(new JLabel("Type:"));
        typePanel.add(typeField);

        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.Y_AXIS));
        sizePanel.add(new JLabel("Size:"));
        sizePanel.add(sizeField);

        JPanel defaultPanel = new JPanel();
        defaultPanel.setLayout(new BoxLayout(defaultPanel, BoxLayout.Y_AXIS));
        defaultPanel.add(new JLabel("Default:"));
        defaultPanel.add(defaultField);

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        checkBoxPanel.add(isPrimaryKey);
        checkBoxPanel.add(isNotNull);
        checkBoxPanel.add(isUnique);

        JPanel vPanel = new JPanel();
        vPanel.setLayout(new BoxLayout(vPanel, BoxLayout.Y_AXIS));

        JPanel hPanel = new JPanel();
        hPanel.setLayout(new BoxLayout(hPanel, BoxLayout.X_AXIS));

        hPanel.add(namePanel);
        hPanel.add(typePanel);
        hPanel.add(sizePanel);
        hPanel.add(defaultPanel);

        vPanel.add(hPanel);
        vPanel.add(checkBoxPanel);

        add(checkPanel);
        add(vPanel);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelected(!isSelected());
            }
        });
    }

    public void setSelected(boolean selected) {
        isSelectedCheckBox.setSelected(selected);
    }

    public boolean isSelected() {
        return isSelectedCheckBox.isSelected();
    }

    public Column buildColumn() {
        Column result = new Column();
        result.setName(getColumnName());
        result.setType(new DBType(getColumnType()));
        result.setSize(getColumnSize());
        result.setDefaultValue(getColumnDefaultValue());
        result.setRequired(isNotNull());
        result.setPrimaryKey(isPrimaryKey());
        result.setUnique(isUnique());

        return result;
    }

    public boolean isNotNull() {
        return isNotNull.isSelected();
    }

    public boolean isUnique() {
        return isUnique.isSelected();
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey.isSelected();
    }

    public String getColumnName() {
        return nameField.getText();
    }

    public String getColumnType() {
        return typeField.getText();
    }

    public Integer getColumnSize() {
        String text = sizeField.getText();
        return text.isEmpty() ? null : Integer.parseInt(text);
    }

    public String getColumnDefaultValue() {
        return defaultField.getText();
    }
}
