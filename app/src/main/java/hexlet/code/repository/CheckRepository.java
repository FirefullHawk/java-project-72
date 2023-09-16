package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CheckRepository extends BaseRepository {
    public static void save(UrlCheck url) throws SQLException {
        String sql = "INSERT INTO urlChecks (statusCode, title, h1, description, urlId, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, url.getStatusCode());
            preparedStatement.setString(2, url.getTitle());
            preparedStatement.setString(3, url.getH1());
            preparedStatement.setString(4, url.getDescription());
            preparedStatement.setLong(5, url.getUrlId());
            preparedStatement.setTimestamp(6, url.getCreatedAt());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static Optional<UrlCheck> find(Long id) throws SQLException {
        var sql = "SELECT * FROM urlChecks WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var statusCode = resultSet.getLong("statusCode");
                var title = resultSet.getString("statusCode");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var urlId = resultSet.getLong("urlId");
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
                var urlId = resultSet.getLong("urlId");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
                urlCheck.setId(id);
                result.add(urlCheck);
            }
            return result;
        }
    }

    public static List<UrlCheck> getEntitiesById(Long inputId) throws SQLException {
        var sql = "SELECT * FROM urlChecks WHERE urlId = ? ORDER BY id DESC";
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
                var urlId = resultSet.getLong("urlId");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId, createdAt);
                urlCheck.setId(id);
                result.add(urlCheck);
            }
            return result;
        }
    }
}
