package travelsync.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Robust RFC 4180-compliant CSV parser that correctly handles:
 * - Quoted fields with embedded commas
 * - Escaped double quotes ("")
 * - Leading/trailing whitespace
 * - Empty fields
 */
public class CSVParser {

    /**
     * Parses a single CSV line into a list of field strings.
     */
    public static List<String> parseLine(String line) {
        List<String> result = new ArrayList<>();
        if (line == null || line.trim().isEmpty()) {
            return result;
        }

        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        int length = line.length();

        for (int i = 0; i < length; i++) {
            char c = line.charAt(i);

            if (c == '\"') {
                if (inQuotes && i + 1 < length && line.charAt(i + 1) == '\"') {
                    // Escaped quote
                    currentField.append('\"');
                    i++; // skip next quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(currentField.toString().trim());
                currentField.setLength(0);
            } else {
                currentField.append(c);
            }
        }
        result.add(currentField.toString().trim());
        return result;
    }
}
