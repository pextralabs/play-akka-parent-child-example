package controllers;

import actors.Actors;
import actors.ParentActor;
import actors.WsSubscriberActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import play.libs.streams.ActorFlow;
import play.mvc.*;

import static akka.pattern.PatternsCS.ask;

import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private final ActorRef parentActor;
    private final ActorSystem actorSystem;
    private final Materializer mat;

    @Inject
    public HomeController(@Named("parent") ActorRef parentActor, ActorSystem actorSystem, Materializer mat) {
        this.parentActor = parentActor;
        this.actorSystem = actorSystem;
        this.mat = mat;
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

    public WebSocket subscribe(String username) {
        return WebSocket.Text.accept(
                request -> ActorFlow.actorRef(
                            out -> WsSubscriberActor.props(parentActor, out, username),
                            actorSystem,
                            mat)
        );
    }

}
