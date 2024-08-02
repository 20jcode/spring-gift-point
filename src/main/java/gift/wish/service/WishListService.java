package gift.wish.service;


import gift.repository.JpaMemberRepository;
import gift.repository.JpaProductRepository;
import gift.repository.JpaWishRepository;
import gift.exceptionAdvisor.exceptions.GiftNotFoundException;
import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.wish.entity.Wish;
import gift.wish.dto.WishListResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WishListService {

    private final JpaMemberRepository jpaMemberRepository;
    private final JpaWishRepository jpaWishRepository;
    private final JpaProductRepository jpaProductRepository;

    public WishListService(JpaMemberRepository jpaMemberRepository,
        JpaWishRepository jpaWishRepository, JpaProductRepository jpaProductRepository) {
        this.jpaMemberRepository = jpaMemberRepository;
        this.jpaWishRepository = jpaWishRepository;
        this.jpaProductRepository = jpaProductRepository;
    }

    public WishListResponse addProduct(long memberId, long productId) {
        Member member = jpaMemberRepository.findById(memberId)
            .orElseThrow(()->new GiftNotFoundException("회원이 존재하지 않습니다."));
        Product product = jpaProductRepository.findById(productId).orElseThrow();
        Wish wish = new Wish(member, product);
        member.addWish(wish);
        jpaWishRepository.save(wish);

        return new WishListResponse(memberId, jpaWishRepository.findAllByMemberId(memberId).stream()
            .collect(Collectors.toMap(Wish::getProductName, Wish::getProductCount)));
    }

    public void deleteProduct(long memberId, long productId) {
        Member member = jpaMemberRepository.findById(memberId)
            .orElseThrow(()-> new GiftNotFoundException("회원이 존재하지 않습니다."));
        Product product = jpaProductRepository.findById(productId)
            .orElseThrow(()-> new GiftNotFoundException("상품이 존재하지 않습니다."));
        Wish wish = jpaWishRepository.findByMemberIdAndProductId(memberId, productId)
            .orElseThrow(()-> new GiftNotFoundException("wish가 존재하지 않습니다."));
        member.delWish(wish);
        jpaWishRepository.delete(wish);
    }

    public WishListResponse updateProduct(long memberId, long productId, int productValue) {
        Wish wish = jpaWishRepository.findByMemberIdAndProductId(memberId, productId)
            .orElseThrow(()-> new GiftNotFoundException("wish가 존재하지 않습니다."));

        wish.productCountUpdate(productValue);

        return new WishListResponse(memberId,jpaWishRepository.findAllByMemberId(memberId).stream()
            .collect(Collectors.toMap(Wish::getProductName, Wish::getProductCount)));
    }

    public WishListResponse getWishList(long memberId) {
        Member member = jpaMemberRepository.findById(memberId)
            .orElseThrow(()-> new GiftNotFoundException("회원이 존재하지 않습니다."));
        Map<String, Integer> wishList = member.getWishList().stream()
            .collect(Collectors.toMap(Wish::getProductName, Wish::getProductCount));
        return new WishListResponse(member.getId(), wishList);
    }

    public WishListResponse getWishListPage(long memberId, int pageNumber, int pageSize, List<String> sort) {

        String sortedName = sort.get(0);
        String sortedOrder = sort.get(1);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortedOrder), sortedName));
        Map<String, Integer> wishlist = jpaWishRepository.findByMemberId(memberId, pageable)
            .stream().collect(Collectors.toMap(Wish::getProductName, Wish::getProductCount));
        return new WishListResponse(memberId, wishlist);
    }
}
