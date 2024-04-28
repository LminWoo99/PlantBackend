package Plant.PlantProject.service;

import Plant.PlantProject.Entity.Comment;
import Plant.PlantProject.Entity.DeleteStatus;
import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.vo.CommentCreateRequestDto;
import Plant.PlantProject.dto.CommentDto;
import Plant.PlantProject.exception.ErrorCode;
import Plant.PlantProject.repository.CommentRepository;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.repository.TradeBoardRepository;
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

    public List<CommentDto> findCommentsByTradeBoardId(Long tradeBoardId) {
        return convertNestedStructure(commentRepository.findCommentByTradeBoardId(tradeBoardId));
    }
    public Page<CommentDto> pageList(TradeBoard tradeBoardId, Pageable pageable) {
        Page<Comment> comments;

        comments = commentRepository.findCommentByTradeBoardId(tradeBoardId, pageable);
        return comments.map(comment -> CommentDto.convertCommentToDto(comment));
    }

    public CommentDto createComment(CommentCreateRequestDto requestDto) {
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
        return CommentDto.convertCommentToDto(comment);
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


    private List<CommentDto> convertNestedStructure(List<Comment> comments) {
        List<CommentDto> result = new ArrayList<>();
        Map<Long, CommentDto> map = new HashMap<>();
        comments.stream().forEach(c -> {
            CommentDto dto = CommentDto.convertCommentToDto(c);
            map.put(dto.getId(), dto);
            if(c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(dto);
            else result.add(dto);
        });
        return result;
    }

    public CommentDto updateComment(CommentDto commentCreateRequestDto) {
        Comment comment = commentRepository.findById(commentCreateRequestDto.getId()).orElseThrow(EntityNotFoundException::new);
        comment.setContent(commentCreateRequestDto.getContent());
        commentRepository.updateComment(comment);
        return CommentDto.convertCommentToDto(comment);
    }
}