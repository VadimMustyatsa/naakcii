package naakcii.by.api.product.repository;

        import naakcii.by.api.product.repository.model.Product;
        import org.springframework.data.jpa.repository.Modifying;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.data.repository.query.Param;

        import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Modifying
    @Query("update Product product set product.isActive = false where product.id = :productId")
    void softDelete(@Param("productId") Long productId);

    List<Product> getBySubcategoryId(Long subcategoryId);
}
