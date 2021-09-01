package vivien.Modules;

import java.util.Random;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;

public class BillBoard implements Runnable {

    private GatewayDiscordClient client;

    public BillBoard(GatewayDiscordClient client) {
        this.client = client;
    }

    private String[] texts = { "In-Dev", "Made By Vvamp" };

    @Override
    public void run() {
        Random rnd = new Random();
        int choice = rnd.nextInt(texts.length) + 0;
        System.out.println("> Updating Status to " + texts[choice]);
        client.updatePresence(Presence.online(Activity.playing(texts[choice]))).block();

    }

}
