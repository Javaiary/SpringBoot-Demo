package com.example.demo.repository.search;

import com.example.demo.entity.Board;
import com.example.demo.entity.QBoard;
import com.example.demo.entity.QMember;
import com.example.demo.entity.QReply;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    //    @Override
//    public Board search1() {
//        log.info("search1................");
//
//        QBoard board = QBoard.board;
//        JPQLQuery<Board> jpqlQuery = from(board);
//
//        jpqlQuery.select(board).where(board.bno.eq(1L));
//
//        log.info("----------------------------");
//        log.info(jpqlQuery);
//        log.info("----------------------------");
//
//        List<Board> result = jpqlQuery.fetch();
//
//        return null;
//    }
   /* @Override
    public Board search1() {
        log.info("search1......................................");
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.select(board).where(board.bno.eq(1L));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));



        return null;
    }*/

    @Override
    public Board search1(){
        log.info("search1......................................");
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        tuple.groupBy(board, member);

        log.info("------------------------");
        log.info(jpqlQuery);
        log.info("------------------------");

        List<Tuple> result = tuple.fetch();

        log.info(result);

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("searchPage..............................");
        return null;
    }


}
