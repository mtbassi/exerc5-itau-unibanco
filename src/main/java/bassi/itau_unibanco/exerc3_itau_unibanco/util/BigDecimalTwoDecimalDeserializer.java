package bassi.itau_unibanco.exerc3_itau_unibanco.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalTwoDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        var value = jsonParser.getText();
        try {
            var bigDecimal = new BigDecimal(value);
            if (bigDecimal.scale() > 2)
                throw new IllegalArgumentException("Valor não pode ter mais de duas casas decimais.");
            return bigDecimal.setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new IOException("O formato ou a escala do número fornecido é inválido.", e);
        }
    }
}
