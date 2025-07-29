package com.example.auth.conf;
import com.example.auth.exceptions.UserExistsWithEmail;
import com.example.auth.exceptions.UserExistsWithUsername;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.example.auth.entity.MessageForValidation;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<MessageForValidation> handleValidationException(MethodArgumentNotValidException e) {
        List<MessageForValidation> listOfExceptions = new ArrayList<>();
        for(ObjectError o: e.getBindingResult().getAllErrors()){
            listOfExceptions.add(new MessageForValidation(o.getDefaultMessage()));
        }
        return listOfExceptions;
    }

    @ExceptionHandler({UserExistsWithEmail.class, UserExistsWithUsername.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageForValidation handleUserAlreadyExists(RuntimeException e) {
        return new MessageForValidation(e.getMessage());
    }
}
