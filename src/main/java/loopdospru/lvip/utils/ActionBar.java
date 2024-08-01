package loopdospru.lvip.utils;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import loopdospru.lvip.LVIP;
import org.bukkit.entity.Player;

public class ActionBar {
    private final Player player;
    private static final ProtocolManager protocolManager = LVIP.protocolManager;

    public ActionBar(Player player) {
        this.player = player;
    }

    public void sendMessage(String message) {
        try {
            PacketContainer packet = protocolManager.createPacket(com.comphenix.protocol.PacketType.Play.Server.CHAT);
            packet.getChatComponents().write(0, WrappedChatComponent.fromText(message));
            packet.getBytes().write(0, (byte) 2); // 2 is the chat position for ActionBar
            protocolManager.sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}