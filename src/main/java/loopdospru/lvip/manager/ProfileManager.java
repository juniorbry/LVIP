package loopdospru.lvip.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import loopdospru.lvip.corja.profile.Profile;
import loopdospru.lvip.corja.profile.ProfileFactory;
import loopdospru.lvip.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileManager {

    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    // Salvar perfil no banco de dados
    public static void saveProfile(Profile profile) {
        String json = ProfileFactory.toJson(profile);
        String identifier = profile.getIdentifier();
        String sql = "INSERT INTO clients (player_name, json_data) VALUES (?, ?) ON CONFLICT(player_name) DO UPDATE SET json_data = ?";

        try (java.sql.Connection connection = Database.hikari.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, identifier);
            statement.setString(2, json);
            statement.setString(3, json); // Para o caso de atualização
            statement.executeUpdate();
        } catch (SQLException e) {
            // Adicione logs mais informativos
            System.err.println("Erro ao salvar perfil: " + e.getMessage());
        }
    }

    // Carregar perfil do banco de dados
    public static Profile loadProfile(String identifier, boolean isUuid) {
        String sql = "SELECT json_data FROM clients WHERE player_name = ?";
        try (java.sql.Connection connection = Database.hikari.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, identifier);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String json = resultSet.getString("json_data");
                resultSet.close(); // Feche o ResultSet quando terminar
                return ProfileFactory.createProfile(json, isUuid);
            } else {
                // Adicione um log para identificar quando o perfil não é encontrado
                System.out.println("Perfil não encontrado para o identificador: " + identifier);
            }
        } catch (SQLException e) {
            // Adicione logs mais informativos
            System.err.println("Erro ao carregar perfil: " + e.getMessage());
        }
        return null;
    }
}
