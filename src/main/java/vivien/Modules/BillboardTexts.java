package vivien.Modules;

public class BillboardTexts {
    private static String[] texts_playing = { "with water", "with magic" };
    private static String[] texts_listening = { "to Sir Lancelot's rambling", "to King Arthur's Request",
            "The Knights of the Round Table" };
    private static String[] texts_watching = { "over the lake ", "over Sir Lancelot", "over Winchester" };
    private static String[] texts_competing = { "in a Joust", "in a magic duel" };

    public static String[] getTextArray(TextType choice) {
        switch (choice) {
            case Playing:
                return texts_playing;
            case Listening:
                return texts_listening;
            case Watching:
                return texts_watching;
            case Competing:
                return texts_competing;
            default:
                return null;
        }
    }
}

enum TextType {
    Playing, Listening, Watching, Competing
}
