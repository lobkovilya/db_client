package ru.nsu.fit.lobkov.models;

import pro.batalin.ddl4j.DatabaseOperationException;
import pro.batalin.ddl4j.model.Column;
import pro.batalin.ddl4j.model.Schema;
import pro.batalin.ddl4j.model.Table;
import pro.batalin.ddl4j.model.alters.constraint.AddConstraintPrimaryAlter;
import pro.batalin.ddl4j.model.constraints.PrimaryKey;
import pro.batalin.ddl4j.platforms.Platform;
import pro.batalin.ddl4j.platforms.PlatformFactory;
import pro.batalin.ddl4j.platforms.PlatformFactoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ilya on 22.05.2017.
 */
public class DatabaseModel {
    private Platform platform;
    private Schema currentScheme;
    private Connection connection;

    public DatabaseModel() throws PlatformFactoryException, SQLException {
        PlatformFactory platformFactory = new PlatformFactory();

        String connectionString = "jdbc:oracle:thin:@localhost:1521:XE";
        String username = "db_client";
        String password = "111111";

        Locale.setDefault(Locale.ENGLISH);
        connection = DriverManager.getConnection(connectionString, username, password);
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

    public List<Table> getTableList() throws DatabaseOperationException {
        List<String> tableNames = platform.loadTables(currentScheme.getName());
        List<Table> tables = new ArrayList<>();

        for (String name : tableNames) {
            tables.add(platform.loadTable(currentScheme, name));
        }

        return tables;
    }

    public ResultSet loadRows(Table table) throws SQLException {
        String selectQuery = "SELECT * FROM " + table.getFullName();
        Statement statement = connection.createStatement();
        return statement.executeQuery(selectQuery);
    }

    public void createTable(Table table) throws DatabaseOperationException {
        table.setSchema(currentScheme);
        platform.createTable(table);
    }

    private long maxAllowedPrimaryKey = 0;
    public void createPrimaryKey(Table table, List<Column> primaryKey) throws DatabaseOperationException {
        AddConstraintPrimaryAlter primaryAlter = new AddConstraintPrimaryAlter(
                table,
                "pk_" + maxAllowedPrimaryKey++,
                primaryKey);


        platform.executeAlter(primaryAlter);
    }
}
