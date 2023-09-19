package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CheckRepository extends BaseRepository {
    private static final Integer INDEX_ONE = 1;
    private static final Integer INDEX_TWO = 2;
    private static final Integer INDEX_THREE = 3;
    private static final Integer INDEX_FOUR = 4;
    private static final Integer INDEX_FIVE = 5;
    private static final Integer INDEX_SIX = 6;

    public static void save(UrlCheck url) throws SQLException {
        String sql = "INSERT INTO urlChecks (statusCode, title, h1, description, url_id, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(INDEX_ONE, url.getStatusCode());
            preparedStatement.setString(INDEX_TWO, url.getTitle());
            preparedStatement.setString(INDEX_THREE, url.getH1());
            preparedStatement.setString(INDEX_FOUR, url.getDescription());
            preparedStatement.setLong(INDEX_FIVE, url.getUrlId());
            preparedStatement.setTimestamp(INDEX_SIX, url.getCreatedAt());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static Optional<UrlCheck> byUrlId(Long id) throws SQLException {
        var sql = "SELECT * FROM urlChecks WHERE url_id = ? ORDER BY id DESC";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var statusCode = resultSet.getLong("statusCode");
                var title = resultSet.getString("statusCode");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var urlId = resultSet.getLong("url_id");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
                urlCheck.setId(id);
                return Optional.of(urlCheck);
            }
            return Optional.empty();
        }
    }

    public static List<UrlCheck> getEntities() throws SQLException {
        var sql = "SELECT * FROM urlChecks ORDER BY id DESC";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getLong("statusCode");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var urlId = resultSet.getLong("url_id");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
                urlCheck.setId(id);
                result.add(urlCheck);
            }
            return result;
        }
    }

    public static List<UrlCheck> getEntitiesByUrlId(Long inputId) throws SQLException {
        var sql = "SELECT * FROM urlChecks WHERE url_id = ? ORDER BY id DESC";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, inputId);
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getLong("statusCode");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var urlId = resultSet.getLong("url_id");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
                urlCheck.setId(id);
                result.add(urlCheck);
            }
            return result;
        }
    }
}
