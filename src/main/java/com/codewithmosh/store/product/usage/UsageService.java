package com.codewithmosh.store.product.usage;

import com.codewithmosh.store.common.EntityNotFoundException;
import com.codewithmosh.store.product.item.ChemicalProduct;
import com.codewithmosh.store.product.item.ProductNotFoundExceptionException;
import com.codewithmosh.store.product.item.ProductRepository;
import com.codewithmosh.store.product.stock.StockAlert;
import com.codewithmosh.store.product.stock.StockAlertRepository;
import com.codewithmosh.store.user.UserNotFoundException;
import com.codewithmosh.store.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class UsageService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UsageRepository usageRepository;
    private final UsageMapper usageMapper;
    private final StockAlertRepository stockAlertRepository;

    public Iterable<UsageDto> getUsages() {
        return usageRepository.findAll()
                .stream()
                .map(usageMapper::toDto)
                .toList();
    }

    public UsageDto getUsage(Integer id) {
        var usage = usageRepository.findById(id).orElse(null);
        return usage != null ? usageMapper.toDto(usage) : null;
    }

    public UsageDto createUsage(CreateUsageRequest request) {
        var product = productRepository.findById(request.getProductId()).orElseThrow(ProductNotFoundExceptionException::new);

        var user = userRepository.findById(request.getTakenBy()).orElseThrow(UserNotFoundException::new);

        float newStock = product.getCurrentStock() - request.getAmount();

        validateAndHandleStock(product, newStock);

        ChemicalUsage usage = usageMapper.toEntity(request);
        usage.setProduct(product);
        usage.setUser(user);

        usageRepository.save(usage);
        product.setCurrentStock(product.getCurrentStock() - request.getAmount());
        productRepository.save(product);
        return usageMapper.toDto(usage);
    }

    public UsageDto updateUsage(Integer id, UpdateUsageRequest request) {
        var usage = usageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, ChemicalUsage.class.getSimpleName()));

        var product = productRepository.findById(request.getProductId())
                .orElseThrow(ProductNotFoundExceptionException::new);

        var user = userRepository.findById(request.getTakenBy())
                .orElseThrow(UserNotFoundException::new);

        if(request.getStatus() != Status.REQUESTED && request.getHandledBy() != null) {
            System.out.println(request.getHandledBy());
            var handledBy = userRepository.findById(request.getHandledBy())
                    .orElseThrow(() -> new EntityNotFoundException(id, ChemicalUsage.class.getSimpleName()));
            usage.setHandledBy(handledBy);
        }

        Float oldAmount = usage.getAmount();
        Float newAmount = request.getAmount();
        Float delta = newAmount - oldAmount;

        float newStockLevel = product.getCurrentStock() - delta;

        validateAndHandleStock(product, newStockLevel);

        usageMapper.update(request, usage);
        usage.setProduct(product);
        usage.setUser(user);

        usageRepository.save(usage);

        product.setCurrentStock(newStockLevel);
        productRepository.save(product);

        return usageMapper.toDto(usage);
    }

    public void deleteUsage(Integer id) {
        var usage = usageRepository.findById(id).orElseThrow(ChemicalUsageNotFoundException::new);

        usageRepository.deleteById(id);
    }

    private void validateAndHandleStock(ChemicalProduct product, float stockAfterUsage) {
        if (stockAfterUsage < 0) {
            throw new IllegalArgumentException("Not enough stock available for this usage request/update");
        }

        if (stockAfterUsage < product.getMinimumStock()) {
            boolean alertExists = stockAlertRepository.existsByItemIdAndResolvedFalse(product.getId());

            if (alertExists) {
                throw new IllegalArgumentException("There's an unhandled low stock alert, you can't create or update usages");
            } else {
                StockAlert stockAlert = new StockAlert();
                stockAlert.setItemId(product.getId());
                stockAlertRepository.save(stockAlert);
            }
        }
    }

}

