package com.codewithmosh.store.product.usage;

import com.codewithmosh.store.product.item.ProductNotFoundExceptionException;
import com.codewithmosh.store.product.item.ProductRepository;
import com.codewithmosh.store.product.item.ProductService;
import com.codewithmosh.store.user.UserNotFoundException;
import com.codewithmosh.store.user.UserRepository;
import com.codewithmosh.store.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsageService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UsageRepository usageRepository;
    private final UsageMapper usageMapper;

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
        var product = productRepository.findById(request.getProductId()).orElse(null);
        if(product == null) {
            throw new ProductNotFoundExceptionException();
        }

        var user = userRepository.findById(request.getTakenBy()).orElse(null);
        if(user == null) {
            throw new UserNotFoundException();
        }

        ChemicalUsage usage = usageMapper.toEntity(request);
        usage.setProduct(product);
        usage.setUser(user);
        usageRepository.save(usage);
        return usageMapper.toDto(usage);
    }

//    ublic UsageDto updateUsage(Integer id, UpdateUsageRequest request) {
//        var usage = usageRepository.findById(id).orElse(null);
//        if (usage == null) {
//            return null;
//        }
//
//        usageMapper.update(request, usage);
//        usageRepository.save(usage);
//        return usageMapper.toDto(usage);
//    }
//    p
    public void deleteUsage(Integer id) {
        var usage = usageRepository.findById(id).orElse(null);
        if (usage == null) {
            throw new ChemicalUsageNotFoundException();
        }

        usageRepository.deleteById(id);
    }
}
