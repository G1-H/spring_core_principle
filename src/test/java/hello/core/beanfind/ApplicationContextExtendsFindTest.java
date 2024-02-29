package hello.core.beanfind;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextExtendsFindTest {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfig.class );

    @Test
    @DisplayName("부모 타입으로 조회 시, 자식이 두 개 이상일 경우 중복 오류가 발생.")
    void findBeanByParentTypeDuplicate(){
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> applicationContext.getBean(DiscountPolicy.class));

    }

    @Test
    @DisplayName("부모 타입으로 조회 시, 자식이 두 개 이상일 경우, 이름 같이 조회")
    void findBeanByParentTypeAndName(){
        DiscountPolicy fixDiscountPolicy = applicationContext.getBean("fixDiscountPolicy", DiscountPolicy.class);
        DiscountPolicy rateDiscountPolicy = applicationContext.getBean("rateDiscountPolicy", DiscountPolicy.class);

        assertThat(fixDiscountPolicy).isInstanceOf(FixDiscountPolicy.class);
        assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);

    }

    @Test
    @DisplayName("특정 하위 타입으로 조회")
    void findBeanBySubType(){
        FixDiscountPolicy bean = applicationContext.getBean(FixDiscountPolicy.class);

        assertThat(bean).isInstanceOf(FixDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회")
    void findAllBeansByParentType() {
        Map<String, DiscountPolicy> beansOfType = applicationContext.getBeansOfType(DiscountPolicy.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " / value = " + beansOfType.get(key));
        }
        System.out.println("beanOfType = " + beansOfType);
        assertThat(beansOfType.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회 - Object")
    void findAllBeansByObjectType() {
        Map<String, Object> beansOfType = applicationContext.getBeansOfType(Object.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " / value = " + beansOfType.get(key));

        }
    }


    @Configuration
    static class TestConfig {
        @Bean
        public DiscountPolicy rateDiscountPolicy() {
            return new RateDiscountPolicy();
        }

        @Bean
        public DiscountPolicy fixDiscountPolicy() {
            return new FixDiscountPolicy();
        }
    }
}
