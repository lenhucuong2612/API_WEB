package com.example.API.service.product;

import com.example.API.dto.ImageDto;
import com.example.API.dto.ProductDto;
import com.example.API.exception.ProductNotFoundException;
import com.example.API.exception.ResourceNotFoundException;
import com.example.API.model.Category;
import com.example.API.model.Image;
import com.example.API.model.Product;
import com.example.API.repository.CategoryRepository;
import com.example.API.repository.ImageRepository;
import com.example.API.repository.ProductRepository;
import com.example.API.request.AddProductRequest;
import com.example.API.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;
    @Override
    public Product addProduct(AddProductRequest request) {
        //check is the category is found is the db
        //If yes, set it as the new product category
        //If no, the save it as a new category
        //The set as the new product category
        Category category= Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()->{
                    Category newCategory=new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }
    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                ()->{throw new ResourceNotFoundException("Product not found!");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(exitstingProduct->updateExitstingProduct(exitstingProduct,request))
                .map(productRepository::save)
                .orElseThrow(()->new ResourceNotFoundException("Product not found"));
    }
    private Product updateExitstingProduct(Product exitstingProduct, ProductUpdateRequest request){
        exitstingProduct.setName(request.getName());
        exitstingProduct.setBrand(request.getBrand());
        exitstingProduct.setPrice(request.getPrice());
        exitstingProduct.setInventory(request.getInventory());
        exitstingProduct.setDescription(request.getDescription());
        Category category=categoryRepository.findByName(request.getCategory().getName());
        exitstingProduct.setCategory(category);
        return exitstingProduct;
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }
    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertDto).toList();
    }
    @Override
    public ProductDto convertDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream().map(image -> modelMapper
                        .map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
