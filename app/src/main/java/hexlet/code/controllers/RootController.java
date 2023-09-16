package hexlet.code.controllers;

import hexlet.code.utils.NamedRoutes;
import io.javalin.http.Context;

public class RootController {
    public static void index(Context ctx) {
        UrlController.build(ctx);
    }
}
