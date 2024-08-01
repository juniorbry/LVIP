package github;

import loopdospru.lvip.config.General;
import loopdospru.lvip.utils.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class GitHub {

    private static final String REPO_OWNER = "juniorbry";
    private static final String REPO_NAME = "LVIP";
    private static final String LATEST_VERSION_URL = "https://raw.githubusercontent.com/juniorbry/LVIP/main/latest-version.txt"; // Verifique o caminho real
    private static final String ZIP_URL = "https://github.com/" + REPO_OWNER + "/" + REPO_NAME + "/archive/refs/tags/vBuild1.0.0.zip";
    private static final String JAR_NAME = "lvip.jar"; // Nome do JAR que você espera no ZIP

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
        HttpURLConnection connection = null;
        BufferedReader in = null;
        try {
            URL url = new URL(LATEST_VERSION_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String latestVersion = in.readLine();
                return latestVersion != null ? latestVersion.trim() : null;
            } else {
                System.err.println("Erro ao conectar: Código de resposta " + connection.getResponseCode());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (in != null) in.close();
                if (connection != null) connection.disconnect();
            } catch (IOException ignored) {}
        }
    }

    public static void update(JavaPlugin plugin) {
        if (!isTimeToUpdate(plugin)) {
            if (General.getLanguage().equalsIgnoreCase("pt-br.yml")) {
                plugin.getLogger().info("O plugin se encontra em sua última versão.");
            } else {
                plugin.getLogger().info("The plugin is in the last version.");
            }
            return;
        }

        if (General.getLanguage().equalsIgnoreCase("pt-br.yml")) {
            plugin.getLogger().info("Atualizando o plugin...");
        } else {
            plugin.getLogger().info("Updating the plugin...");
        }

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            URL url = new URL(ZIP_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                File tempZipFile = File.createTempFile("plugin-update", ".zip");
                try (FileOutputStream zipOutputStream = new FileOutputStream(tempZipFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    long totalBytesRead = 0;
                    long fileSize = connection.getContentLengthLong();

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        zipOutputStream.write(buffer, 0, bytesRead);
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
                }

                extractAndReplacePlugin(tempZipFile, plugin);

                plugin.getLogger().info("O plugin foi atualizado. Reinicie o servidor para aplicar as alterações.");
            } else {
                plugin.getLogger().warning("Não foi possível baixar a versão mais recente. Código de resposta: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (connection != null) connection.disconnect();
            } catch (IOException ignored) {}
        }
    }

    private static void extractAndReplacePlugin(File zipFile, JavaPlugin plugin) {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.getName().endsWith(JAR_NAME)) {
                    File pluginFile = new File(plugin.getDataFolder().getParentFile(), JAR_NAME);
                    try (FileOutputStream fos = new FileOutputStream(pluginFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                    break;
                }
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
