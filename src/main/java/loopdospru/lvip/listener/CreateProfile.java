package loopdospru.lvip.listener;

import loopdospru.lvip.config.General;
import loopdospru.lvip.corja.profile.Profile;
import loopdospru.lvip.corja.profile.ProfilePlayer;
import loopdospru.lvip.corja.profile.ProfileUUID;
import loopdospru.lvip.manager.ProfileManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.ArrayList;

/**
 * Listener to handle player profile creation or loading on login.
 */
public class CreateProfile implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String identifier = getPlayerIdentifier(event);
        Profile profile = ProfileManager.loadProfile(identifier, General.isUuid());

        if (profile == null) {
            profile = createNewProfile(event);
        }

        ProfileManager.saveProfile(profile);
    }

    /**
     * Retrieves the player's unique identifier based on the current configuration.
     *
     * @param event The AsyncPlayerPreLoginEvent containing player information.
     * @return The player's unique identifier as a String.
     */
    private String getPlayerIdentifier(AsyncPlayerPreLoginEvent event) {
        return General.isUuid() ? String.valueOf(event.getUniqueId()) : event.getName();
    }

    /**
     * Creates a new player profile based on the event details and current configuration.
     *
     * @param event The AsyncPlayerPreLoginEvent containing player information.
     * @return The newly created Profile instance.
     */
    private Profile createNewProfile(AsyncPlayerPreLoginEvent event) {
        boolean isUuid = General.isUuid();
        String identifier = getPlayerIdentifier(event);

        if (isUuid) {
            return new ProfileUUID(
                    identifier,
                    true,
                    new ArrayList<>(),
                    null
            );
        } else {
            return new ProfilePlayer(
                    identifier,
                    true,
                    new ArrayList<>(),
                    null
            );
        }
    }
}
