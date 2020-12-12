package uz.ecma.apppolymergasserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import uz.ecma.apppolymergasserver.entity.Category;
import uz.ecma.apppolymergasserver.exception.ResourceNotFoundException;
import uz.ecma.apppolymergasserver.payload.ApiResponse;
import uz.ecma.apppolymergasserver.payload.ApiResponseModel;
import uz.ecma.apppolymergasserver.payload.ReqCategory;
import uz.ecma.apppolymergasserver.payload.ResCategory;
import uz.ecma.apppolymergasserver.repository.CategoryRepository;
import uz.ecma.apppolymergasserver.utils.CommonUtils;

import java.util.stream.Collectors;

@Service
public class CategoryService {


    @Autowired
    CategoryRepository categoryRepository;

    public ResCategory getResCategory(Category category) {
        return new ResCategory(category.getId(), category.getNameUz(), category.getNameRu(), category.getNameEn());
    }

    public ApiResponseModel getCategory(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category type not found!", "id", id));
        return new ApiResponseModel(true, "Kategoriya", getResCategory(category));
    }

    public ApiResponseModel getCategories(int page, int size, String searchName) {
        Page<Category> pageable = categoryRepository.findAllByNameUzContainsIgnoreCaseOrNameRuContainsIgnoreCaseOrNameEnContainsIgnoreCase(searchName, searchName, searchName, CommonUtils.getPageableWithoutSort(page, size));
        PageImpl<ResCategory> categories = new PageImpl<>(
                pageable.getContent().stream().map(this::getResCategory).collect(Collectors.toList()),
                pageable.getPageable(),
                pageable.getTotalElements()
        );
        return new ApiResponseModel(true, "Kategoriyalar", categories);
    }

    public ApiResponse deleteCategory(Integer id) {
        try {
            categoryRepository.deleteById(id);
            return new ApiResponse("Muvoffaqiyatli o'chirildi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }

    public ApiResponse saveCategory(ReqCategory reqCategory) {
        try {
            Category category = new Category();
            if (reqCategory.getCategoryId() != null) {
                category = categoryRepository.findById(reqCategory.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found!", "id", (reqCategory.getCategoryId())));
            }
            category.setNameEn(reqCategory.getNameEn());
            category.setNameUz(reqCategory.getNameUz());
            category.setNameRu(reqCategory.getNameRu());
            categoryRepository.save(category);
            return new ApiResponse("Muvoffaqiyatli saqlandi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }


}
