package hexlet.code.utils;

public class NamedRoutes {
    public static String rootPath() {
        return "/";
    }

    public static String buildPath() {
        return "/";
    }

    public static String urlsPath() {
        return "/urls";
    }

    public static String urlPath(long id) {
        return urlPath(String.valueOf(id));
    }

    public static String urlPath(String id) {
        return "/urls/" + id;
    }

    public static String checkPath(String id) {
        return "/urls/" + id + "/checks";
    }

    public static String checkPath(long id) {
        return checkPath(String.valueOf(id));
    }
}
