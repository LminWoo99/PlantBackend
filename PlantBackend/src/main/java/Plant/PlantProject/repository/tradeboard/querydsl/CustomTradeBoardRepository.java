package Plant.PlantProject.repository.tradeboard.querydsl;

import Plant.PlantProject.domain.Entity.TradeBoard;
import Plant.PlantProject.dto.response.TradeBoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CustomTradeBoardRepository {
    public Page<TradeBoardResponseDto> search(Map<String, String> searchCondition, Pageable pageable);

    public TradeBoard getTradeBoardById(Long id);
}
