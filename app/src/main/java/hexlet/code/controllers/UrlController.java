package hexlet.code.controllers;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;

import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import hexlet.code.utils.HtmlParser;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static hexlet.code.utils.UrlBuild.urlBuild;

public  class UrlController {
    public static void addUrl(Context ctx) throws SQLException {
        try {
            var name = ctx.formParamAsClass("url", String.class)
                    .get()
                    .toLowerCase()
                    .trim();

            var urlToValidate = new URI(name);
            String normalizeUrl = urlBuild(urlToValidate.toURL());
            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            var url = new Url(normalizeUrl, createdAt);

            if (!UrlRepository.existsByName(normalizeUrl)) {
                ctx.status(409);
                UrlRepository.save(url);
                ctx.sessionAttribute("flash", "Сайт успешно добавлен");
                ctx.sessionAttribute("flash-type", "success");
                ctx.redirect(NamedRoutes.urlsPath());
            } else {
                ctx.status(400);
                ctx.sessionAttribute("flash", "Сайт уже добавлен");
                ctx.sessionAttribute("flash-type", "info");
                ctx.redirect(NamedRoutes.urlsPath());
            }
        } catch (MalformedURLException | URISyntaxException e) {

            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }

    public static void showUrls(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var pageNumber = ctx.queryParamAsClass("page", long.class).getOrDefault(1L);
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

        String conditionNext = UrlRepository.getEntities().size() > pageNumber * per
                ? "active" : "disabled";
        String conditionBack = pageNumber > 1 ? "active" : "disabled";

        var page = new UrlsPage(pagedUrls, pageNumber, conditionNext, conditionBack);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void showUrl(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var pageNumber = ctx.queryParamAsClass("page", long.class).getOrDefault(1L);
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));

        var urlChecks = UrlCheckRepository.getEntities(id);
        url.setUrlChecks(urlChecks);

        final long urlPerPage = 5;

        String conditionNext = UrlCheckRepository.getEntities(id).size() > pageNumber * urlPerPage
                ? "active" : "disabled";
        String conditionBack = pageNumber > 1 ? "active" : "disabled";

        var page = new UrlPage(url, pageNumber, conditionNext, conditionBack);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }

    public static void checkUrl(Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));

        var parsedHtml = new HtmlParser(url.getName());

        int status = parsedHtml.getCode();
        String h1 = parsedHtml.getH1();
        String description = parsedHtml.getDescription();
        String title = parsedHtml.getTitle();
        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());

        var urlToCheck = new UrlCheck(status, title, h1, description, id, createdAt);
        UrlCheckRepository.save(urlToCheck);

        ctx.sessionAttribute("flash", "Проверка сайта успешно проведена");
        ctx.sessionAttribute("flash-type", "success");
        ctx.redirect(NamedRoutes.urlPath(id));
    }
}
