package com.udemy.cursomc.services.validation;

import com.udemy.cursomc.domain.Cliente;
import com.udemy.cursomc.domain.enums.TipoCliente;
import com.udemy.cursomc.dto.ClienteDTO;
import com.udemy.cursomc.dto.ClienteNewDTO;
import com.udemy.cursomc.repositories.ClienteRepository;
import com.udemy.cursomc.resources.exceptions.FieldMessage;
import com.udemy.cursomc.services.validation.utils.BR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    ClienteRepository repository;

    @Override
    public void initialize(ClienteUpdate ann){

    }
    public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context){
        Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Integer uriID =  Integer.parseInt(map.get("id"));

        List<FieldMessage> list = new ArrayList<>();

        Cliente cliente = repository.findByEmail(objDto.getEmail());
        if (cliente != null && !cliente.getId().equals(uriID)){
            list.add(new FieldMessage("email", "Email já existente"));
        }

        for (FieldMessage e: list ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();

        }
        return list.isEmpty();
    }
}
