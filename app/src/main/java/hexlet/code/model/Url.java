package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter

public class Url {
    private long id;

    private String name;

    private Timestamp createdAt;

    private List<UrlCheck> urlChecks;

    private UrlCheck lastCheck;

    public Url(String name, Timestamp createdAt) {

        this.name = name;
        this.createdAt = createdAt;

    }
}


