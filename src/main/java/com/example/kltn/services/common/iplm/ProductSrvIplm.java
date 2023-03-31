package com.example.kltn.services.common.iplm;

import com.example.kltn.exceptions.NotFoundException;
import com.example.kltn.models.Product;
import com.example.kltn.models.Shop;
import com.example.kltn.models.User;
import com.example.kltn.repos.ProductOptionRepo;
import com.example.kltn.repos.ProductRepo;
import com.example.kltn.repos.ProductShopRepo;
import com.example.kltn.repos.ShopRepo;
import com.example.kltn.services.common.ProductSrv;
import com.example.kltn.utils.SlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(rollbackFor = {DuplicateKeyException.class})
    public Product saveNewProduct(Product productReq) {
        productReq.setSlug(SlugGenerator.slugify(productReq.getName()));
        if (productReq.getProductOptions() != null && productReq.getProductOptions().size() > 0) {
            productReq.getProductOptions().forEach(option -> {
                String key = "%s %s %s".formatted(productReq.getName(), option.getOptionName(), option.getColor());
                option.setKey(SlugGenerator.slugify(key));
                option.setFullName(key);
            });
            productOptionRepo.saveAll(productReq.getProductOptions());
        }
        Product product = productRepo.save(productReq);
        return product;
    }

    @Override
    public Product findProductById(String productId) {
        Product product = productRepo.findById(productId).orElse(null);
        return product;
    }

    @Override
    public Product updateProduct(Product productReq) {
        return null;
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
        }
        else {
            log.info("Update Quantity Product - Update");
            productShop.setQuantity(quantity);
            productShopRepo.save(productShop);
        }
    }
}
