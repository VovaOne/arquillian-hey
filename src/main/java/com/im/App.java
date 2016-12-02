package com.im;


import liquibase.Liquibase;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

@Startup
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
@TransactionAttribute(value = TransactionAttributeType.NEVER)
public class App {

    @Resource(lookup = "java:jboss/datasources/ExampleDS")
    DataSource dataSource;

    @PostConstruct
    void migrateDB() throws LiquibaseException, SQLException, IOException, URISyntaxException {
        Connection connection = dataSource.getConnection();
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

        DatabaseChangeLog databaseChangeLog = new DatabaseChangeLog();

        Liquibase liq = new Liquibase("db-migration.xml", new FileSystemResourceAccessor(), database);
        liq.update("");

    }
}
