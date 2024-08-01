package loopdospru.lvip.corja.profile;

import com.google.gson.annotations.Expose;
import loopdospru.lvip.corja.VIP;

import java.util.List;

public class ProfileUUID implements Profile {

    @Expose
    private String playerUUID;

    @Expose
    private boolean anunciar;

    @Expose
    private List<VIP> vips;

    @Expose
    private VIP atual;

    public ProfileUUID(String playerUUID, boolean anunciar, List<VIP> vips, VIP atual) {
        this.playerUUID = playerUUID;
        this.anunciar = anunciar;
        this.vips = vips;
        this.atual = atual;
    }

    @Override
    public String getIdentifier() {
        return playerUUID;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.playerUUID = identifier;
    }

    @Override
    public boolean isAnunciar() {
        return anunciar;
    }

    @Override
    public void setAnunciar(boolean anunciar) {
        this.anunciar = anunciar;
    }

    @Override
    public List<VIP> getVips() {
        return vips;
    }

    @Override
    public VIP getAtual() {
        return atual;
    }
}
