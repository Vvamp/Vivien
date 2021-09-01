package vivien.Modules;

import java.util.Random;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.discordjson.json.ActivityUpdateRequest;

public class BillBoard implements Runnable {

    private GatewayDiscordClient client;

    public BillBoard(GatewayDiscordClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        Random rnd = new Random();

        // Choose which type of text to show(watching/playing/listening/competing)
        int text_type = rnd.nextInt(4);
        TextType chosenType = TextType.values()[text_type];

        String[] texts = BillboardTexts.getTextArray(chosenType);

        // Choose which specific text we'll show
        int choice = rnd.nextInt(texts.length);

        // Log it
        System.out.println("> Updating Status to " + texts[choice] + "(" + chosenType.toString() + ")");

        ActivityUpdateRequest aur;
        // Figure out which activity we should be choosing
        switch (chosenType) {
            case Playing:
                aur = Activity.playing(texts[choice]);
                break;
            case Watching:
                aur = Activity.watching(texts[choice]);
                break;
            case Listening:
                aur = Activity.listening(texts[choice]);
                break;
            case Competing:
                aur = Activity.competing(texts[choice]);
                break;
            default:
                aur = Activity.playing(texts[choice]);
        }
        client.updatePresence(Presence.online(aur)).block();

    }

}
