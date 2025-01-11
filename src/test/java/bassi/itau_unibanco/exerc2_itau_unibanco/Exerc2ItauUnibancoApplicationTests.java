package bassi.itau_unibanco.exerc2_itau_unibanco;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

@Epic("Configuração do Ambiente de Teste")
@Feature("Carregamento de Perfis")
@SpringBootTest
@ActiveProfiles({"test"})
class Exerc2ItauUnibancoApplicationTests {

    @Autowired
    private Environment environment;

    @Test
    @Story("Validação do Carregamento de Perfis")
    @Description("Verifica se o perfil 'test' é carregado corretamente durante a inicialização da aplicação, garantindo a configuração do ambiente de teste.")
    @DisplayName("Validação do perfil ativo: \"test\"")
    void contextLoads() {
        Assertions.assertTrue(
                Arrays.stream(this.environment.getActiveProfiles())
                        .anyMatch("test"::equalsIgnoreCase)
        );
    }
}
