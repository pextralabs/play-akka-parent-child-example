package actors;

public class Actors {

    public static class Message {
        String recipient;
        String content;
        public Message(String recipient, String message) {
            this.recipient = recipient;
            this.content = message;
        }
    }

}
