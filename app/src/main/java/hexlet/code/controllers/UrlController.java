package hexlet.code.controllers;

import java.sql.SQLException;
import java.sql.Timestamp;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;

import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public  class UrlController {
    public static void addUrl(Context ctx) throws SQLException {

        try {
            var inputUrl = ctx.formParamAsClass("url", String.class)
                   .check(value -> !value.isEmpty(), "Заполните это поле")
                   .get();

            URL parsedUrl = new URL(inputUrl);

            var port = "";
            var portNumber = parsedUrl.getPort();
            if (portNumber != -1) {
                port = ":" + portNumber;
            }

            var normalizedUrl = parsedUrl.getProtocol() + "://" + parsedUrl.getHost() + port;

            if (UrlRepository.existsByName(normalizedUrl)) {
                ctx.status(409);
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.sessionAttribute("flash-type", "info");
                ctx.redirect(NamedRoutes.urlsPath());
            }

            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            Url url = new Url(normalizedUrl, createdAt);
            UrlRepository.save(url);

            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flash-type", "success");
            ctx.redirect(NamedRoutes.urlsPath());

        } catch (Exception e) {
            ctx.status(400);
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }

    public static void showUrls(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var pageNumber = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
        var per = 10;
        var firstPost = (pageNumber - 1) * per;

        List<Url> pagedUrls = urls.stream()
                .skip(firstPost)
                .limit(per)
                .collect(Collectors.toList());

        pagedUrls.forEach(url -> {
            try {
                url.setLastCheck(UrlCheckRepository.getLastCheck(url.getId()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        var page = new UrlsPage(pagedUrls, pageNumber);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void showUrl(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));

        var urlChecks = UrlCheckRepository.getEntities(id);
        url.setUrlChecks(urlChecks);

        var page = new UrlPage(url);
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }

    public static void checkUrl(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(urlId)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));

        HttpResponse<String> response = Unirest.get(url.getName()).asString();

        var statusCode = response.getStatus();
        Document doc = Jsoup.parse(response.getBody());

        String title = "";
        if (doc.title() != null) {
            title = doc.title();
        }


        String h1 = "";
        Element h1Element = doc.selectFirst("h1");
        if (h1Element != null) {
            h1 = h1Element.text();
        }

        String description = "";
        Element descElement = doc.selectFirst("meta[name=description]");
        if (descElement != null) {
            description = descElement.attr("content");
        }

        Timestamp createdAt = new Timestamp(System.currentTimeMillis());

        var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
        UrlCheckRepository.save(urlCheck);

        ctx.sessionAttribute("flash", "Страница успешно проверена");
        ctx.sessionAttribute("flash-type", "success");
        ctx.redirect(NamedRoutes.urlPath(urlId));
    }


}
