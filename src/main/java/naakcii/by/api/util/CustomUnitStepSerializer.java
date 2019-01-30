package naakcii.by.api.util;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomUnitStepSerializer extends StdSerializer<BigDecimal> {

    private static final long serialVersionUID = -3353389155483229296L;

    public CustomUnitStepSerializer() {
        this(null);
    }

    public CustomUnitStepSerializer(Class<BigDecimal> t) {
        super(t);
    }

    @Override
    public void serialize(BigDecimal step, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(step.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
    }
}