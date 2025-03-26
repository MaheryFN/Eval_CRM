package site.easy.to.build.crm.entity.CsvFile;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CsvFile {
    String name;
    String path;
    HashMap<String,Class<?>> types = new HashMap<>();
    
    public CsvFile() {
    }
    
    public CsvFile(String name, String path, HashMap<String, Class<?>> types) {
        this.name = name;
        this.path = path;
        this.types = types;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public HashMap<String, Class<?>> getTypes() {
        return types;
    }
    
    public void setTypes(HashMap<String, Class<?>> types) {
        this.types = types;
    }
    
}
