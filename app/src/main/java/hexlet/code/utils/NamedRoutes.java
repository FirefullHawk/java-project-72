package hexlet.code.utils;

public class NamedRoutes {

    private static final String ROOT_PATH = "/";

    private static String URL_PATH;
    private static final String URLS_PATH = "/urls";

    private static String URL_CHECK_PATH;

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
        return String.format(URL_PATH = URLS_PATH + "/%s", id);
    }

    public static String urlCheckPath(String id) {
        return String.format(URL_CHECK_PATH = URLS_PATH + "/%s/checks", id);
    }

    public static String urlCheckPath(long id) {
        return urlCheckPath(String.valueOf(id));
    }
}
