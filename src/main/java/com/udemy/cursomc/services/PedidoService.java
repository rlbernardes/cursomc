package com.udemy.cursomc.services;

import com.udemy.cursomc.domain.ItemPedido;
import com.udemy.cursomc.domain.PagamentoComBoleto;
import com.udemy.cursomc.domain.Pedido;
import com.udemy.cursomc.domain.enums.EstadoPagamento;
import com.udemy.cursomc.repositories.ItemPedidoRepository;
import com.udemy.cursomc.repositories.PagamentoRepository;
import com.udemy.cursomc.repositories.PedidoRepository;
import com.udemy.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class PedidoService {
    PedidoRepository pedidoRepository;
    PagamentoRepository pagamentoRepository;
    BoletoService boletoService;
    ProdutoService produtoService;
    ItemPedidoRepository itemPedidoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, BoletoService boletoService, PagamentoRepository pagamentoRepository, ProdutoService produtoService, ItemPedidoRepository itemPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.boletoService = boletoService;
        this.pagamentoRepository = pagamentoRepository;
        this.produtoService = produtoService;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public Pedido find(Integer id){
        Optional<Pedido> optionalPedido = pedidoRepository.findById(id);
        return optionalPedido.orElseThrow(()-> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", tipo: " + Pedido.class.getName() ));
    }

    @Transactional
    public Pedido insert(Pedido pedido){
        pedido.setId(null);
        pedido.setInstate(new Date());
        pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
        pedido.getPagamento().setPedido(pedido);
        if(pedido.getPagamento() instanceof PagamentoComBoleto){
            PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
            boletoService.preenchePagamentoComBoleto(pagto, pedido.getInstate());
        }
        pedido = pedidoRepository.save(pedido);
        pagamentoRepository.save(pedido.getPagamento());

        for(ItemPedido item: pedido.getItens()){
            item.setDesconto(0.0);
            item.setPreco(produtoService.find(item.getProduto().getId()).getPreco());
            item.setPedido(pedido);
        }
        itemPedidoRepository.saveAll(pedido.getItens());
        return pedido;
    }
}
