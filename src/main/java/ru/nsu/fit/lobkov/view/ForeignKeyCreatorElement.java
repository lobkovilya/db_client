package ru.nsu.fit.lobkov.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Ilya on 05.06.2017.
 */
public class ForeignKeyCreatorElement extends JPanel {
    private JCheckBox isSelectedCheckBox = new JCheckBox();

    private JTextField foreignTableField = new JTextField(12);
    private JTextField foreignColumnField = new JTextField(12);
    private JTextField firstColumnField = new JTextField(12);

    public ForeignKeyCreatorElement() {
        JPanel tablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tablePanel.add(isSelectedCheckBox);
        tablePanel.add(new JLabel("Table name: "));
        tablePanel.add(foreignTableField);
        tablePanel.add(new JLabel("From: "));
        tablePanel.add(firstColumnField);
        tablePanel.add(new JLabel("To: "));
        tablePanel.add(foreignColumnField);

        add(tablePanel);

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

    public String getFirstColumnName() {
        return firstColumnField.getText();
    }

    public String getForeignColumnName() {
        return foreignColumnField.getText();
    }

    public String getForeignTable() {
        return foreignTableField.getText().toUpperCase();
    }
}
