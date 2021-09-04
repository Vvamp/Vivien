package vivien.Modules;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Member;

public class RoleManager {
    private static String[] defaultRoles = { "" };

    public void applyDefaultRoles(Member member) {
        for (String role : defaultRoles) {
            try {
                member.addRole(Snowflake.of(role)).block();
            } catch (Exception e) {
                System.out.println("> Failed to add role: " + e.getMessage());
            }
        }
    }
}
