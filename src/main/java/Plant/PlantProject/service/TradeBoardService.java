package Plant.PlantProject.service;

import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.TradeBoardDto;
import Plant.PlantProject.repository.TradeBoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 작성자 : 이민우
 작성 일자: 02.19
 내용 : 거래 게시글 서비스 구현
 특이 사항: 없음
*/
@Service
@AllArgsConstructor
public class TradeBoardService {
    private final TradeBoardRepository tradeBoardRepository;
    @Transactional
    public Long saveTradePost(TradeBoardDto tradeBoardDto){
        return tradeBoardRepository.save(tradeBoardDto.toEntity()).getId();
    }
    @Transactional(readOnly = true)
    public Page<TradeBoardDto> pageList(Pageable pageable) {
        return tradeBoardRepository.findAll(pageable).map(tradeBoard -> new TradeBoardDto(tradeBoard.getId(), tradeBoard.getTTitle(),
                tradeBoard.getTContent(),tradeBoard.getStatus(), tradeBoard.getCreatedAt(),tradeBoard.getUpdatedAt()));
    }
    public TradeBoard findById(Long id){
        return tradeBoardRepository.findById(id).get();
    }


}
