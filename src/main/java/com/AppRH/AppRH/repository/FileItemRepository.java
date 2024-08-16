package com.AppRH.AppRH.repository;

import com.AppRH.AppRH.models.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FileItemRepository {

    private static final String FILE_PATH = "src/main/resources/itens.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Item> findAll() throws IOException {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            return objectMapper.readValue(file, new TypeReference<List<Item>>() {});
        } else {
            return new ArrayList<>();
        }
    }

    public Optional<Item> findById(Long id) throws IOException {
        List<Item> items = findAll();
        return items.stream().filter(item -> item.getId().equals(id)).findFirst();
    }

    public void save(Item item) throws IOException {
        List<Item> items = findAll();
        if (item.getId() == null) {
            item.setId((long) (items.size() + 1));
        } else {
            items.removeIf(existingItem -> existingItem.getId().equals(item.getId()));
        }
        items.add(item);
        objectMapper.writeValue(new File(FILE_PATH), items);
    }

    public void deleteById(Long id) throws IOException {
        List<Item> items = findAll();
        items.removeIf(item -> item.getId().equals(id));
        objectMapper.writeValue(new File(FILE_PATH), items);
    }
}
