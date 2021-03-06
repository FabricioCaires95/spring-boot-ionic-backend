package br.com.CourseSpringBoot.service.validation;

import br.com.CourseSpringBoot.domain.Client;
import br.com.CourseSpringBoot.dto.ClientNewDTO;
import br.com.CourseSpringBoot.enums.ClientType;
import br.com.CourseSpringBoot.exceptions.FieldMessage;
import br.com.CourseSpringBoot.repositories.ClientRepository;
import br.com.CourseSpringBoot.service.validation.utils.BR;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fabricio
 */
public class ClientInsertValidation implements ConstraintValidator<ClientInsert, ClientNewDTO> {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void initialize(ClientInsert constraintAnnotation) {

    }

    @Override
    public boolean isValid(ClientNewDTO clientNewDTO, ConstraintValidatorContext constraintValidatorContext) {

        List<FieldMessage> list = new ArrayList<>();

        //check the client's type
        if (clientNewDTO.getClientType().equals(ClientType.PHYISICALPERSON.getCod()) && !BR.isValidCPF(clientNewDTO.getCpfOrCnpj())){
            list.add(new FieldMessage("cpfOrCnpj", "CPF is invalid"));
        }

        if (clientNewDTO.getClientType().equals(ClientType.LEGALPERSON.getCod()) && !BR.isValidCNPJ(clientNewDTO.getCpfOrCnpj())){
            list.add(new FieldMessage("cpfOrCnpj", "CNPJ is invalid"));
        }

        //chek if a email is duplicate
        Client aux = clientRepository.findByEmail(clientNewDTO.getEmail());
        if (aux != null){
            list.add(new FieldMessage("email", "duplicate email, this email already exists"));
        }

        for (FieldMessage e: list){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                .addConstraintViolation();

        }


        return list.isEmpty();
    }
}
