package vivien.Modules;

public class ReactionMessage {
    private String id;
    private String channelid;
    private String emoji;
    private String role;

    public ReactionMessage(String id, String channelid, String emoji, String role) {
        this.id = id;
        this.channelid = channelid;
        this.emoji = emoji;
        this.role = role;
    }

    public String getId() {
        return this.id;
    }

    public String getChannelId() {
        return this.channelid;
    }

    public String getEmoji() {
        return this.emoji;
    }

    public String getRole() {
        return this.role;
    }
}
