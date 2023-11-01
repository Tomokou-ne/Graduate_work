package ru.skypro.homework.mappers;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {
    public Comment entityToCommentDto(CommentEntity entity) {
        long createdAtInMillisecondsSinceEpoch = entity.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli();
        Comment comment = new Comment();
        comment.setPk(entity.getId());
        comment.setAuthor(entity.getAuthor().getId());
        comment.setAuthorImage(entity.getAuthor().getImage());
        comment.setText(entity.getText());
        comment.setAuthorFirstName(entity.getAuthor().getFirstName());
        comment.setCreatedAt(createdAtInMillisecondsSinceEpoch);
        return comment;
    }

//    public CommentEntity createCommentToEntity(CreateOrUpdateComment createOrUpdateComment, AdEntity ad, UserEntity author) {
//        return new CommentEntity(author, LocalDateTime.now(), createOrUpdateComment.getText(), ad);
//    }

    public CommentEntity createCommentToEntity(CreateOrUpdateComment commentText, AdEntity adEntity) {
        CommentEntity newCommentEntity = new CommentEntity();
        newCommentEntity.setText(commentText.getText());
        newCommentEntity.setCreatedAt(LocalDateTime.now());
        newCommentEntity.setAuthor(adEntity.getAuthor());
        newCommentEntity.setAd(adEntity);
        return newCommentEntity;
    }

    public Comments entityToComments(List<CommentEntity> entities) {
        return Comments.builder()
                .results(entities
                        .stream().map(this::entityToCommentDto)
                        .collect(Collectors.toList()))
                .count(entities.size())
                .build();
    }

}