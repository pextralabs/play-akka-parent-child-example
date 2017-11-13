package controllers;

import actors.Actors;
import actors.ParentActor;
import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import play.mvc.*;

import static akka.pattern.PatternsCS.ask;

import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private final ActorRef parentActor;

    @Inject
    public HomeController(@Named("parent") ActorRef parentActor) {
        this.parentActor = parentActor;
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> message(String username) {
        JsonNode dataNode = request().body().asJson();
        return ask(parentActor,
                new Actors.Message(username, dataNode.get("message").asText()),
                100).thenApplyAsync(
                (Object message) -> ok(message.toString())
        );
    }
}
