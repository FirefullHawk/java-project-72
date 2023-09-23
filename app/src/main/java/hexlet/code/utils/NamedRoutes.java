package hexlet.code.utils;

public class NamedRoutes {

    private static final String ROOT_PATH = "/";
    private static final String URLS_PATH = "/urls";

    public static String rootPath() {
        return ROOT_PATH;
    }

    public static String urlsPath() {
        return URLS_PATH;
    }

    public static String urlPath(long id) {
        return urlPath(String.valueOf(id));
    }

    public static String urlPath(String id) {
        return String.format(URLS_PATH + "/%s", id);
    }

    public static String urlCheckPath(String id) {
        return String.format(URLS_PATH + "/%s/checks", id);
    }

    public static String urlCheckPath(long id) {
        return urlCheckPath(String.valueOf(id));
    }
}
