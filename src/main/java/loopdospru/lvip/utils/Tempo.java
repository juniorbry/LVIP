package loopdospru.lvip.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tempo {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy#HH:mm");

    public static String restam(String data, String tempo, String timezone) {
        ZonedDateTime expirationDate = getData(data, timezone);
        Duration duration = parseTempo(tempo);

        // Calcula a data e hora de expiração
        ZonedDateTime expirationDateTime = expirationDate.plus(duration);

        // Obtém a data e hora atual no mesmo fuso horário
        ZonedDateTime now = ZonedDateTime.now(expirationDate.getZone());

        Duration remaining = Duration.between(now, expirationDateTime);
        Duration exceeded = Duration.between(expirationDateTime, now);

        if (remaining.isNegative()) {
            // Tempo ultrapassado
            return formatDuration(exceeded) + " ultrapassado";
        } else {
            // Tempo restante
            return formatDuration(remaining) + " restante";
        }
    }

    public static ZonedDateTime getData(String data, String timezone) {
        String[] dateTime = data.split("#");
        LocalDate date = LocalDate.parse(dateTime[0], DATE_FORMATTER);
        LocalTime time = LocalTime.parse(dateTime[1], TIME_FORMATTER);
        ZoneId zoneId = ZoneId.of(timezone);
        return ZonedDateTime.of(date, time, zoneId);
    }

    private static Duration parseTempo(String tempo) {
        Pattern pattern = Pattern.compile("(\\d+)d\\s*(\\d+)h\\s*(\\d+)m");
        Matcher matcher = pattern.matcher(tempo);

        int days = 0;
        int hours = 0;
        int minutes = 0;

        if (matcher.find()) {
            days = Integer.parseInt(matcher.group(1));
            hours = Integer.parseInt(matcher.group(2));
            minutes = Integer.parseInt(matcher.group(3));
        }

        return Duration.ofDays(days).plusHours(hours).plusMinutes(minutes);
    }

    private static String formatDuration(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        return String.format("%d dia(s), %d hora(s), %d minuto(s), %d segundo(s)",
                days, hours, minutes, seconds);
    }
}
