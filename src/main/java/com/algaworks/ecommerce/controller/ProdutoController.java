package com.algaworks.ecommerce.controller;

import com.algaworks.ecommerce.model.Produto;
import com.algaworks.ecommerce.repository.Produtos;
import com.algaworks.ecommerce.service.ProdutoService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@AllArgsConstructor
@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private Produtos produtos;
    private ProdutoService service;

    @PostMapping("/{id}/editar")
    public ModelAndView atualizar(@PathVariable Integer id,
                                  @RequestParam Map<String, Object> produto,
                                  RedirectAttributes redirectAttributes) {
        service.atualizar(id, produto);

        redirectAttributes.addFlashAttribute("mensagem", "Atualização feita com sucesso!");

        return new ModelAndView("redirect:/produtos/{id}/editar");
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable Integer id, ServletRequest servletRequest) {
        return novo(produtos.buscar(id), servletRequest);
    }

    @PostMapping("/novo")
    public ModelAndView criar(Produto produto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        Produto atualizado = service.criar(produto);

        redirectAttributes.addFlashAttribute("mensagem", "Registro criado com sucesso!");

        return new ModelAndView(
                "redirect:/produtos/{id}/editar", "id", atualizado.getId());
    }

    @GetMapping("/novo")
    public ModelAndView novo(Produto produto, ServletRequest servletRequest) {
        var httpServletRequest = (HttpServletRequest) servletRequest;

        ModelAndView mv = new ModelAndView("produtos/produtos-formulario");
        mv.addObject("produto", produto);
        mv.addObject("httpServletRequest", httpServletRequest);
        return mv;
    }

    @GetMapping
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("produtos/produtos-lista");
        mv.addObject("produtos", produtos.listar());
        return mv;
    }

}
