package gift.product.controller;

import gift.product.dto.ProductRequest;
import gift.product.dto.ProductResponse;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 상품 추가,수정,삭제,조회를 위한 api end-point
 * <p>
 * $/api/products
 */
@RequestMapping("/api")
@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getList() {
        return ResponseEntity.ok(productService.readAll());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(productService.read(id));
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> add(@RequestBody @Valid ProductRequest productRequest) {
        var body = productService.create(productRequest);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid ProductRequest productRequest) {
        if (id == null) {
            throw new IllegalArgumentException("id를 입력해주세요");
        }
        productService.update(id, productRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getPage(@RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "id") List<String> sortedBy,
            @RequestParam(defaultValue = "0") Long categoryId) {

        return ResponseEntity.ok(productService.readProduct(page, size,sortedBy,categoryId));
    }

}