package com.company.json.converter;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import static com.company.util.MathUtils.BIGDECIMAL_ROUNDING_MODE;
import static com.company.util.MathUtils.BIGDECIMAL_SCALE;

@Configuration
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
	@Override
	public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jgen.writeString(value.setScale(BIGDECIMAL_SCALE, BIGDECIMAL_ROUNDING_MODE).toString());
	}
}
