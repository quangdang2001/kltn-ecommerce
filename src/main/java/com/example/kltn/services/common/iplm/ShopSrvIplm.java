package com.example.kltn.services.common.iplm;

import com.example.kltn.models.Product;
import com.example.kltn.repos.ProductShopRepo;
import com.example.kltn.repos.UserRepo;
import com.example.kltn.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopSrvIplm {
    private final UserRepo userRepo;
    private final ProductShopRepo productShopRepo;

    public List<Product.ProductShop> findShopNearBy(String productOptionId) {
        var address = userRepo.findUserAddressDefault(UserUtil.getIdCurrentUser());
        var productShops = productShopRepo.findShopAvailableNearBy(productOptionId, address.getProvince());
        var producShopSorted = new ArrayList<Product.ProductShop>();
        var productShopSortedlv1 = new ArrayList<Product.ProductShop>();
        var productShopSortedlv2 = new ArrayList<Product.ProductShop>();
        var productShopSortedlv3 = new ArrayList<Product.ProductShop>();

        productShops.forEach(productShop -> {
            var shopProvince = productShop.getShop().getProvince();
            var shopDistrict = productShop.getShop().getDistrict();
            var shopWard = productShop.getShop().getWard();
            if (shopProvince.equals(address.getProvince())&&
                    shopDistrict.equals(address.getDistrict())&&
                            shopWard.equals(address.getWard())){
                productShopSortedlv1.add(productShop);
            } else if (shopProvince.equals(address.getProvince())&&
                    shopDistrict.equals(address.getDistrict())){
                productShopSortedlv2.add(productShop);
            } else if (shopProvince.equals(address.getProvince())){
                productShopSortedlv3.add(productShop);
            }
        });
            productShopSortedlv1.sort(Comparator.comparing(Product.ProductShop::getQuantity).reversed());
            productShopSortedlv2.sort(Comparator.comparing(Product.ProductShop::getQuantity).reversed());
            productShopSortedlv3.sort(Comparator.comparing(Product.ProductShop::getQuantity).reversed());

            producShopSorted.addAll(productShopSortedlv1);
            producShopSorted.addAll(productShopSortedlv2);
            producShopSorted.addAll(productShopSortedlv3);
        return producShopSorted;
    }
}
