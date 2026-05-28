package io.github.cursodsouza.libraryapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "livro")
@Data // incorpora os: getter e setters e outras outras (to string, etc.) deixando completo
@ToString(exclude = "autor") // é campo para relacionamento e não precisa mostrar.
@EntityListeners(AuditingEntityListener.class)
public class Livro {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    //@Enumerated(EnumType.ORDINAL) para guardar a posicao de cada item do enum
    @Enumerated(EnumType.STRING) // com o string nao corre o risco de mudar a ordem e assim começar a não bater com ja salvo no BD
    @Column(name = "genero", length = 30, nullable = false)
    private GeneroLivro genero;

    @Column(name = "preco", precision = 18, scale = 2)
    private BigDecimal preco;

    @ManyToOne(
    //      cascade = CascadeType.ALL, (cascade = CascadeType.ALL) //Não sera usado, pois aqui critico trabalhar com cascade
            //fetch = FetchType.EAGER // É por padrão não necessita colocar. Trás o autor ao 'ler' o objeto livro.
            fetch = FetchType.LAZY // Não trás o autor ao 'ler' o objeto livro.
    )

    @JoinColumn(name = "id_autor")
    private Autor autor;

    // Campos de auditoria:
    @CreatedDate
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // TO DO
    @Column(name = "id_usuario")
    private UUID idUsuario;
}