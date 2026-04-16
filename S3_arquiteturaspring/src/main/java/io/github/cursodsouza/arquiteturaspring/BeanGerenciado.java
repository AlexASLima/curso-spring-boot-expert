package io.github.cursodsouza.arquiteturaspring;

import io.github.cursodsouza.arquiteturaspring.todos.TodoEntity;
import io.github.cursodsouza.arquiteturaspring.todos.TodoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

//@Lazy(false)
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
//@Scope(WebApplicationContext.SCOPE_APPLICATION)
//@Scope("request")
//@Scope("session")
//@Scope("application")
public class BeanGerenciado {

    private String idUsuarioLogado;

    @Autowired // * primeira forma
    private TodoValidator validator;

    //@Autowired * terceira forma. Essa por construtor, não é obrigatorio usar o @Autowired. Essa é a recomendada pelo spring.
    public BeanGerenciado(TodoValidator validator) {
        this.validator = validator;
    }

    public void utilizar(){
        var todo = new TodoEntity();
        validator.validar(todo);
    }

    @Autowired  // * segunda forma
    public void setValidator(TodoValidator validator){
        this.validator = validator;
    }
}
