package gift.category.controller;


import gift.category.dto.CategoryRequest;
import gift.category.dto.CategoryResponse;
import gift.category.service.CategoryService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/categories")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(categoryService.readAll());
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> add(@RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.create(categoryRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Integer id, @RequestBody CategoryRequest categoryRequest) {
        var body = categoryService.update(id, categoryRequest);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categoryService.delete(id);

        return ResponseEntity.ok().build();
    }
}
