    package com.codewithmosh.store.product.usage;

    import com.codewithmosh.store.common.EntityNotFoundException;
    import com.codewithmosh.store.product.item.ChemicalProduct;
    import com.codewithmosh.store.product.item.ProductNotFoundExceptionException;
    import com.codewithmosh.store.product.item.ProductRepository;
    import com.codewithmosh.store.product.stock.StockAlert;
    import com.codewithmosh.store.product.stock.StockAlertRepository;
    import com.codewithmosh.store.session.LabSessionRepository;
    import com.codewithmosh.store.user.UserRepository;
    import lombok.AllArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    @AllArgsConstructor
    public class UsageService {
        private final UserRepository userRepository;
        private final ProductRepository productRepository;
        private final UsageRepository usageRepository;
        private final UsageMapper usageMapper;
        private final StockAlertRepository stockAlertRepository;
        private final LabSessionRepository labSessionRepository;


        public long countNumberUsages() {
            return usageRepository.count();
        }

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
            var product = productRepository.findById(request.getProductId())
                    .orElseThrow(ProductNotFoundExceptionException::new);

            var user = userRepository.findById(request.getTakenBy())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            var session = labSessionRepository.findById(request.getSessionId())
                    .orElseThrow(() -> new EntityNotFoundException("Lab session not found"));

            float newStock = product.getCurrentStock() - request.getAmount();
            validateAndHandleStock(product, newStock);

            ChemicalUsage usage = usageMapper.toEntity(request);
            usage.setProduct(product);
            usage.setTakenBy(user);
            usage.setSession(session);

            usageRepository.save(usage);

            product.setCurrentStock(newStock);
            productRepository.save(product);

            return usageMapper.toDto(usage);
        }

        public UsageDto updateUsage(Integer id, UpdateUsageRequest request) {
            var usage = usageRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Chemical Usage not found"));

            var product = productRepository.findById(request.getProductId())
                    .orElseThrow(ProductNotFoundExceptionException::new);

            var user = userRepository.findById(request.getTakenBy())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            var session = labSessionRepository.findById(request.getSessionId())
                    .orElseThrow(() -> new EntityNotFoundException("Lab session not found"));

            if (!"REQUESTED".equals(request.getStatus()) && request.getHandledBy() != null) {
                var handledBy = userRepository.findById(request.getHandledBy())
                        .orElseThrow(() -> new EntityNotFoundException("User not found"));
                usage.setHandledBy(handledBy);
            }

            Float oldAmount = usage.getAmount();
            Float newAmount = request.getAmount();
            Float delta = newAmount - oldAmount;

            float newStockLevel = product.getCurrentStock() - delta;
            validateAndHandleStock(product, newStockLevel);

            usageMapper.update(request, usage);
            usage.setProduct(product);
            usage.setTakenBy(user);
            usage.setSession(session);

            usageRepository.save(usage);

            product.setCurrentStock(newStockLevel);
            productRepository.save(product);

            return usageMapper.toDto(usage);
        }

        public void deleteUsage(Integer id) {
            usageRepository.findById(id).orElseThrow(ChemicalUsageNotFoundException::new);
            usageRepository.deleteById(id);
        }

        public void deleteBySessionId(Integer sessionId) {
            usageRepository.deleteBySessionId(sessionId);
        }

        public void createBatchUsages(List<CreateUsageRequest> requests) {
            for(var request : requests) {
                createUsage(request);
            }
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
