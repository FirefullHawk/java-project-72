package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;

import hexlet.code.controllers.RootController;
import hexlet.code.controllers.UrlController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
public final class      App {

    private static int getPort() {
        String port = System.getenv()
                .getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    private static String getMode() {
        String mode = System.getenv()
                .getOrDefault("APP_ENV", "development");
        return mode;
    }

    private static boolean isProduction() {
        return getMode().equals("production");
    }

    private static String getDatabaseUrl() {
        String dataBase = System.getenv()
                .getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:hexlet_project;DB_CLOSE_DELAY=-1;");
        return dataBase;
    }

    public static void main(String[] args) throws SQLException, IOException {
        Javalin app = getApp();
        app.start(getPort());
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("jte", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }

    public static Javalin getApp() throws IOException, SQLException {

        JavalinJte.init(createTemplateEngine());

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());
        if (isProduction()) {
            String username = System.getenv("JDBC_DATABASE_USERNAME");
            hikariConfig.setUsername(username);
            String password = System.getenv("JDBC_DATABASE_PASSWORD");
            hikariConfig.setPassword(password);
        }
        var dataSource = new HikariDataSource(hikariConfig);

        var inputStream = App.class.getClassLoader().getResourceAsStream("schema.sql");
        var reader = new BufferedReader(new InputStreamReader(inputStream));
        var sql = reader.lines().collect(Collectors.joining("\n"));

        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> {
            if (!isProduction()) {
                config.plugins.enableDevLogging();
            }
        });

        app.before(ctx -> {
            ctx.contentType("text/html; charset=utf-8");
        });

        app.get(NamedRoutes.rootPath(), RootController::index);
        app.get(NamedRoutes.urlsPath(), UrlController::showUrls);
        app.post(NamedRoutes.urlsPath(), UrlController::addUrl);
        app.get(NamedRoutes.urlPath("{id}"), UrlController::showUrl);
        app.post(NamedRoutes.urlCheckPath("{id}"), UrlController::checkUrl);

        return app;
    }

}

