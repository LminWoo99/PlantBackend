package Plant.PlantProject.repository.tradeboard.querydsl;

import Plant.PlantProject.domain.Entity.TradeBoard;
import Plant.PlantProject.dto.response.TradeBoardResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static Plant.PlantProject.common.util.SearchParam.KEYWORD;
import static Plant.PlantProject.common.util.SearchParam.TITLE;
import static Plant.PlantProject.domain.Entity.QImage.image;
import static Plant.PlantProject.domain.Entity.QMember.member;
import static Plant.PlantProject.domain.Entity.QTradeBoard.tradeBoard;
import static com.querydsl.core.types.Projections.list;

@RequiredArgsConstructor
public class TradeBoardRepositoryImpl implements CustomTradeBoardRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<TradeBoardResponseDto> search(Map<String, String> searchCondition, Pageable pageable) {

        List<TradeBoardResponseDto> content = jpaQueryFactory.select(Projections.constructor(TradeBoardResponseDto.class,
                        tradeBoard.id,
                        tradeBoard.title,
                        tradeBoard.content,
                        tradeBoard.createBy,
                        member.id.as("memberId"),
                        tradeBoard.view,
                        tradeBoard.status,
                        tradeBoard.createdAt,
                        tradeBoard.updatedAt,
                        tradeBoard.price,
                        tradeBoard.goodCount,
                        tradeBoard.buyer,
                        tradeBoard.keywordContent,
                        list(image.url)).as("imageUrls"))
                .from(tradeBoard)
                .leftJoin(tradeBoard.member, member)
                .leftJoin(tradeBoard.imageList, image)
                .where(allCond(searchCondition))
                .orderBy(tradeBoard.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<TradeBoard> countQuery = jpaQueryFactory.select(tradeBoard)
                .from(tradeBoard)
                .leftJoin(tradeBoard.member, member).fetchJoin()
                .leftJoin(tradeBoard.imageList, image).fetchJoin()
                .where(allCond(searchCondition));

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());

    }

    @Override
    public TradeBoard getTradeBoardById(Long id) {
        return jpaQueryFactory.select(tradeBoard)
                .from(tradeBoard)
                .leftJoin(tradeBoard.member, member).fetchJoin()
                .leftJoin(tradeBoard.imageList, image).fetchJoin()
                .where(tradeBoard.id.eq(id))
                .fetchOne();
    }

    // BooleanBuilder 검색 동적 쿼리
    private BooleanBuilder allCond(Map<String, String> searchCondition) {
        BooleanBuilder builder = new BooleanBuilder();

        return builder
                .and(titleLike(searchCondition.getOrDefault(TITLE.getParamKey(), null)))
                .and(keywordEq(searchCondition.getOrDefault(KEYWORD.getParamKey(), null)));
    }
    // 검색 동적 쿼리 조건1
    private BooleanExpression titleLike(String title) {
        return StringUtils.hasText(title) ? tradeBoard.title.contains(title) : null;
    }
    // 검색 동적 쿼리 조건2
    private BooleanExpression keywordEq(String keywordContent) {
        return StringUtils.hasText(keywordContent) ? tradeBoard.keywordContent.eq(keywordContent) : null;
    }


}
