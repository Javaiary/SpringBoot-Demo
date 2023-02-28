package com.example.demo.repository;

import com.example.demo.entity.Board;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 한개의 로우(Object) 내에 Object[]로 나옴
    @Query("select b, w from Board b left join b.writer w where b.bno =:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno")
    // reply 내의 board는 참조를 하고 있으나, board는 reply를 참조하고 있지 않으므로
    // on 을 통해 r.board = b.bno 를 명시해줌
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value ="SELECT b, w, count(r) " +
            " FROM Board b " +
            " LEFT JOIN b.writer w " +
            " LEFT JOIN Reply r ON r.board = b " +
            " GROUP BY w, b",
            countQuery ="SELECT count(*) FROM Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    @Query(value ="SELECT b, w, count(r) " +
            " FROM Board b LEFT JOIN b.writer w " +
            " LEFT OUTER JOIN Reply r ON r.board = b " +
            " Where b.bno = :bno"+
            " Group By b, w")
    Object getBoardByBno(@Param("bno") Long bno);
}
