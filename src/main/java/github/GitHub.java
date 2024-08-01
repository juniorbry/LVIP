package github;

import loopdospru.lvip.config.General;
import loopdospru.lvip.utils.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class GitHub {

    private static final String REPO_OWNER = "juniorbry";
    private static final String REPO_NAME = "LVIP";
    private static final String LATEST_VERSION_URL = "https://raw.githubusercontent.com/" + REPO_OWNER + "/" + REPO_NAME + "/main/latest-version.txt";
    private static final String RELEASE_URL = "https://github.com/" + REPO_OWNER + "/" + REPO_NAME + "/releases/latest/download/lvip.jar";

    public static boolean isTimeToUpdate(JavaPlugin plugin) {
        String currentVersion = plugin.getDescription().getVersion();
        String latestVersion = fetchLatestVersion();

        if (latestVersion == null) {
            plugin.getLogger().warning("Não foi possível verificar a versão mais recente.");
            return false;
        }

        return !currentVersion.equals(latestVersion);
    }

    private static String fetchLatestVersion() {
        try {
            URL url = new URL(LATEST_VERSION_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String latestVersion = in.readLine();
                in.close();
                return latestVersion.trim();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void update(JavaPlugin plugin) {
        if (!isTimeToUpdate(plugin)) {
            if (General.getLanguage().equalsIgnoreCase("pt-br.yml")) {
                plugin.getLogger().info("O plugin se encontra em sua última versão.");
            }else {
                plugin.getLogger().info("The plugin is in the last version.");
            }
            return;
        }

        if (General.getLanguage().equalsIgnoreCase("pt-br.yml")) {
            plugin.getLogger().info("Atualizando o plugin...");
        }else {
            plugin.getLogger().info("Updating the plugin...");
        }

        try {
            URL url = new URL(RELEASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                File tempFile = File.createTempFile("plugin-update", ".jar");
                FileOutputStream outputStream = new FileOutputStream(tempFile);

                long fileSize = connection.getContentLengthLong();
                byte[] buffer = new byte[1024];
                int bytesRead;
                long totalBytesRead = 0;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;

                    // Atualizar a barra de progresso
                    int progress = (int) ((totalBytesRead * 100) / fileSize);
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        if (player.hasPermission("lvip.admin")) {
                            new ActionBar(player).sendMessage(
                                    String.format(ChatColor.translateAlternateColorCodes('&', "&6LVIP &r%s &7%d%%"), getProgressBar(progress), progress)
                            );
                        }
                    });
                }

                inputStream.close();
                outputStream.close();

                // Substituir o arquivo do plugin
                File pluginFile = new File(plugin.getDataFolder().getParentFile(), plugin.getDescription().getName() + ".jar");
                Files.copy(tempFile.toPath(), pluginFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                plugin.getLogger().info("O plugin foi atualizado. Reinicie o servidor para aplicar as alterações.");
            } else {
                plugin.getLogger().warning("Não foi possível baixar a versão mais recente.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getProgressBar(int progress) {
        int length = 20; // Tamanho da barra de progresso
        int filledLength = (int) ((progress / 100.0) * length);
        StringBuilder bar = new StringBuilder();

        for (int i = 0; i < length; i++) {
            if (i < filledLength) {
                bar.append(ChatColor.GREEN + "█"); // Barra preenchida
            } else {
                bar.append(ChatColor.RED + "░"); // Barra não preenchida
            }
        }

        return bar.toString();
    }
}
