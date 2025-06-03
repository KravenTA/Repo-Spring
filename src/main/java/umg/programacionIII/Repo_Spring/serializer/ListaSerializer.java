package umg.programacionIII.Repo_Spring.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import umg.programacionIII.estructuras.lista.Lista;
import umg.programacionIII.estructuras.lista.Nodo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonComponent
public class ListaSerializer extends JsonSerializer<Lista<?>> {

    @Override
    public void serialize(Lista<?> lista, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();

        Nodo<?> actual = lista.leerPrimero();
        while (actual != null) {
            jsonGenerator.writeObject(actual.leerDato());
            actual = actual.siguiente();
        }

        jsonGenerator.writeEndArray();
    }
}
