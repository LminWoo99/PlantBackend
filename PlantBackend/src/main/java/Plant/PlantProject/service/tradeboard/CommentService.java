package Plant.PlantProject.service.tradeboard;

import Plant.PlantProject.domain.Entity.Comment;
import Plant.PlantProject.domain.Entity.DeleteStatus;
import Plant.PlantProject.domain.Entity.TradeBoard;
import Plant.PlantProject.vo.request.CommentCreateRequestDto;
import Plant.PlantProject.vo.response.CommentResponseDto;
import Plant.PlantProject.common.exception.ErrorCode;
import Plant.PlantProject.repository.comment.CommentRepository;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.repository.tradeboard.TradeBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final TradeBoardRepository tradeBoardRepository;

    public List<CommentResponseDto> findCommentsByTradeBoardId(Long tradeBoardId) {
        return convertNestedStructure(commentRepository.findCommentByTradeBoardId(tradeBoardId));
    }
    public Page<CommentResponseDto> pageList(Long tradeBoardId, Pageable pageable) {
        TradeBoard tradeBoard = tradeBoardRepository.findById(tradeBoardId).orElseThrow(ErrorCode::throwTradeBoardNotFound);

        Page<Comment> comments = commentRepository.findCommentByTradeBoardId(tradeBoard, pageable);

        return comments.map(comment -> CommentResponseDto.convertCommentToDto(comment));
    }

    public CommentResponseDto createComment(CommentCreateRequestDto requestDto) {
        System.out.println("requestDto.getTradeBoardId() = " + requestDto.getTradeBoardId());
        Comment comment = commentRepository.save(
                Comment.createComment(
                        memberRepository.findById(requestDto.getMemberId()).orElseThrow(ErrorCode::throwMemberNotFound),
                        requestDto.getContent(),
                        tradeBoardRepository.findById(requestDto.getTradeBoardId()).orElseThrow(ErrorCode::throwTradeBoardNotFound)
                         ,
                        requestDto.getParentId() != null ?
                                commentRepository.findById(requestDto.getParentId()).orElseThrow(ErrorCode::throwCommentNotFound) : null
                        ,requestDto.getSecret()
                )

        );
        return CommentResponseDto.convertCommentToDto(comment);
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findCommentByIdWithParent(commentId);
        if(comment.getChildren().size() != 0) {
            comment.changeDeletedStatus(DeleteStatus.Y);
        } else {
            commentRepository.delete(getDeletableAncestorComment(comment));
        }
    }

    private Comment getDeletableAncestorComment(Comment comment) {
        Comment parent = comment.getParent();
        if(parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted() == DeleteStatus.Y)
                return getDeletableAncestorComment(parent);
        return comment;
    }


    private List<CommentResponseDto> convertNestedStructure(List<Comment> comments) {
        List<CommentResponseDto> result = new ArrayList<>();
        Map<Long, CommentResponseDto> map = new HashMap<>();
        comments.stream().forEach(c -> {
            CommentResponseDto dto = CommentResponseDto.convertCommentToDto(c);
            map.put(dto.getId(), dto);
            if(c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(dto);
            else result.add(dto);
        });
        return result;
    }

    public CommentResponseDto updateComment(CommentResponseDto commentCreateRequestDto) {
        Comment comment = commentRepository.findById(commentCreateRequestDto.getId()).orElseThrow(EntityNotFoundException::new);

        commentRepository.updateComment(comment);
        return CommentResponseDto.convertCommentToDto(comment);
    }

    public void deleteCommentByTradeBoardId(TradeBoard tradeBoard) {
        commentRepository.deleteAllByTradeBoardId(tradeBoard);


    }
}