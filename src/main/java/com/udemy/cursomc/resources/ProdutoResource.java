package com.udemy.cursomc.resources;

import com.udemy.cursomc.domain.Cliente;
import com.udemy.cursomc.domain.Produto;
import com.udemy.cursomc.dto.ClienteDTO;
import com.udemy.cursomc.dto.ProdutoDTO;
import com.udemy.cursomc.resources.utils.URL;
import com.udemy.cursomc.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

    @Autowired
    ProdutoService produtoService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> find(@PathVariable Integer id){
        Produto Produto = produtoService.find(id);
        return ResponseEntity.ok().body(Produto);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<ProdutoDTO>> findPage(
            @RequestParam(value = "nome", defaultValue = "") String nome,
            @RequestParam(value = "categorias", defaultValue = "") String categorias,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction){
        List<Integer> ids = URL.decodeIntList(categorias);
        Page<Produto> produtos = produtoService.search(URL.decodeParam(nome), ids, page, linesPerPage, orderBy, direction);
        Page<ProdutoDTO> produtosDTO = produtos.map(value -> new ProdutoDTO(value));
        return ResponseEntity.ok().body(produtosDTO);
    }


}
