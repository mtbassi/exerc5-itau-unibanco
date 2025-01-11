package bassi.itau_unibanco.exerc5_itau_unibanco.producer;

import bassi.itau_unibanco.exerc5_itau_unibanco.entity.ProdutoEntity;
import bassi.itau_unibanco.exerc5_itau_unibanco.infra.rabbitmq.RabbitMqConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastroProdutoProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(ProdutoEntity message) {
        var messageProperties = new MessageProperties();
        messageProperties.setContentType(MediaType.APPLICATION_JSON_VALUE);
        this.rabbitTemplate.send(
                RabbitMqConfiguration.CADASTRO_PRODUTO_QUEUE,
                new Message(this.serializeToJson(message).getBytes(), messageProperties)
        );
    }

    private String serializeToJson(ProdutoEntity data) {
        try {
            return new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Erro ao serializar DTO para fila.", e);
        }
    }
}