package uz.ecma.apppolymergasserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import uz.ecma.apppolymergasserver.entity.ProductType;
import uz.ecma.apppolymergasserver.exception.ResourceNotFoundException;
import uz.ecma.apppolymergasserver.payload.ApiResponse;
import uz.ecma.apppolymergasserver.payload.ApiResponseModel;
import uz.ecma.apppolymergasserver.payload.ReqProductType;
import uz.ecma.apppolymergasserver.payload.ResProductType;
import uz.ecma.apppolymergasserver.repository.ProductTypeRepository;
import uz.ecma.apppolymergasserver.utils.CommonUtils;

import java.util.stream.Collectors;

@Service
public class ProductTypeService {

    @Autowired
    ProductTypeRepository productTypeRepository;

    public ResProductType getResProductType(ProductType productType) {
        return new ResProductType(productType.getId(), productType.getNameUz(), productType.getNameRu(), productType.getNameEn());
    }

    public ApiResponseModel getProductType(Integer id) {
        ProductType productType = productTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ProductType type not found!", "id", id));
        return new ApiResponseModel(true, "Mahsulot tipi", getResProductType(productType));
    }

    public ApiResponseModel getProductTypes(int page, int size, String searchName) {
        Page<ProductType> pageable = productTypeRepository.findAllByNameUzContainsIgnoreCaseOrNameRuContainsIgnoreCaseOrNameEnContainsIgnoreCase(searchName, searchName, searchName, CommonUtils.getPageableWithoutSort(page, size));
        PageImpl<ResProductType> productTypes = new PageImpl<>(
                pageable.getContent().stream().map(this::getResProductType).collect(Collectors.toList()),
                pageable.getPageable(),
                pageable.getTotalElements()
        );
        return new ApiResponseModel(true, "Mahsulot tiplari", productTypes);
    }

    public ApiResponse deleteProductType(Integer id) {
        try {
            productTypeRepository.deleteById(id);
            return new ApiResponse("Muvoffaqiyatli o'chirildi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }

    public ApiResponse saveProductType(ReqProductType reqProductType) {
        try {
            ProductType productType = new ProductType();
            if (reqProductType.getProductTypeId() != null) {
                productType = productTypeRepository.findById(reqProductType.getProductTypeId()).orElseThrow(() -> new ResourceNotFoundException("Product type not found!", "id", (reqProductType.getProductTypeId())));
            }
            productType.setNameEn(reqProductType.getNameEn());
            productType.setNameUz(reqProductType.getNameUz());
            productType.setNameRu(reqProductType.getNameRu());
            productTypeRepository.save(productType);
            return new ApiResponse("Muvoffaqiyatli saqlandi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }

}
