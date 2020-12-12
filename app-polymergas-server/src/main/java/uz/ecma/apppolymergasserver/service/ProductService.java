package uz.ecma.apppolymergasserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import uz.ecma.apppolymergasserver.entity.Attachment;
import uz.ecma.apppolymergasserver.entity.Product;
import uz.ecma.apppolymergasserver.entity.ProductPrice;
import uz.ecma.apppolymergasserver.entity.Quantity;
import uz.ecma.apppolymergasserver.exception.ResourceNotFoundException;
import uz.ecma.apppolymergasserver.payload.*;
import uz.ecma.apppolymergasserver.repository.*;
import uz.ecma.apppolymergasserver.utils.CommonUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductTypeRepository productTypeRepository;

    @Autowired
    ProductTypeService productTypeService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    QuantityRepository quantityRepository;

    @Autowired
    UserService userService;


    //Mahsulot qo'shish yoki o'zgartirish
    public ApiResponse saveProduct(ReqProduct reqProduct) {
        try {
            Product product = new Product();
            if (reqProduct.getId() != null) {
                product = productRepository.findById(reqProduct.getId()).orElseThrow(() -> new ResourceNotFoundException("Mahsulot topilmadi!", "id", reqProduct.getId()));
            }
            product.setName(reqProduct.getName());
            product.setMadeIn(reqProduct.getMadeIn());
            product.setMadeIn(reqProduct.getMadeIn());
//            product.setProductionDate(reqProduct.getProductionDate());
//            product.setExpirationDate(reqProduct.getExpirationDate());
            product.setDescriptionUz(reqProduct.getDescriptionUz());
            product.setDescriptionRu(reqProduct.getDescriptionRu());
            product.setDescriptionEn(reqProduct.getDescriptionEn());
//            product.setProvider(reqProduct.getProvider());
            if (reqProduct.getCategoryId() != null) {
                product.setCategory(categoryRepository.findById(reqProduct.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found!", "id", reqProduct.getCategoryId())));
            }
            if (reqProduct.getProductTypeId() != null) {
                product.setProductType(productTypeRepository.findById(reqProduct.getProductTypeId()).orElseThrow(() -> new ResourceNotFoundException("Product type not found!", "id", reqProduct.getProductTypeId())));
            }
            if (reqProduct.getPhotosId() != null) {
                product.setPhotos(attachmentRepository.findAllById(reqProduct.getPhotosId()));
            }
            product.setProductPrices(new ArrayList<>());
            Product save = productRepository.save(product);
            if (reqProduct.getReqProductPrices() != null) {
                save.getProductPrices().addAll(reqProduct.getReqProductPrices().stream().map(reqProductPrice -> createProductPrice(save, reqProductPrice)).collect(Collectors.toList()));
            }
            productRepository.save(save);
            return new ApiResponse("Muvoffaqiyatli qo'shildi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }

    public ProductPrice createProductPrice(Product product, ReqProductPrice reqProductPrice) {
        ProductPrice productPrice = new ProductPrice();
        productPrice.setPrice(reqProductPrice.getPrice());
        productPrice.setProducts(Collections.singletonList(product));
        productPrice.setQuantity(createQuantity(reqProductPrice.getReqQuantity()));
        productPrice.setHaveProduct(reqProductPrice.isHaveProduct());
        return productPriceRepository.save(productPrice);
    }

    public Quantity createQuantity(ReqQuantity reqQuantity) {
        Quantity quantity = new Quantity();
        quantity.setQuantityType(reqQuantity.getQuantityType());
        quantity.setValue(reqQuantity.getValue());
        return quantityRepository.save(quantity);

    }

    public ResProduct getResProduct(Product product) {
        return new ResProduct(
                product.getId(),
                product.getName(),
                product.getDescriptionUz(),
                product.getDescriptionRu(),
                product.getDescriptionEn(),
                product.getCategory() == null ? null : categoryService.getResCategory(product.getCategory()),
                product.getProductType() == null ? null : productTypeService.getResProductType(product.getProductType()),
//                product.getProvider(),
                product.getProductPrices() == null ? null : product.getProductPrices().stream().map(this::getResProductPrice).collect(Collectors.toList()),
                product.getPhotos() == null ? null : product.getPhotos().stream().map(this::getResUploadFile).collect(Collectors.toList()),
//                product.getExpirationDate() == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(product.getExpirationDate()),
//                product.getProductionDate() == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(product.getProductionDate()),
                product.getMadeIn(),
                product.getCreatedAt() == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm").format(product.getCreatedAt()),
                product.getCreatedBy() == null ? null : userService.getUser(product.getCreatedBy()).getFullName()
        );
    }

    public ResProduct getResProductHaveProduct(Product product) {
        return new ResProduct(
                product.getId(),
                product.getName(),
                product.getDescriptionUz(),
                product.getDescriptionRu(),
                product.getDescriptionEn(),
                product.getCategory() == null ? null : categoryService.getResCategory(product.getCategory()),
                product.getProductType() == null ? null : productTypeService.getResProductType(product.getProductType()),
//                product.getProvider(),
                product.getProductPrices() == null ? null : product.getProductPrices().stream().filter(ProductPrice::isHaveProduct).map(this::getResProductPrice).collect(Collectors.toList()),
                product.getPhotos() == null ? null : product.getPhotos().stream().map(this::getResUploadFile).collect(Collectors.toList()),
//                product.getExpirationDate() == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(product.getExpirationDate()),
//                product.getProductionDate() == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(product.getProductionDate()),
                product.getMadeIn(),
                product.getCreatedAt() == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm").format(product.getCreatedAt()),
                product.getCreatedBy() == null ? null : userService.getUser(product.getCreatedBy()).getFullName()
        );
    }

    public List<ResProductPrice> getResProductPrices(List<UUID> productPriceIds) {
        return productPriceRepository.findAllById(productPriceIds).stream().map(this::getResProductPrice).collect(Collectors.toList());
    }

    public List<ResProductPrice> getResProductPricesHave() {
        return productPriceRepository.findAllByHaveProductIsTrueOrderByCreatedAt().stream().map(this::getResProductPrice).collect(Collectors.toList());
    }

    public ApiResponse changeProductPriceHaveStatus(UUID id, boolean have) {
        try {
            ProductPrice productPrice = productPriceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Mahsulot turi topilmadi!", "id", id));
            productPrice.setHaveProduct(have);
            productPriceRepository.save(productPrice);
            return new ApiResponse("Muvoffaqiyatli bajarildi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }

    public List<ResProduct> getResProduct(List<UUID> productIds) {
        return productRepository.findAllById(productIds).stream().map(this::getResProduct).collect(Collectors.toList());
    }

    public ResProductPrice getResProductPrice(ProductPrice productPrice) {
        return new ResProductPrice(
                productPrice.getId(),
                productPrice.getPrice(),
                productPrice.getQuantity() == null ? null : getResQuantity(productPrice.getQuantity()),
                productPrice.isHaveProduct()
        );
    }

    public ResQuantity getResQuantity(Quantity quantity) {
        return new ResQuantity(
                quantity.getId(),
                quantity.getValue(),
                quantity.getQuantityType());
    }

    public ResUploadFile getResUploadFile(Attachment attachment) {
        return new ResUploadFile(
                attachment.getId(),
                attachment.getName(),
                "/api/file/" + attachment.getId(),
                attachment.getContentType(),
                attachment.getSize()
        );
    }

    //Mahsulotni o`chirish
    public ApiResponse deleteProduct(UUID id) {
        try {
            productRepository.deleteById(id);
            return new ApiResponse("Muvoffaqiyatli o`chirildi!", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik", false);
        }

    }

    //Mahsulotlarni listi
    public ApiResponseModel getProducts(int page, int size, String searchName, String sortType, boolean haveSort) {
        Page<Product> products = null;
        switch (sortType) {
            case "byCreatedAsc":
                products = productRepository.findAllByNameContainsIgnoreCaseOrDescriptionUzContainsIgnoreCaseOrDescriptionRuContainsIgnoreCaseOrDescriptionEnContainsIgnoreCaseOrMadeInContainsIgnoreCaseOrderByCreatedAtAsc(searchName, searchName, searchName, searchName, searchName, CommonUtils.getPageable(page, size));
                break;
            case "byCreatedDesc":
                products = productRepository.findAllByNameContainsIgnoreCaseOrDescriptionUzContainsIgnoreCaseOrDescriptionRuContainsIgnoreCaseOrDescriptionEnContainsIgnoreCaseOrMadeInContainsIgnoreCaseOrderByCreatedAtDesc(searchName, searchName, searchName, searchName, searchName, CommonUtils.getPageable(page, size));
                break;
            case "byName":
                products = productRepository.findAllByNameContainsIgnoreCaseOrDescriptionUzContainsIgnoreCaseOrDescriptionRuContainsIgnoreCaseOrDescriptionEnContainsIgnoreCaseOrMadeInContainsIgnoreCaseOrderByName(searchName, searchName, searchName, searchName, searchName, CommonUtils.getPageable(page, size));
                break;
            case "byHaveProduct":
                products = productRepository.findAllByNameContainsIgnoreCaseAndProductPrices_HaveProductIsTrueOrderByCreatedAtDesc(searchName, CommonUtils.getPageable(page, size));
                break;
            case "byNotHaveProduct":
                products = productRepository.findAllByNameContainsIgnoreCaseAndProductPrices_HaveProductIsFalseOrderByCreatedAtDesc(searchName, CommonUtils.getPageable(page, size));
                break;
            default:
                products = productRepository.findAllByNameContainsIgnoreCaseOrDescriptionUzContainsIgnoreCaseOrDescriptionRuContainsIgnoreCaseOrDescriptionEnContainsIgnoreCaseOrMadeInContainsIgnoreCase(searchName, searchName, searchName, searchName, searchName, CommonUtils.getPageable(page, size));
                break;
        }

        Page<ResProduct> pageable = null;
        if (products != null) {
            pageable = new PageImpl<>(
                    products.getContent().stream().map(this::getResProduct).collect(Collectors.toList()),
                    products.getPageable(),
                    products.getTotalElements()
            );
        }
        return new ApiResponseModel(true, "Mahsulotlar", pageable);
    }

    public ApiResponseModel getProductsForSale(){
        return new ApiResponseModel(true,"Sotuvdagi maxsulotlar",productRepository.findAllByProductPrices_HaveProductIsTrueOrderByCreatedAtDesc().stream().map(this::getResProduct).collect(Collectors.toList()));
    }

    //Popular Mahsulotlarni listi
    public ApiResponseModel getPopularProducts(int page, int size) {
        Page<Product> products = productRepository.findAllPopularProducts(CommonUtils.getPageableForNative(page, size));
        Page<ResProduct> pageable = new PageImpl<ResProduct>(
                products.getContent().stream().map(this::getResProduct).collect(Collectors.toList()),
                products.getPageable(),
                products.getTotalElements()
        );
        return new ApiResponseModel(true, "Mahsulotlar", pageable);
    }

    public ResProduct getProduct(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Mahsulot topilmadi!", "id", id));
        return getResProduct(product);
    }

    public ApiResponseModel getProductPrice(UUID id) {
        ProductPrice productPrice = productPriceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product price not found!", "id", id));
        return new ApiResponseModel(true, "Mahsulot narxi", getResProductPrice(productPrice));
    }

    public ResDashboard getProductToDashboard(ReqDashboard reqDashboard) {
        Timestamp from = Timestamp.valueOf(reqDashboard.getFrom());
        Timestamp to = Timestamp.valueOf(reqDashboard.getTo());
        Integer usersCount = productRepository.countAllByCreatedAtBetween(from, to);
        Map<Integer, Integer> userStatistic = new HashMap<>();
        if (reqDashboard.getType().equals("month")) {
            do {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(from.getTime()));
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
                userStatistic.put(calendar.get(Calendar.MONTH), productRepository.countAllByCreatedAtBetween(from, new Timestamp(calendar.getTimeInMillis())));
                from.setTime(calendar.getTimeInMillis());
            }
            while (!from.after(to));
        } else if (reqDashboard.getType().equals("hour")) {
            do {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(from.getTime()));
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                userStatistic.put(calendar.get(Calendar.DAY_OF_MONTH), productRepository.countAllByCreatedAtBetween(from, new Timestamp(calendar.getTimeInMillis())));
                from.setTime(calendar.getTimeInMillis());
            }
            while (!from.after(to));
        } else {
            do {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(from.getTime()));
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
                userStatistic.put(calendar.get(Calendar.YEAR), productRepository.countAllByCreatedAtBetween(from, new Timestamp(calendar.getTimeInMillis())));
                from.setTime(calendar.getTimeInMillis());
            }
            while (from.getYear() < to.getYear());
        }
        List<Object[]> products = productRepository.top10Product();
        return new ResDashboard(usersCount, userStatistic, products);


    }

}
