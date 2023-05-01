import java.util.*;

/**
 * Base file for the ChatterBot exercise.
 * The bot's replyTo method receives a statement.
 * If it starts with the constant REQUEST_PREFIX, the bot returns
 * whatever is after this prefix. Otherwise, it returns one of
 * a few possible replies as supplied to it via its constructor.
 * In this case, it may also include the statement after
 * the selected reply (coin toss).
 *
 * @author Dan Nirel, Tal Sharon
 */
class ChatterBot {
    // Data Members
    static final String REQUEST_PREFIX = "say ";
    static final String REQUESTED_PHRASE_PLACEHOLDER = "<phrase>";
    static final String ILLEGAL_REQUEST_PLACEHOLDER = "<request>";
    Random rand = new Random();
    String name;
    String[] repliesToIllegalRequest;
    String[] repliesToLegalRequest;

    // Methods
    /**
     * Constructor of ChatterBot Class.
     * @param name: String - The name of the bot.
     * @param repliesToLegalRequest: String[] - All possible replies to legal requests.
     * @param repliesToIllegalRequest: String[] - All possible replies to illegal requests.
     */
    ChatterBot(String name, String[] repliesToLegalRequest, String[] repliesToIllegalRequest) {
        this.name = name;
        this.repliesToLegalRequest = new String[repliesToLegalRequest.length];
        System.arraycopy(repliesToLegalRequest, 0, this.repliesToLegalRequest,
                                                0, repliesToLegalRequest.length);
        this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];
        System.arraycopy(repliesToIllegalRequest, 0, this.repliesToIllegalRequest,
                                                  0, repliesToIllegalRequest.length);
    }

    /**
     * Getter: Gets the name of the bot.
     * @return String - The name of the bot.
     */
    String getName() {
        return this.name;
    }

    /**
     * Gets a statement as an argument -> creates and returns a reply accordingly.
     * if request is legal: returns a reply from bot's repliesToLegalRequest array.
     * else -> request is illegal: returns a reply from bot's repliesToIllegalRequest array.
     * In both cases the method replaces existing placeholders.
     * @param statement: String - The last given statement from the Chat.
     * @return String - The bot's reply.
     */
    String replyTo(String statement) {
        if (statement.startsWith(REQUEST_PREFIX)) {
            // legal request - we don’t repeat the request prefix, so delete it from the reply
            return replacePlaceholderInARandomPattern(repliesToLegalRequest, REQUESTED_PHRASE_PLACEHOLDER,
                                                    statement.replaceFirst(REQUEST_PREFIX, ""));
        }

        // illegal request - get a reply by the random pattern
        String reply = replacePlaceholderInARandomPattern(repliesToIllegalRequest,
                                                            ILLEGAL_REQUEST_PLACEHOLDER, statement);
        // conduct a coin toss to determine if we should add the statement at the end of the reply
        return replacePlaceholderInARandomPattern(new String[]{reply,
                String.format("%s %s", reply, statement)}, ILLEGAL_REQUEST_PLACEHOLDER, statement);
    }

    /**
     * Gets a statement, an array of replies and a placeholder
     * Returns a reply to the statement based on random pattern
     * Placeholders in the reply are replaced by the statement itself
     * @param replies: String[] - An array of possible replies to the statement
     * @param placeholder String - A placeholder to be replaced by the statement in the reply
     * @param statement String - A statement to create a reply to
     * @return String - The reply generated randomly by the steps mentioned
     */
    String replacePlaceholderInARandomPattern(String[] replies, String placeholder, String statement) {
        int randomIndex = rand.nextInt(replies.length);
        String reply = replies[randomIndex];
        return reply.replaceAll(placeholder, statement);
    }

    // replaced by function: replacePlaceholderInARandomPattern
    String respondToLegalRequest(String statement) {
        int randomIndex = rand.nextInt(repliesToLegalRequest.length);
        String reply = repliesToLegalRequest[randomIndex];
        return reply.replaceAll(REQUESTED_PHRASE_PLACEHOLDER, statement);
    }

    // replaced by function: replacePlaceholderInARandomPattern
    String respondToIllegalRequest(String statement) {
        int randomIndex = rand.nextInt(repliesToIllegalRequest.length);
        String reply = repliesToIllegalRequest[randomIndex];
        reply = reply.replaceAll(ILLEGAL_REQUEST_PLACEHOLDER, statement);
        if (rand.nextBoolean()) {
            reply = reply + statement;
        }
        return reply;
    }

}
