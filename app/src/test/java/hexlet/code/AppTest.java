package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {


    private static MockWebServer mockWebServer;
    private static String mockUrl;

    Javalin app;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }


    @BeforeAll
    public static void beforeAll() throws IOException, SQLException {
        mockWebServer = new MockWebServer();
        Path path  = Paths.get("src/test/resources/fixtures/test.html").toAbsolutePath().normalize();
        MockResponse mockedResponse = new MockResponse()
                .setBody(Files.readString(path));
        mockWebServer.enqueue(mockedResponse);
        mockWebServer.start();
        mockUrl = mockWebServer.url("/").toString().replaceAll("/$", "");
    }

    @AfterAll
    public static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testMainPage() throws Exception {

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testUrlsPage() throws Exception {

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlPage() throws Exception {
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        var url = new Url(mockUrl, createdAt);
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testCreateUrl() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + mockUrl;
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            var url = UrlRepository.findByName(mockUrl);
            assertThat(url.getName()).isEqualTo(mockUrl);
        });
    }

    @Test
   public void testCheckUrl() throws Exception {

        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + mockUrl;
            client.post("/urls", requestBody);
            var url = UrlRepository.findByName(mockUrl);

            client.post("/urls/" + url.getId() + "/checks");
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);

            var urlChecks = UrlCheckRepository.getEntities(url.getId());
            url.setUrlChecks(urlChecks);
            var urlCheck = UrlCheckRepository.getLastCheck(url.getId());

            String actualTitle = urlCheck.getTitle();
            String actualH1 = urlCheck.getH1();
            String actualDescription = urlCheck.getDescription();

            String expectedTitle  = "Погода в Санкт-Петербурге";
            String expectedH1  = "Погода";
            String expectedDescription  = "Текущая погода и точный прогноз";

            assertThat(actualTitle).isEqualTo(expectedTitle);
            assertThat(actualH1).isEqualTo(expectedH1);
            assertThat(actualDescription).isEqualTo(expectedDescription);

        });

    }

}
