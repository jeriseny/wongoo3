package org.wongoo.wongoo3.domain.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.wongoo.wongoo3.domain.board.QBoard;
import org.wongoo.wongoo3.domain.post.Post;
import org.wongoo.wongoo3.domain.post.QPost;
import org.wongoo.wongoo3.domain.post.dto.PostSearchRequest;
import org.wongoo.wongoo3.domain.post.dto.SortType;
import org.wongoo.wongoo3.domain.user.QUser;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> search(PostSearchRequest request, Pageable pageable) {
        QPost post = QPost.post;
        QUser author = QUser.user;
        QBoard board = QBoard.board;

        BooleanBuilder builder = new BooleanBuilder();

        // 게시판 필터
        if (StringUtils.hasText(request.boardSlug())) {
            builder.and(post.board.slug.eq(request.boardSlug()));
        }

        // 검색 조건
        if (request.searchType() != null && StringUtils.hasText(request.keyword())) {
            String keyword = request.keyword();
            switch (request.searchType()) {
                case TITLE -> builder.and(post.title.containsIgnoreCase(keyword));
                case CONTENT -> builder.and(post.content.containsIgnoreCase(keyword));
                case TITLE_CONTENT -> builder.and(
                        post.title.containsIgnoreCase(keyword)
                                .or(post.content.containsIgnoreCase(keyword))
                );
                case AUTHOR -> builder.and(post.author.nickname.containsIgnoreCase(keyword));
            }
        }

        // 정렬 조건
        OrderSpecifier<?> orderSpecifier = switch (request.sortBy()) {
            case VIEW_COUNT -> post.viewCount.desc();
            default -> post.createdAt.desc();
        };

        // 쿼리 실행
        List<Post> content = queryFactory
                .selectFrom(post)
                .leftJoin(post.author, author).fetchJoin()
                .leftJoin(post.board, board).fetchJoin()
                .where(builder)
                .orderBy(orderSpecifier, post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 쿼리
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }
}
