package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    //    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    // 정액에서 정률로 할인 정책이 변화됨에 따라, DIP(Dependency Inversion Principle),
    // OCP(Open/Closed Principle) 원칙 위배됨
    // => 해결을 위해 관심사의 분리 필요 AppConfig(구현객체 생성하고 연결하는 책임을 가지는 클래스)
    //    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
