package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public final class UrlCheck {

    private long id;

    private Long statusCode;

    private String title;

    private String h1;

    private String description;

    private long urlId;

    private Timestamp createdAt;

    public UrlCheck(long statusCode, String title, String h1, String description, long urlId) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.urlId = urlId;
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
    }

    public UrlCheck(long statusCode, String title, String h1, String description, long urlId, Timestamp createdAt) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.urlId = urlId;
        this.createdAt = createdAt;
    }
}