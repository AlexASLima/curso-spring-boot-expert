package io.github.cursodsouza.arquiteturaspring.montadora.configuration;

import io.github.cursodsouza.arquiteturaspring.montadora.Motor;
import io.github.cursodsouza.arquiteturaspring.montadora.TipoMotor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MontadoraConfiguration {

    // Indica que sera uma dependencia para ser injetodo.
    @Bean(name = "motorAspirado") // não obrigatorio, se não colocar pega o nome do metodo.
    //@Primary // Para caso existir mais de 1 bean do msm tipo, como nesse caso (Motor), com essa
             // notacao esse sera o padrao injetado caso não informar o nome da injecao.
    public Motor motorAspirado(/*@Value("${app.montadora-padrao}") Integer cavalos*/){ // para pegar valor padrão do propreiets
        var motor = new Motor();
        motor.setCavalos(120);
        //motor.setCavalos(cavalos);
        motor.setCilindros(4);
        motor.setModelo("XPTO-0");
        motor.setLitragem(2.0);
        motor.setTipo(TipoMotor.ASPIRADO);
        return motor;
    }

    @Bean(name = "motorEletrico")
    public Motor motorEletrico(){
        var motor = new Motor();
        motor.setCavalos(110);
        motor.setCilindros(3);
        motor.setModelo("TH-40");
        motor.setLitragem(1.4);
        motor.setTipo(TipoMotor.ELETRICO);
        return motor;
    }

    @Primary
    @Bean(name = "motorTurbo")
    public Motor motorTurbo(){
        var motor = new Motor();
        motor.setCavalos(180);
        motor.setCilindros(4);
        motor.setModelo("XPTO-01");
        motor.setLitragem(1.5);
        motor.setTipo(TipoMotor.TURBO);
        return motor;
    }
}
