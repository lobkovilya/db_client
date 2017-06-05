package ru.nsu.fit.lobkov.view;

import pro.batalin.ddl4j.model.Column;
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

    private List<ColumnCreatorElement> columnCreators = new LinkedList<>();

    public CreateTableFrame(CreateTableFrameController controller) {
        this.controller = controller;
        setContentPane(mainPanel);
        setSize(650, 500);

        /*DON'T FORGET TO DELETE IT*/
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addButton.addActionListener(e -> controller.addColumn());
        removeButton.addActionListener(e -> {
            Iterator<ColumnCreatorElement> iter = columnCreators.iterator();
            while (iter.hasNext()) {
                ColumnCreatorElement current = iter.next();
                if (current.isSelected()) {
                    columnPanel.remove(current);
                    columnPanel.updateUI();
                    iter.remove();
                }
            }
        });
        OKButton.addActionListener(controller::okBtnPressed);
    }

    public static void main(String[] args) {
//        CreateTableFrameController controller = new CreateTableFrameController(d);
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

    private void createUIComponents() {
        columnPanel = new JPanel();
        columnPanel.setLayout(new BoxLayout(columnPanel, BoxLayout.Y_AXIS));
    }

    public List<Column> buildColumnList() {
        return columnCreators.stream()
                    .map(ColumnCreatorElement::buildColumn)
                    .collect(Collectors.toList());
    }

    public String getTableName() {
        return tableNameField.getText();
    }


}
