import java.util.Scanner;
//import ChatterBot.java;

public class Chat {
    public static void main (String[] args) {
        ChatterBot[] bots = new ChatterBot[2];

        String[] RepliesToLegalResponse0 = {"say " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER +
                                                    "? okay: " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER,
                                            ChatterBot.REQUESTED_PHRASE_PLACEHOLDER +
                                                    " is my favourite word, " +
                                                    ChatterBot.REQUESTED_PHRASE_PLACEHOLDER + "!"};
        String[] RepliesToIllegalResponse0 = {"what", "say I should say"};
        bots[0] = new ChatterBot("Botty", RepliesToLegalResponse0, RepliesToIllegalResponse0);

        String[] RepliesToLegalResponse1 = {"You want me to say "+ ChatterBot.REQUESTED_PHRASE_PLACEHOLDER +
                                                "? alright: " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER,
                                            ChatterBot.REQUESTED_PHRASE_PLACEHOLDER + "!"};
        String[] RepliesToIllegalResponse1 = {"say what? what is " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER +
                                                    "?", "say say"};
        bots[1] = new ChatterBot("Botta", RepliesToLegalResponse1, RepliesToIllegalResponse1);

       String statement = "say Java";

       Scanner scanner = new Scanner(System.in);
       for (int i = 0; ; i++) {
           statement = bots[i % bots.length].replyTo(statement);
           System.out.println(bots[i % bots.length].getName() + ": " + statement);
           scanner.nextLine();
       }
    }
}
