package com.passManager.pass;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
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
        else {
            routingContext.response()
                    .putHeader("content-type", "application/json")
                    .end(storageContainer.get(Integer.parseInt(id)).toString());
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

    public static void deletePassword(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
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
        else {
            idContainer.remove(Integer.parseInt(id));
            storageContainer.remove(Integer.parseInt(id));
            routingContext.response()
                    .putHeader("content-type", "application/json")
                    .end("{\"success\":\"Password deleted\"}");
        }
    }

    public static void updatePassword(RoutingContext RoutingContext) {
        JsonObject pass = RoutingContext.body().asJsonObject();
        System.out.println(pass.encodePrettily());
        int id = pass.getInteger("id");
        if (!idContainer.contains(id)) {
            RoutingContext.response()
                    .setStatusCode(400)
                    .putHeader("content-type", "application/json")
                    .end("{\"error\":\"Invalid id parameter\"}");
        }
        else {
            Password Pass = storageContainer.get(id);
            if(pass.containsKey("email")){
                Pass.setEmail(pass.getString("email"));
            }
            if(pass.containsKey("password")){
                Pass.setPassword(pass.getString("password"));
            }
            if(pass.containsKey("website")){
                Pass.setWebsite(pass.getString("website"));
            }
            if(pass.containsKey("username")){
                Pass.setWebsite(pass.getString("username"));
            }
            storageContainer.put(id, Pass);
            RoutingContext.response()
                    .putHeader("content-type", "application/json")
                    .end("{\"success\":\"Password updated\"}");
        }
    }

    private static int generateRandomId() {
        Random random = new Random();
        return random.nextInt(1000);
    }
}
