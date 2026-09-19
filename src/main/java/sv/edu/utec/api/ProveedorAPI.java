package sv.edu.utec.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import sv.edu.utec.modelo.Producto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ProveedorAPI {
    public List<Producto> obtenerProductos(int limite) throws IOException, InterruptedException {
        String url = "https://dummyjson.com/products?limit=" + limite + "&select=title,stock";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest solicitud =HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(solicitud, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Error en la solicitud. Código: " + response.statusCode());
        }

        ObjectMapper mapper = new ObjectMapper();
        RespuestaProductos resp = mapper.readValue(response.body(), RespuestaProductos.class);

        List<Producto> productos = new ArrayList<>();
        if (resp !=null && resp.getProducts() != null ) {
            for (ProductoApi pApi : resp.getProducts()) {
                productos.add(pApi.aProducto());
            }
        }
        return productos;
    }
}
