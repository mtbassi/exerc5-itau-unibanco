package bassi.itau_unibanco.exerc5_itau_unibanco.listener;

import bassi.itau_unibanco.exerc5_itau_unibanco.entity.ProdutoEntity;
import bassi.itau_unibanco.exerc5_itau_unibanco.infra.rabbitmq.RabbitMqConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class CadastroProdutoListener {

    @RabbitListener(queues = RabbitMqConfiguration.CADASTRO_PRODUTO_QUEUE)
    public void listen(Message<ProdutoEntity> message) {
        log.info("""
                                                        
                        Evento: Cadastro de Produto
                        Status: Sucesso
                        Produto ID: {}
                        Payload: {}
                        Timestamp: {}""",
                message.getPayload().getId(),
                message.getPayload(),
                LocalDateTime.now()
        );
    }
}