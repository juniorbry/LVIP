package loopdospru.lvip.corja.profile;

import loopdospru.lvip.corja.VIP;

import java.util.List;

public interface Profile {
    String getIdentifier();
    void setIdentifier(String identifier);
    boolean isAnunciar();
    void setAnunciar(boolean anunciar);
    List<VIP> getVips();
    VIP getAtual();
}
