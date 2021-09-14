package com.udemy.cursomc.services.exceptions;

import com.udemy.cursomc.domain.Pedido;
import com.udemy.cursomc.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    public Pedido buscar(Integer id){
        Optional<Pedido> optionalPedido = pedidoRepository.findById(id);
        return optionalPedido.orElseThrow(()-> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", tipo: " + Pedido.class.getName() ));
    }

}
