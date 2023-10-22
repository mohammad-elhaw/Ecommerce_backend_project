package com.backend.ecommerce.service;

import com.backend.ecommerce.api.dto.CreateProductDTO;
import com.backend.ecommerce.model.Product;
import com.backend.ecommerce.model.repository.ProductRepo;
import com.backend.ecommerce.service.interfaces.IDiscountService;
import com.backend.ecommerce.util.DiscountJob;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class DiscountService implements IDiscountService {

    private final ProductRepo productRepo;
    private final TaskScheduler taskScheduler;

    private void scheduleDiscountEnd(Product product) {
        DiscountJob discountJob = new DiscountJob(product, productRepo);
        Instant endInstant = product.getDiscountEndDate().atZone(ZoneId.systemDefault()).toInstant();
        taskScheduler.schedule(discountJob, endInstant);
    }

    @Override
    @Transactional
    public void addDiscountForProduct(Product product, CreateProductDTO createProductDTO) {
        product.setDiscountPercent(createProductDTO.getDiscountPercent());

        if(createProductDTO.getDiscountPercent() > 0){
            if(createProductDTO.getDaysForDiscount() > 0
                    || createProductDTO.getHoursForDiscount() > 0
                    || createProductDTO.getMinutesForDiscount() > 0){
                double discountPrice = createProductDTO.getPrice() - ((createProductDTO.getDiscountPercent() * 0.01) * createProductDTO.getPrice());
                product.setDiscountPrice(discountPrice);
                product.setDiscountStartDate(LocalDateTime.now());
                product.setDiscountEndDate(LocalDateTime.now()
                        .plusDays(createProductDTO.getDaysForDiscount())
                        .plusHours(createProductDTO.getHoursForDiscount())
                        .plusMinutes(createProductDTO.getMinutesForDiscount()));
                scheduleDiscountEnd(product);
                productRepo.save(product);

            }
        }
    }

//    @Scheduled(fixedRate = 60000)
//    public void checkDiscountEndDateAndTriggerEvent(){
//        log.info("checkDiscountEndDateAndTriggerEvent() method calls");
//        LocalDateTime currentDate = LocalDateTime.now();
//        List<Product> products = productRepo.findProductExceededDiscountEndDate(currentDate);
//        for(Product product : products){
//            triggerDiscountEvent(product);
//        }
//    }
//
//    private void triggerDiscountEvent(Product product) {
//        DiscountEndEvent event = new DiscountEndEvent(product);
//        CompletableFuture.runAsync(()->publisher.publishEvent(event));
//    }

}
