package loopdospru.lvip.corja.profile;

import com.google.gson.annotations.Expose;
import loopdospru.lvip.corja.VIP;

import java.util.List;

public class ProfilePlayer implements Profile {

    @Expose
    private String playerName;

    @Expose
    private boolean anunciar;

    @Expose
    private List<VIP> vips;

    @Expose
    private VIP atual;

    public ProfilePlayer(String playerName, boolean anunciar, List<VIP> vips, VIP atual) {
        this.playerName = playerName;
        this.anunciar = anunciar;
        this.vips = vips;
        this.atual = atual;
    }

    @Override
    public String getIdentifier() {
        return playerName;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.playerName = identifier;
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
