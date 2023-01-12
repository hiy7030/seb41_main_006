package com.mainproject.server.domain.board.service;

import com.mainproject.server.domain.board.entity.Board;
import com.mainproject.server.domain.board.repository.BoardRepository;
import com.mainproject.server.domain.member.entity.Member;
import com.mainproject.server.domain.member.service.MemberService;
import com.mainproject.server.exception.BusinessLogicException;
import com.mainproject.server.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;

    public Board createBoard(Board board) {
        board.setCountLike(0);
        board.setBoardStatus(Board.BoardStatus.BOARD_OPEN);

       //return saveBoard;
        return boardRepository.save(board);
    }

    public Board updateBoard(Board board) {
       Board findBoard = findVerifiedBoard(board.getBoardId());

       Optional.ofNullable(board.getTitle())
               .ifPresent(findBoard::setTitle);

       Optional.ofNullable(board.getContent())
               .ifPresent(findBoard::setContent);

       Optional.ofNullable(board.getAppointTime())
               .ifPresent(findBoard::setAppointTime);

       Optional.ofNullable(board.getMeetingPlace())
               .ifPresent(findBoard::setMeetingPlace);

       Optional.ofNullable(board.getBoardStatus())
               .ifPresent(findBoard::setBoardStatus);

       return boardRepository.save(findBoard);
    }

    @Transactional(readOnly = true)
    public Board findBoard(Long boardId) {
        return findVerifiedBoard(boardId);
    }

    @Transactional(readOnly = true)
    public Page<Board> findBoards(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("boardId").descending());

        return boardRepository.findAll(pageable);
    }

    public Page<Board> searchBoard(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("boardId").descending());

        return boardRepository.findByMeetingPlaceContaining(pageable, keyword);
    }

    public void deleteBoard(Long boardId) {
        Board findBoard = findVerifiedBoard(boardId);
        boardRepository.delete(findBoard);
    }

    public Board findVerifiedBoard(Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board findBoard = optionalBoard.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND)
        );
        return findBoard;
    }
}
