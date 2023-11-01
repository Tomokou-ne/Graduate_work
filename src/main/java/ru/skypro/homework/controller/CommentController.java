package ru.skypro.homework.controller;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Комментарии", description = "Методы работы с комментариями.")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final AdService adService;

    @GetMapping("/{id}/comments")
    @Operation(
            operationId = "getComments",
            summary = "getComments",
            tags = {"Комментарии"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = Ads.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    public ResponseEntity<Comments> getComments(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(commentService.getComments(id));
    }

    @PostMapping("/{id}/comments")
    @Operation(
            operationId = "addComments",
            summary = "addComments",
            tags = {"Комментарии"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = Comment.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    public ResponseEntity<Comment> addComments(@PathVariable("id") Integer id,
                                               @NotNull @RequestBody CreateOrUpdateComment comment) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment newComment = commentService.add(id, comment);
        return ResponseEntity.ok().body(newComment);
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    @Operation(
            operationId = "deleteComments",
            summary = "deleteComments",
            tags = {"Комментарии"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    public ResponseEntity<HttpStatus> deleteComments(@PathVariable Integer adId,
                                                     @PathVariable Integer commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    @Operation(
            operationId = "updateComments",
            summary = "updateComments",
            tags = {"Комментарии"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "*/*", schema = @Schema(implementation = Comment.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    public ResponseEntity<Comment> updateComments(@PathVariable Long adId,
                                                  @PathVariable Integer commentId,
                                                  @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        return ResponseEntity.ok(
                commentService.update(commentId, createOrUpdateComment)
        );
    }
}