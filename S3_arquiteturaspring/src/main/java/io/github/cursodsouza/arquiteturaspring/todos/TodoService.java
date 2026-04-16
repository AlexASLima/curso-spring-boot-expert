package io.github.cursodsouza.arquiteturaspring.todos;

import org.springframework.stereotype.Service;

@Service
public class TodoService {

    // @Autowired esta injetando abaixo e não precisa do Autowires pois tem-se o construtor, abaixo e é gerenciada pelo @Service
    private TodoRepository repository;
    private TodoValidator validator;
    private MailSender mailSender;

    // @Autowired não necessita, devido a explicação acima
    public TodoService(TodoRepository todoRepository,
                       TodoValidator validator,
                       MailSender mailSender){
        this.repository = todoRepository;
        this.validator = validator;
        this.mailSender = mailSender; // devido a esse construtor o spring já injeta dependencia aqui nessa linhas acima.
    }

    public TodoEntity salvar(TodoEntity novoTodo){
        validator.validar(novoTodo);
        return repository.save(novoTodo);
    }

    public void atualizarStatus(TodoEntity todo){
        repository.save(todo);
        String status = todo.getConcluido() == Boolean.TRUE ? "Concluido" : "Não concluido";
        mailSender.enviar("Todo "  + todo.getDescricao() + " foi atualizado para " + status);
    }

    public TodoEntity buscarPorId(Integer id){
        return repository.findById(id).orElse(null);
    }
}
