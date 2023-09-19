package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controllers.RootController;
import hexlet.code.controllers.UrlController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.utils.NamedRoutes;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

public final class App {

    private static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project");
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "3000");
        return Integer.parseInt(port);
    }

    private static String getMode() {
        return System.getenv().getOrDefault("APP_ENV", "development");
    }

    private static boolean isProduction() {
        return getMode().equals("production");
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("jte", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    public static Javalin getApp() throws IOException, SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());

        var dataSource = new HikariDataSource(hikariConfig);

        String file = "schema.sql";

        String sql = getContentFromStream(getFileFromResourceAsStream(file));

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        Javalin app = Javalin.create(config -> {
            if (!isProduction()) {
                config.plugins.enableDevLogging();
            }
        });

        app.before(ctx -> ctx.contentType("text/html; charset=utf-8"));

        JavalinJte.init(createTemplateEngine());

        app.get(NamedRoutes.rootPath(), RootController::index);
        app.get(NamedRoutes.urlsPath(), UrlController::index);
        app.post(NamedRoutes.urlsPath(), UrlController::create);
        app.get(NamedRoutes.urlPath("{id}"), UrlController::show);
        app.post(NamedRoutes.checkPath("{id}"), UrlController::check);
        return app;
    }

    public static void main(String[] args) throws SQLException, IOException {
        Javalin app = getApp();
        app.start(getPort());
    }

    private static InputStream getFileFromResourceAsStream(String file) {
        ClassLoader classLoader = App.class.getClassLoader();
        return classLoader.getResourceAsStream(file);
    }

    private static String getContentFromStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
