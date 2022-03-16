package com.udemy.cursomc.services.validation;

import com.udemy.cursomc.domain.Cliente;
import com.udemy.cursomc.domain.enums.TipoCliente;
import com.udemy.cursomc.dto.ClienteNewDTO;
import com.udemy.cursomc.repositories.ClienteRepository;
import com.udemy.cursomc.resources.exceptions.FieldMessage;
import com.udemy.cursomc.services.validation.utils.BR;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteInsertValidador implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
    @Autowired
    ClienteRepository repository;

    @Override
    public void initialize(ClienteInsert ann){

    }
    public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context){
        List<FieldMessage> list = new ArrayList<>();

        if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidSsn(objDto.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
        }

        if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidTfn(objDto.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
        }

        Cliente cliente = repository.findByEmail(objDto.getEmail());

        if (cliente != null){
            list.add(new FieldMessage("email", "Email já existente"));
        }

        for (FieldMessage e: list ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();

        }
        return list.isEmpty();
    }

}
