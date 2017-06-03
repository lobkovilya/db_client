package ru.nsu.fit.lobkov.models;

import pro.batalin.ddl4j.DatabaseOperationException;
import pro.batalin.ddl4j.model.Schema;
import pro.batalin.ddl4j.model.Table;
import pro.batalin.ddl4j.platforms.Platform;
import pro.batalin.ddl4j.platforms.PlatformFactory;
import pro.batalin.ddl4j.platforms.PlatformFactoryException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ilya on 22.05.2017.
 */
public class DatabaseModel {
    private Platform platform;
    private Schema currentScheme;

    public DatabaseModel() throws PlatformFactoryException, SQLException {
        PlatformFactory platformFactory = new PlatformFactory();

        String connectionString = "jdbc:oracle:thin:@localhost:1521:XE";
        String username = "db_client";
        String password = "111111";

        Locale.setDefault(Locale.ENGLISH);
        Connection connection = DriverManager.getConnection(connectionString, username, password);
        platform = platformFactory.create("oracle", connection);
    }

    public List<Schema> getSchemasList() {
        try {
            return platform.loadSchemas();
        } catch (DatabaseOperationException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Schema getCurrentScheme() {
        return currentScheme;
    }

    public void setCurrentScheme(Schema currentScheme) {
        this.currentScheme = currentScheme;
    }

    public List<String> getTableList() throws DatabaseOperationException {
        return platform.loadTables(currentScheme.getName());
    }
}
