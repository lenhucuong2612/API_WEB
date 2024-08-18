package com.example.API.service.product;

import com.example.API.dto.ProductDto;
import com.example.API.model.Product;
import com.example.API.request.AddProductRequest;
import com.example.API.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest request);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest request, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand,String name);
    public List<ProductDto> getConvertedProducts(List<Product> products);
    public ProductDto convertDto(Product product) ;
}
