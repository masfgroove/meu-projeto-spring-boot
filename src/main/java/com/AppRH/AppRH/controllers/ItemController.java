package com.AppRH.AppRH.controllers;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AppRH.AppRH.models.Item;
import com.AppRH.AppRH.repository.FileItemRepository;
import com.AppRH.AppRH.repository.ItemRepository;

@Controller
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FileItemRepository fileItemRepository;

    @GetMapping("/cadastrarItem")
    public String formItem(Model model) {
        model.addAttribute("item", new Item());
        return "item/formulario"; // Retorna o nome do arquivo HTML do formulário
    }

    @PostMapping("/cadastrarItem")
    public String cadastrarItem(@Valid Item item, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/cadastrarItem";
        }

        try {
            fileItemRepository.save(item); // Salva no arquivo JSON
            attributes.addFlashAttribute("mensagem", "Item cadastrado com sucesso!");
        } catch (IOException e) {
            attributes.addFlashAttribute("mensagem", "Erro ao salvar o item!");
        }

        return "redirect:/itens"; // Redireciona para a lista de itens após o cadastro
    }

    @GetMapping("/editarItem/{id}")
    public String editarItem(@PathVariable("id") Long id, Model model) throws IllegalArgumentException, IOException {
        Item item = fileItemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Item inválido"));
        model.addAttribute("item", item);
        return "item/formulario"; // Retorna o formulário de edição
    }

    @PostMapping("/atualizarItem")
    public String atualizarItem(@Valid Item item, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/editarItem/" + item.getId();
        }

        try {
            fileItemRepository.save(item); // Atualiza no arquivo JSON
            attributes.addFlashAttribute("mensagem", "Item atualizado com sucesso!");
        } catch (IOException e) {
            attributes.addFlashAttribute("mensagem", "Erro ao atualizar o item!");
        }

        return "redirect:/itens"; // Redireciona para a lista de itens após a atualização
    }

    @GetMapping("/deletarItem/{id}")
    public String deletarItem(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            fileItemRepository.deleteById(id); // Deleta do arquivo JSON
            attributes.addFlashAttribute("mensagem", "Item deletado com sucesso!");
        } catch (IOException e) {
            attributes.addFlashAttribute("mensagem", "Erro ao deletar o item!");
        }

        return "redirect:/itens"; // Redireciona para a lista de itens após a exclusão
    }

    @GetMapping("/itens")
    public String listarItens(Model model) {
        try {
            model.addAttribute("itens", fileItemRepository.findAll()); // Lista itens do arquivo JSON
        } catch (IOException e) {
            model.addAttribute("mensagem", "Erro ao carregar itens!");
        }
        return "item/tabela"; // Nome da view para a tabela de itens
    }

    @GetMapping("/")
    public String home() {
        return "index"; // Nome do arquivo HTML da página home
    }

    @GetMapping("/sobre")
    public String sobre() {
        return "item/sobre"; // Nome do arquivo HTML da página sobre
    }
}
