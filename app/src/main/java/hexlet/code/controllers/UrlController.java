package hexlet.code.controllers;

import hexlet.code.dto.urls.UrlBuildPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.model.Url;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.utils.HtmlParser;
import hexlet.code.utils.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.sql.SQLException;
import java.util.Collections;
import java.util.stream.Collectors;

import static hexlet.code.utils.UrlBuild.urlBuild;

public class UrlController {

    public static void build(Context ctx) {
        var page = new UrlBuildPage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        try {
            var name = ctx.formParamAsClass("url", String.class)
                    .get()
                    .toLowerCase()
                    .trim();

            var urlToValidate = new URI(name);
            String normalizeUrl = urlBuild(urlToValidate.toURL());
            var url = new Url(normalizeUrl);

            if (UrlsRepository.findByName(url.getName()).isEmpty()) {
                UrlsRepository.save(url);
                ctx.sessionAttribute("flash", "Сайт успешно добавлен");
                ctx.sessionAttribute("flash-type", "success");
                ctx.redirect(NamedRoutes.urlsPath());
            } else {
                ctx.sessionAttribute("flash", "Сайт уже добавлен");
                ctx.sessionAttribute("flash-type", "info");
                ctx.redirect(NamedRoutes.urlsPath());
            }
        } catch (MalformedURLException | URISyntaxException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect(NamedRoutes.buildPath());
        }
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var pageNumber = ctx.queryParamAsClass("page", long.class).getOrDefault(1L);

        var url = UrlsRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));

        final long urlPerPage = 5;

        var urlsCheck = UrlCheckRepository.getEntitiesByUrlId(id)
                .stream()
                .skip((pageNumber - 1) * urlPerPage)
                .limit(urlPerPage)
                .collect(Collectors.toList());

        String conditionNext = UrlCheckRepository.getEntities().size() > pageNumber * urlPerPage
                ? "active" : "disabled";
        String conditionBack = pageNumber > 1 ? "active" : "disabled";

        var page = new UrlPage(url, urlsCheck, pageNumber, conditionNext, conditionBack);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }

    public static void check(Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlsRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));

        try {
            var parsedHtml = new HtmlParser(url.getName());

            long status = parsedHtml.getCode();
            String h1 = parsedHtml.getH1();
            String description = parsedHtml.getDescription();
            String title = parsedHtml.getTitle();

            var urlToCheck = new UrlCheck(status, title, h1, description, id);
            UrlCheckRepository.save(urlToCheck);

            ctx.sessionAttribute("flash", "Проверка сайта успешно проведена");
            ctx.sessionAttribute("flash-type", "success");

            ctx.redirect(NamedRoutes.urlPath(id));
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect(NamedRoutes.urlPath(id));
        }
    }

    public static void index(Context ctx) throws SQLException {
        var pageNumber = ctx.queryParamAsClass("page", long.class).getOrDefault(1L);
        final long urlPerPage = 10;
        var urls = UrlsRepository.getEntities()
                .stream()
                .skip((pageNumber - 1) * urlPerPage)
                .limit(urlPerPage)
                .collect(Collectors.toList());

        var urlChecks = UrlCheckRepository.getEntities();

        String conditionNext = UrlsRepository.getEntities().size() > urlPerPage * pageNumber ? "active" : "disabled";
        String conditionBack = pageNumber > 1 ? "active" : "disabled";

        var page = new UrlsPage(urls, urlChecks, pageNumber, conditionNext, conditionBack);

        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));

        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }
}
