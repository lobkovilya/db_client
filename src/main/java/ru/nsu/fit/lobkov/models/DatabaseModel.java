package ru.nsu.fit.lobkov.models;

import pro.batalin.ddl4j.DatabaseOperationException;
import pro.batalin.ddl4j.model.Column;
import pro.batalin.ddl4j.model.Schema;
import pro.batalin.ddl4j.model.Table;
import pro.batalin.ddl4j.model.alters.constraint.AddConstraintForeignKeyAlter;
import pro.batalin.ddl4j.model.alters.constraint.AddConstraintPrimaryAlter;
import pro.batalin.ddl4j.model.alters.constraint.AddConstraintUniqueAlter;
import pro.batalin.ddl4j.model.constraints.PrimaryKey;
import pro.batalin.ddl4j.platforms.Platform;
import pro.batalin.ddl4j.platforms.PlatformFactory;
import pro.batalin.ddl4j.platforms.PlatformFactoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

//    private long maxAllowedPrimaryKey = 0;

    public void createPrimaryKey(Table table, List<Column> primaryKey) throws DatabaseOperationException {
        String columnNames = String.join("_", primaryKey.stream().map(Column::getName).collect(Collectors.toList()));
        AddConstraintPrimaryAlter primaryAlter = new AddConstraintPrimaryAlter(
                table,
                String.join("_", "pk", table.getName(), columnNames),
                primaryKey
        );


        platform.executeAlter(primaryAlter);
    }

//    private long maxAllowedUnique = 0;

    public void createUnique(Table table, List<Column> uniqueColumns) throws DatabaseOperationException {
        String columnNames = String.join("_", uniqueColumns.stream().map(Column::getName).collect(Collectors.toList()));
        AddConstraintUniqueAlter uniqueAlter = new AddConstraintUniqueAlter(
                table,
                String.join("_", "unq", table.getName(), columnNames),
                uniqueColumns
        );

        platform.executeAlter(uniqueAlter);
    }

    public void createForeignKey(Table firstTable, String firstColumnName, String foreignTableName, String foreignColumnName)
            throws DatabaseOperationException {
        Column firstColumn = new Column();
        firstColumn.setName(firstColumnName);

        Column foreignColumn = new Column();
        foreignColumn.setName(foreignColumnName);

        Table foreignTable = platform.loadTable(currentScheme, foreignTableName);

        AddConstraintForeignKeyAlter foreignKeyAlter = new AddConstraintForeignKeyAlter(
                firstTable,
                firstColumn,
                foreignTable,
                foreignColumn,
                String.join("_",
                        "fk",
                        firstTable.getName(),
                        firstColumnName,
                        foreignTableName,
                        foreignColumnName
                )
        );


        platform.executeAlter(foreignKeyAlter);
    }

    public void executeQuery(String query) throws DatabaseOperationException {
        platform.executeQuery(query);
    }

}
