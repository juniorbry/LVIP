package loopdospru.lvip.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;

public class Title {
    private final Player player;
    private final String titulo;
    private final String subtitulo;
    private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    public Title(Player player, String titulo, String subtitulo) {
        this.player = player;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
    }

    public void sendTitle(int fadeInTime, int stayTime, int fadeOutTime) {
        sendTimes(fadeInTime, stayTime, fadeOutTime);
        sendTitlePacket(titulo, EnumWrappers.TitleAction.TITLE);
        sendTitlePacket(subtitulo, EnumWrappers.TitleAction.SUBTITLE);
    }

    private void sendTimes(int fadeInTime, int stayTime, int fadeOutTime) {
        PacketContainer timesPacket = protocolManager.createPacket(PacketType.Play.Server.TITLE);
        timesPacket.getTitleActions().write(0, EnumWrappers.TitleAction.TIMES);
        timesPacket.getIntegers()
                .write(0, fadeInTime)
                .write(1, stayTime)
                .write(2, fadeOutTime);

        protocolManager.sendServerPacket(player, timesPacket);
    }

    private void sendTitlePacket(String text, EnumWrappers.TitleAction action) {
        if (text == null || text.isEmpty()) return;

        PacketContainer titlePacket = protocolManager.createPacket(PacketType.Play.Server.TITLE);
        titlePacket.getTitleActions().write(0, action);
        titlePacket.getChatComponents().write(0, WrappedChatComponent.fromText(text));

        protocolManager.sendServerPacket(player, titlePacket);
    }
}
