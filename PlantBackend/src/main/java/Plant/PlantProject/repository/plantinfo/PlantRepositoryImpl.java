package Plant.PlantProject.repository.plantinfo;


import Plant.PlantProject.domain.Entity.Plant;
import Plant.PlantProject.vo.response.PlantResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static Plant.PlantProject.common.util.SearchParam.CATEGORY;
import static Plant.PlantProject.common.util.SearchParam.MANAGE;
import static Plant.PlantProject.domain.Entity.QPlant.plant;
import static com.querydsl.core.types.Projections.list;

@RequiredArgsConstructor
public class PlantRepositoryImpl implements CustomPlantRepository{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Plant> search(final Map<String, String> searchCondition) {
        return jpaQueryFactory
                .selectFrom(plant)
                .where(allCond(searchCondition))
                .fetch();
    }

    // BooleanBuilder 검색 동적 쿼리
    private BooleanBuilder allCond(Map<String, String> searchCondition) {
        BooleanBuilder builder = new BooleanBuilder();

        return builder
                .and(snsCategoryLike(searchCondition.getOrDefault(CATEGORY.getParamKey(), null)))
                .and(manageEq(searchCondition.getOrDefault(MANAGE.getParamKey(), null)));
    }
    // 검색 동적 쿼리 조건1
    private BooleanExpression snsCategoryLike(String category) {
        return StringUtils.hasText(category) ? plant.category.contains(category) : null;
    }
    // 검색 동적 쿼리 조건2
    private BooleanExpression manageEq(String manage) {
        return StringUtils.hasText(manage) ? plant.manage.eq(manage) : null;
    }


}
