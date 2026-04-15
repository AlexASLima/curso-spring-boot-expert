package io.github.cursodsouza.arquiteturaspring.montadora.api;


import io.github.cursodsouza.arquiteturaspring.montadora.CarroStatus;
import io.github.cursodsouza.arquiteturaspring.montadora.Chave;
import io.github.cursodsouza.arquiteturaspring.montadora.HondaHRV;
import io.github.cursodsouza.arquiteturaspring.montadora.Motor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carros")
public class TesteFabricaController {
    @Autowired // Injetando a dependencia 'private Motor motor' da classe MontadoraConfiguration nessa classe.
    //@Qualifier("motorAspirado") // Tem q setar o nome do bean quando tem mais de 1 bean do msm tipo no container/app.
                                 // escolhe o bean a ser injetado.
    @Eletrico // Faz o papel do acima, foi criado uma anotation propria para injetar motor do tipo 'Eletrico'.
    private Motor motor;

    @PostMapping
    public CarroStatus ligarCarro(@RequestBody Chave chave){
        var carro = new HondaHRV(motor);
        return carro.darIgnicao(chave);
    }
}
