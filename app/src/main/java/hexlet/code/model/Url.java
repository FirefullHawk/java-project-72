package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString

public class Url {
    private long id;

    @ToString.Include
    private String name;

    private Timestamp createdAt;

    private List<UrlCheck> urlChecks;

    private UrlCheck lastCheck;

    public Url() { }

    public Url(String name, Timestamp createdAt) {

        this.name = name;
        this.createdAt = createdAt;

    }
}


