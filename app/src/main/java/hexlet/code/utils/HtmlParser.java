package hexlet.code.utils;

import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import kong.unirest.HttpResponse;

public final class HtmlParser {
    private final String urlToParser;

    public HtmlParser(String urlToParser) {
        this.urlToParser = urlToParser;
    }

    private Document parsedHtmlBody() {
        final int connectionTime = 8000;
        final int socketTime = 8000;

        HttpResponse<String> parsedHtml = Unirest.get(this.urlToParser)
                .connectTimeout(connectionTime)
                .socketTimeout(socketTime)
                .asString();
        return Jsoup.parse(parsedHtml.getBody(), this.urlToParser);
    }

    public Integer getCode() {
        return Unirest.get(this.urlToParser).asString().getStatus();
    }

    public String getTitle() {
        try {
            return parsedHtmlBody().head().selectFirst("title").text();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String getH1() {
        try {
            return parsedHtmlBody().selectFirst("h1").text();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String getDescription() {
        try {
            return parsedHtmlBody().selectFirst("meta[name=description]").attr("content");
        } catch (NullPointerException e) {
            return "";
        }
    }
}
