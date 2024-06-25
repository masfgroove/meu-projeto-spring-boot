package com.AppRH.AppRH.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AppRH.AppRH.models.Candidato;
import com.AppRH.AppRH.models.Pedido;
import com.AppRH.AppRH.models.Vaga;
import com.AppRH.AppRH.repository.CandidatoRepository;
import com.AppRH.AppRH.repository.PedidoRepository;
import com.AppRH.AppRH.repository.VagaRepository;

@Controller
public class VagaController {

	@Autowired
	private VagaRepository vr;
	
	@Autowired
	private CandidatoRepository cr;
	
	@Autowired
	private PedidoRepository pd;

	// CADASTRA VAGA
	@RequestMapping(value = "/cadastrarVaga", method = RequestMethod.GET)
	public String form() {
		return "vaga/formVaga";
	}

	@RequestMapping(value = "/cadastrarVaga", method = RequestMethod.POST)
	public String form(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos...");
			return "redirect:/cadastrarVaga";
		}

		vr.save(vaga);
		attributes.addFlashAttribute("mensagem", "Vaga cadastrada com sucesso!");
		return "redirect:/cadastrarVaga";
	}

	
	@RequestMapping("/vagas")
	public ModelAndView listaVagas() {
	    ModelAndView mv = new ModelAndView("vaga/listaVaga");
	    Iterable<Vaga> vagas = vr.findAll();
	    
	    List<List<Candidato>> candidatosPorVaga = new ArrayList<>();
	    for (Vaga vaga : vagas) {
	        List<Candidato> candidatos = new ArrayList<>();
	        for (Candidato candidato : cr.findByVaga(vaga)) {
	            candidatos.add(candidato);
	        }
	        candidatosPorVaga.add(candidatos);
	    }
	    
	    mv.addObject("vagas", vagas);
	    mv.addObject("candidatosPorVaga", candidatosPorVaga);

	    
	    
	    return mv;
	}

	

	@RequestMapping("/pedidos")
	public ModelAndView listaPedidos() {
	    ModelAndView mv = new ModelAndView("vaga/listaPedidos");
	    Iterable<Vaga> vagas = vr.findAll();
	    
	    List<List<Candidato>> candidatosPorVaga = new ArrayList<>();
	    for (Vaga vaga : vagas) {
	        List<Candidato> candidatos = new ArrayList<>();
	        for (Candidato candidato : cr.findByVaga(vaga)) {
	            candidatos.add(candidato);
	        }
	        candidatosPorVaga.add(candidatos);
	    }
	    
	    mv.addObject("vagas", vagas);
	    mv.addObject("candidatosPorVaga", candidatosPorVaga);

   Iterable<Pedido> pedidos = pd.findAll();
	    
	    mv.addObject("pedidos", pedidos);
	     
	    
	    return mv;
	}
	
	
	
	
	//
	@RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
	public ModelAndView detalhesVaga(@PathVariable("codigo") long codigo) {
		Vaga vaga = vr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("vaga/detalhesVaga");
		mv.addObject("vaga", vaga);

		Iterable<Candidato> canditados = cr.findByVaga(vaga);
		mv.addObject("candidatos", canditados);

		return mv;

	}
		
	
	
	// CADASTRAR PEDIDO
	@RequestMapping(value = "/cadastrarPedido", method = RequestMethod.POST)
	public String cadastrarPedido(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {

	    if (result.hasErrors()) {
	        attributes.addFlashAttribute("mensagem", "Verifique os campos...");
	        return "redirect:/cadastrarVaga"; // Redireciona para a página de cadastro de vaga
	    }

	    Pedido pedido = new Pedido();
	    pedido.setNome(vaga.getNome());
	    pedido.setDescricao(vaga.getDescricao());
	    pedido.setData(vaga.getData());
	    pedido.setSalario(vaga.getSalario());

	    // Salvar o pedido no banco de dados
	    pd.save(pedido);

	    attributes.addFlashAttribute("mensagem", "Pedido cadastrado com sucesso!");
	    return "redirect:/cadastrarVaga"; // Redireciona para a página de cadastro de vaga
	}

	
	
	// DELETA VAGA
	@RequestMapping("/deletarVaga")
	public String deletarVaga(long codigo) {
		Vaga vaga = vr.findByCodigo(codigo);
		vr.delete(vaga);
		return "redirect:/vagas";
	}

	// ADICIONAR CANDIDATO
	@RequestMapping(value = "/{codigo}", method = RequestMethod.POST)
	public String detalhesVagaPost(@PathVariable("codigo") long codigo, @Valid Candidato candidato,
			BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			return "redirect:/{codigo}";
		}

		// rg duplicado
		if (cr.findByRg(candidato.getRg()) != null) {
			attributes.addFlashAttribute("mensagem_erro", "RG duplicado");
			return "redirect:/{codigo}";
		}

		Vaga vaga = vr.findByCodigo(codigo);
		candidato.setVaga(vaga);
		cr.save(candidato);
		attributes.addFlashAttribute("mensagem", "Candidato adionado com sucesso!");
		return "redirect:/{codigo}";
	}

	// DELETA CANDIDATO pelo RG
	@RequestMapping("/deletarCandidato")
	public String deletarCandidato(String rg) {
		Candidato candidato = cr.findByRg(rg);
		Vaga vaga = candidato.getVaga();
		String codigo = "" + vaga.getCodigo();

		cr.delete(candidato);

		return "redirect:/" + codigo;

	}

	// Métodos que atualizam vaga
	// formulário edição de vaga
	@RequestMapping(value = "/editar-vaga", method = RequestMethod.GET)
	public ModelAndView editarVaga(long codigo) {
		Vaga vaga = vr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("vaga/update-vaga");
		mv.addObject("vaga", vaga);
		return mv;
	}

	// UPDATE vaga
	@RequestMapping(value = "/editar-vaga", method = RequestMethod.POST)
	public String updateVaga(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {
		vr.save(vaga);
		attributes.addFlashAttribute("success", "Vaga alterada com sucesso!");

		long codigoLong = vaga.getCodigo();
		String codigo = "" + codigoLong;
		return "redirect:/" + codigo;
	}

}
