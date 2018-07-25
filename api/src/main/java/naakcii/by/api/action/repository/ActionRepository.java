package naakcii.by.api.action.repository;

import naakcii.by.api.action.repository.model.Action;
import naakcii.by.api.chain.repository.model.Chain;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

public interface ActionRepository extends CrudRepository<Action, Action.Id> {

    @Query("select ac from Action ac "
            + "left join fetch ac.chain ch "
            + "left join fetch ac.product p "
            + "where p.subcategory.id = :subcategoryId and :currentDate between ac.startDate and ac.endDate"
    )
    List<Action> findAllBySubcategoryId(
            @Param("subcategoryId") Long subcategoryId,
            @Param("currentDate") Calendar currentDate
    );

    @Query("select ac from Action ac "
            + "left join fetch ac.chain ch "
            + "left join fetch ac.product p "
            + "where p.subcategory.id in :subcategoriesIds and :currentDate between ac.startDate and ac.endDate"
    )
    List<Action> findAllBySubcategoriesIds(
            @Param("subcategoriesIds") Set<Long> subcategoriesIds,
            @Param("currentDate") Calendar currentDate
    );

    List<Action> findAllByChain(Chain chain);

    List<Action> findAllByProductId(Long id);

}
