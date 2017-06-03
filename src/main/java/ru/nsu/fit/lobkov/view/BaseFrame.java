package ru.nsu.fit.lobkov.view;

import javax.swing.*;

/**
 * Created by Ilya on 22.05.2017.
 */
abstract public class BaseFrame extends JFrame {
    protected JMenuBar menuBar = new JMenuBar();

    protected JMenu createMenu(String name) {
        JMenu menu = new JMenu(name);
        menuBar.add(menu);
        return menu;
    }

    protected JMenuItem createMenuItem(JMenu menu, String name, Runnable action) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(e -> action.run());
        menu.add(item);
        return item;
    }
}
