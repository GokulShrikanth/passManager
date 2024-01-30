package com.passManager.pass;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class Storage {
    static HashMap<Integer, Password> storageContainer = new HashMap<>();
    static HashSet<Integer> idContainer = new HashSet<>();

    public static void findPassword(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        System.out.println(id);
        if (id == null || id.isEmpty()) {
            routingContext.response()
                    .setStatusCode(400)
                    .putHeader("content-type", "application/json")
                    .end("{\"error\":\"Missing id parameter\"}");
        } else if (!idContainer.contains(Integer.parseInt(id))) {
            routingContext.response()
                    .setStatusCode(400)
                    .putHeader("content-type", "application/json")
                    .end("{\"error\":\"Invalid id parameter\"}");
        }
    }

    public static void addPassword(RoutingContext routingContext) {
        JsonObject pass = routingContext.body().asJsonObject();
        System.out.println(pass.encodePrettily());
        int id = generateRandomId();
        idContainer.add(id);
        pass.put("id", id);
        storageContainer.put(id, new Password(pass));

        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(storageContainer.get(id).toString());
    }

    public static void listIDs(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(idContainer.toString());
    }

    public static void listPasswords(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(storageContainer.toString());
    }

    private static int generateRandomId() {
        Random random = new Random();
        return random.nextInt(1000);
    }
}
