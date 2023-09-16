package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public final class Url {

    private Long id;

    @ToString.Include
    private String name;

    private Timestamp createdAt;

    public Url(String name) {
        this.name = name;
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
    }

    public Url(String name, Timestamp createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }
}