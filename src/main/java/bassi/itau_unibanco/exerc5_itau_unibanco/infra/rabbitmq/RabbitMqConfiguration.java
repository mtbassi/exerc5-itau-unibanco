package bassi.itau_unibanco.exerc5_itau_unibanco.infra.rabbitmq;

import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    public static final String CADASTRO_PRODUTO_QUEUE = "itau-unibanco-cadastro-produto";

    @Bean
    public Declarable produtoCadastradoQueue() {
        return new Queue(CADASTRO_PRODUTO_QUEUE);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
