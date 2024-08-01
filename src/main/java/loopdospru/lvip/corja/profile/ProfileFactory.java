package loopdospru.lvip.corja.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import loopdospru.lvip.corja.profile.Profile;
import loopdospru.lvip.corja.profile.ProfilePlayer;
import loopdospru.lvip.corja.profile.ProfileUUID;

public class ProfileFactory {

    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static Profile createProfile(String json, boolean isUuid) {
        if (isUuid) {
            return gson.fromJson(json, ProfileUUID.class);
        } else {
            return gson.fromJson(json, ProfilePlayer.class);
        }
    }

    public static String toJson(Profile profile) {
        return gson.toJson(profile);
    }
}
