package gift.product.service;

import gift.exceptionAdvisor.exceptions.GiftBadRequestException;
import gift.category.entity.Category;
import gift.option.entity.GiftOption;
import gift.product.dto.ProductRequest;
import gift.product.dto.ProductResponse;
import gift.product.entity.Product;
import gift.product.facadeRepository.ProductFacadeRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {

    private final ProductFacadeRepository productFacadeRepository;

    public ProductService(ProductFacadeRepository productFacadeRepository) {
        this.productFacadeRepository = productFacadeRepository;
    }

    public List<ProductResponse> readAll() {
        List<Product> productList = productFacadeRepository.findAll();
        return productList.stream().map(ProductResponse::new).toList();
    }

    public ProductResponse read(long id) {
        Product product = productFacadeRepository.getProduct(id);
        return new ProductResponse(product);
    }

    public ProductResponse create(ProductRequest productRequest) {

        checkKakao(productRequest.getName());

        final Product product = new Product(null, productRequest.getName(), productRequest.getPrice(),
            productRequest.getImageUrl());

        productRequest.getGiftOptions().forEach(giftOptionRequest -> {
            GiftOption giftOption = new GiftOption(null, giftOptionRequest.getName(), giftOptionRequest.getQuantity());
            product.addGiftOption(giftOption);
            });

        Category category = new Category(productRequest.getCategoryId());

        product.updateCategory(category);

        return new ProductResponse(productFacadeRepository.saveProduct(product));

    }


    public ProductResponse update(long id, ProductRequest productRequest) {

        Product product = productFacadeRepository.getProduct(id);

        product.update(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());

        checkKakao(product.getName());

        product = productFacadeRepository.saveProduct(product);

        return new ProductResponse(product);
    }

    public void delete(long id) {
        productFacadeRepository.deleteProduct(id);
    }

    private void checkKakao(String productName) {
        if (productName.contains("카카오")) {
            throw new GiftBadRequestException("카카오 문구는 MD 협의 이후 사용할 수 있습니다.");
        }
    }

    public List<ProductResponse> readProduct(int pageNumber, int pageSize,List<String> sort, Long categoryId) {

        String sortedName = sort.get(0);
        String sortedOrder = sort.get(1);


        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortedOrder), sortedName));

        List<Product> productList = productFacadeRepository.findByCategoryId(categoryId, pageable);

        return productList.stream().map(ProductResponse::new).toList();
    }
}
