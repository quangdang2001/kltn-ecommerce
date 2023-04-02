package com.example.kltn.services.common.iplm;

import com.example.kltn.dto.ProductSearchReq;
import com.example.kltn.dto.SearchProductResp;
import com.example.kltn.exceptions.NotFoundException;
import com.example.kltn.models.Product;
import com.example.kltn.models.Shop;
import com.example.kltn.models.User;
import com.example.kltn.repos.*;
import com.example.kltn.services.common.ProductSrv;
import com.example.kltn.utils.MoneyConvert;
import com.example.kltn.utils.ProductHandler;
import com.example.kltn.utils.SlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSrvIplm implements ProductSrv {
    private final ProductRepo productRepo;
    private final ProductOptionRepo productOptionRepo;
    private final ProductShopRepo productShopRepo;
    private final ShopRepo shopRepo;
    private final CategoryRepo categoryRepo;

    private final ModelMapper modelMapper;

    @Override
    @Transactional()
    public Product saveNewProduct(Product productReq) {
        productReq.setSlug(SlugGenerator.slugify(productReq.getName()));
        if (productReq.getProductOptions() != null && productReq.getProductOptions().size() > 0) {
            productReq.getProductOptions().forEach(option -> {
                String key = "%s %s %s".formatted(productReq.getName(), option.getOptionName(), option.getColor());
                option.setKey(SlugGenerator.slugify(key));
                option.setFullName(key);
                option.setSalePrice(MoneyConvert.calculaterPrice(option.getMarketPrice(), option.getPromotion()));
            });
            productReq.setBestPrice(ProductHandler.getBestPrice(productReq));
            productReq.setBestPromotion(ProductHandler.getBestPromotion(productReq));
            productOptionRepo.saveAll(productReq.getProductOptions());
        }
        Product product = productRepo.save(productReq);
        return product;
    }

    @Override
    public Product findProductById(String productId) {
        Product product = productRepo.findById(productId).orElse(null);
        product.setView(product.getView() + 1);
        productRepo.save(product);
        return product;
    }

    @Override
    public Product updateProduct(Product productReq) {
        return null;
    }

    @Override
    public List<Product> getProductByKeyword(ProductSearchReq searchReq) {
        Pageable pageable;
        if (searchReq.getOrderBy().equals("asc"))
            pageable = PageRequest.of(searchReq.getPage(), searchReq.getSize(), Sort.by(searchReq.getSortBy()).ascending());
        else
            pageable = PageRequest.of(searchReq.getPage(), searchReq.getSize(), Sort.by(searchReq.getSortBy()).descending());
        var dataSearched = productRepo.searchProducts(searchReq.getKeyword(),
                searchReq.getCate(), searchReq.getManu(), pageable);
        return dataSearched;
    }

    @Override
    public void disableProduct(String productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product ID not found"));
        product.setEnable(!product.isEnable());
        productRepo.save(product);
    }

    @Override
    public void updateQuantityProduct(String shopId, String productOptionId, Integer quantity) {
        Product.ProductShop productShop = productShopRepo.findProductShopByProductOption_IdAndShop_Id(productOptionId, shopId);
        if (productShop == null) {
            log.info("Update Quantity Product - New");
            Product.ProductShop productShopNew = new Product.ProductShop();
            productShopNew.setQuantity(quantity);
            productShopNew.setProductOption(productOptionRepo.findById(productOptionId).orElseThrow());
            productShopNew.setShop(shopRepo.findById(shopId).orElseThrow());
            productShopRepo.save(productShopNew);
        } else {
            log.info("Update Quantity Product - Update");
            productShop.setQuantity(quantity);
            productShopRepo.save(productShop);
        }
    }
}
