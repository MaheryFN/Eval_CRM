package site.easy.to.build.crm.entity.CsvFile;

import java.io.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CsvProcessor {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    
    public String parseCSVLine(String line) {
        StringBuilder result = new StringBuilder();
        boolean insideQuotes = false;
        
        for (char c : line.toCharArray()) {
            if (c == '"') {
                insideQuotes = !insideQuotes;
            }
            if (insideQuotes && c == ',') {
                result.append('.');
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    public List<HashMap<String,Object>> extractDataFromCSV(String fileName, String separator, HashMap<String,Class<?>> columnTypeMap) {
        List<HashMap<String,Object>> result = new ArrayList<>();
        try (BufferedReader csvReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 0;
            List<String> headers = new ArrayList<>();
            
            while ((line = csvReader.readLine()) != null) {
                line = parseCSVLine(line);
                String[] values = line.split(separator);
                
                if (lineNumber > 0) {
                    HashMap<String,Object> lineData = new HashMap<>();
                    for (int i = 0; i < values.length; i++) {
                        Class<?> valueType = columnTypeMap.get(headers.get(i));
                        lineData.put(headers.get(i),
                            parseValue(values[i], valueType, lineNumber, headers.get(i))
                        );
                    }
                    result.add(lineData);
                } else {
                    headers.addAll(Arrays.asList(values));
                }
                lineNumber++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    
    public Object parseValue(String valeur, Class<?> typeChamp, int numeroLigne, String nomColonne) throws Exception {
        if (typeChamp == null || valeur == null) {
            return null;
        }
        
        try {
            if (typeChamp.equals(int.class) || typeChamp.equals(Integer.class)) {
                return Integer.parseInt(valeur);
            } else if (typeChamp.equals(double.class) || typeChamp.equals(Double.class)) {
                valeur = valeur.replace(",", ".")
                    .replace("\"", "")
                    .trim();
                return Double.parseDouble(valeur);
            } else if (typeChamp.equals(boolean.class) || typeChamp.equals(Boolean.class)) {
                return Boolean.parseBoolean(valeur);
            } else if (typeChamp.equals(String.class)) {
                return valeur.replace("\"", "").trim();
            } else if (typeChamp == Date.class) {
                return Date.valueOf(LocalDate.parse(valeur, DATE_FORMATTER));
            } else if (typeChamp == Timestamp.class) {
                return Timestamp.valueOf(LocalDateTime.parse(valeur, DATE_TIME_FORMATTER));
            } else if (typeChamp == LocalDate.class) {
                return LocalDate.parse(valeur, DATE_FORMATTER);
            } else if (typeChamp == LocalDateTime.class) {
                return LocalDateTime.parse(valeur, DATE_TIME_FORMATTER);
            } else if (typeChamp == LocalTime.class) {
                return LocalTime.parse(valeur, TIME_FORMATTER);
            } else if (typeChamp.isArray()) {
                return valeur.split(",");
            } else if (List.class.isAssignableFrom(typeChamp)) {
                return Arrays.stream(valeur.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new Exception("Impossible de convertir la valeur : '" + valeur + "' en " +
                typeChamp.getSimpleName() + " à la ligne " + numeroLigne + " (colonne : " + nomColonne + ")", e);
        }
        
        return null;
    }
}
