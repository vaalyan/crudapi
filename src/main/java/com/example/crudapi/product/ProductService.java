package com.example.crudapi.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {
    HashMap<String, Object> datos;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public List<Product> getProducts(){
        return this.productRepository.findAll();
    }

    public ResponseEntity<Object> newProduct(Product product) {
        Optional<Product> res = productRepository.findProductByNombre(product.getNombre());
        datos = new HashMap<>();

        if(res.isPresent() && product.getId()==null){
            datos.put("Error",true);
            datos.put("Mensaje","Ya existe un producto con ese nombre");
           return new ResponseEntity<>(
                   datos,
                   HttpStatus.CONFLICT
           );
        }
        datos.put("Mensaje","Se guardó con éxito");
        if(product.getId()!=null){
            datos.put("Mensaje","Se actualizó con éxito");

        }
        productRepository.save(product);
        datos.put("Datos",product);

        return new ResponseEntity<>(
                datos,
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<Object> deleteproduct(Long id){
        datos = new HashMap<>();
        boolean existe=this.productRepository.existsById(id);
        if(!existe){
            datos.put("Error",true);
            datos.put("Mensaje","No existe un producto con ese ID");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
        }
        productRepository.deleteById(id);
        datos.put("Mensaje","Producto eliminado");
        return new ResponseEntity<>(
                datos,
                HttpStatus.ACCEPTED
        );
    }
}
