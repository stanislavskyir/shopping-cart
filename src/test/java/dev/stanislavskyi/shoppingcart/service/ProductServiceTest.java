package dev.stanislavskyi.shoppingcart.service;

import dev.stanislavskyi.shoppingcart.exceptions.ResourceNotFoundException;
import dev.stanislavskyi.shoppingcart.model.Category;
import dev.stanislavskyi.shoppingcart.model.Product;
import dev.stanislavskyi.shoppingcart.repository.CategoryRepository;
import dev.stanislavskyi.shoppingcart.repository.ImageRepository;
import dev.stanislavskyi.shoppingcart.repository.ProductRepository;
import dev.stanislavskyi.shoppingcart.request.AddProductRequest;
import dev.stanislavskyi.shoppingcart.request.ProductUpdateRequest;
import dev.stanislavskyi.shoppingcart.service.product.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void givenValidRequest_whenAddProduct_thenReturnSavedProduct(){
        // given
        AddProductRequest addProductRequest = new AddProductRequest();
        addProductRequest.setName("Apple watch X");
        addProductRequest.setBrand("Apple");
        addProductRequest.setPrice(BigDecimal.TEN);
        addProductRequest.setInventory(10);
        addProductRequest.setDescription("This is a test product");
        Category category = new Category("Watch");
        addProductRequest.setCategory(category);


        when(productRepository.existsByNameAndBrand(addProductRequest.getName(), addProductRequest.getBrand()))
                .thenReturn(false);

        when(categoryRepository.findByName(addProductRequest.getCategory().getName())).thenReturn(null);

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Product product = new Product("Apple watch X", "Apple", BigDecimal.TEN, 10, "Latest iPhone", category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // when
        Product result = productService.addProduct(addProductRequest);

        // then
        assertNotNull(result);
        assertEquals("Apple watch X", result.getName());
        assertEquals("Apple", result.getBrand());

    }

    @Test
    public void givenProductId_whenGetProductId_thenReturnProduct() {
        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setName("IPhone X");
        product.setBrand("Apple");
        product.setPrice(BigDecimal.TEN);
        product.setInventory(10);
        product.setDescription("This is a test product");
        Category category = new Category("Phones");
        product.setCategory(category);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(productId);
        assertNotNull(result);
        assertEquals(productId, result.getId());

    }


    @Test
    public void givenValidateId_whenDeleteProductById_thenRepositoryDeleteCalled(){
        Long productId = 1L;
        Product product = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when
        productService.deleteProductById(1L);

        verify(productRepository, times(1)).delete(product);

    }

    @Test
    public void givenValidRequest_whenUpdateProduct_thenReturnUpdatedProduct() {
        // given
        Product existingProduct = new Product("OldName", "OldBrand", BigDecimal.TEN, 5, "OldDesc", new Category("OldCat"));
        existingProduct.setId(1L);

        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName("NewName");
        request.setBrand("NewBrand");
        request.setPrice(BigDecimal.TEN);
        request.setInventory(10);
        request.setDescription("NewDesc");
        request.setCategory(new Category("NewCat"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findByName("NewCat")).thenReturn(new Category("NewCat"));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Product result = productService.updateProduct(request, 1L);

        // then
        assertEquals("NewName", result.getName());
        assertEquals("NewBrand", result.getBrand());
        assertEquals(BigDecimal.TEN, result.getPrice());
    }

}
