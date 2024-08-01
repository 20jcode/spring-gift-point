package gift.wish.dto;

import gift.wish.entity.Wish;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WishListResponse {

    private Long memberId;
    private Map<String,Integer> wishList;


    public WishListResponse() {
    }

    public WishListResponse(Long memberId, List<Wish> wishList) {
        this.memberId = memberId;
        this.wishList = wishList.stream().collect(
            Collectors.toMap(wish -> wish.getProduct().getName(), Wish::getProductCount));
    }

    public WishListResponse(Wish wish,Map<String,Integer> wishList){
        this.memberId = wish.getMember().getId();
        this.wishList = wishList;
    }

    public WishListResponse(Long memberId, Map<String, Integer> wishList) {
        this.memberId = memberId;
        this.wishList = wishList;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Map<String, Integer> getWishList() {
        return wishList;
    }

}
