package helpers;

import org.neo4j.driver.Value;

import java.time.LocalDateTime;

public class TypeConverter {
    /**
     * Converte um objeto Value do Neo4j para um LocalDateTime.
     * @param value O objeto Value retornado pelo driver.
     * @return um objeto LocalDateTime ou null se o valor for nulo.
     */
    public static LocalDateTime toLocalDateTime(Value value) {
        if (value == null || value.isNull()) {
            return null;
        }
        return value.asLocalDateTime();
    }
}
