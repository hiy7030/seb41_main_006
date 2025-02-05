package com.mainproject.server.domain.board.service;

import com.mainproject.server.auth.userdetails.MemberDetails;
import com.mainproject.server.domain.LikeStatus;
import com.mainproject.server.domain.board.dto.BoardDto;
import com.mainproject.server.domain.board.entity.Board;
import com.mainproject.server.domain.board.mapper.BoardMapper;
import com.mainproject.server.domain.board.repository.BoardLikeRepository;
import com.mainproject.server.domain.board.repository.BoardRepository;
import com.mainproject.server.domain.comments.entity.Comments;
import com.mainproject.server.domain.comments.service.CommentsService;
import com.mainproject.server.domain.member.entity.Member;
import com.mainproject.server.domain.member.service.MemberService;
import com.mainproject.server.domain.pet.entity.Pet;
import com.mainproject.server.domain.pet.service.PetService;
import com.mainproject.server.exception.BusinessLogicException;
import com.mainproject.server.exception.ExceptionCode;
import com.mainproject.server.utils.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final PetService petService;
    private final CustomBeanUtils customBeanUtils;
    private final EntityManager entityManager;


    public Board createBoard(Board board, MemberDetails memberDetails) {

        Member member = memberService.validateVerifyMember(memberDetails.getMemberId());
        Pet pet = petService.findPet(board.getPet().getPetId());

        board.setMember(member);
        board.setPet(pet);

        return boardRepository.save(board);
    }

    public Board updateBoard(long boardId, BoardDto.Patch patch, MemberDetails memberDetails) {

       Board findBoard = findVerifiedBoard(boardId);

       validateBoardWriter(findBoard, memberDetails.getMemberId());

       Board updateBoard = (Board) customBeanUtils.copyNonNullProperties(patch, findBoard);

       return boardRepository.save(updateBoard);
    }
    public Board findBoard(long boardId) {
        Board findBoard = findVerifiedBoard(boardId);
        return findBoard;
    }

    @Transactional(readOnly = true)
    public Page<Board> findBoards(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("boardId").descending());

        return boardRepository.findAll(pageable);
    }

    public Page<Board> searchBoard(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("boardId").descending());

        return boardRepository.findByPlaceCodeContaining(pageable, keyword);
    }

    public Page<Board> findMyBoards(int page, int size, MemberDetails memberDetails) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("boardId").descending());
        Member member = memberService.validateVerifyMember(memberDetails.getMemberId());

        Page<Board> boardPage = boardRepository.findByMember(pageable, member);

        return boardPage;
    }

    @Transactional
    public void deleteBoard(Long boardId, MemberDetails memberDetails) {
        Board findBoard = findVerifiedBoard(boardId);

        validateBoardWriter(findBoard, memberDetails.getMemberId());

        entityManager.createQuery("delete from BoardLike bl where bl.board.boardId = :boardId")
            .setParameter("boardId", boardId)
            .executeUpdate();

        boardRepository.delete(findBoard);
    }

    public Board findVerifiedBoard(Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board findBoard = optionalBoard.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND)
        );
        return findBoard;
    }

    public void validateBoardWriter(Board board, long memberId) {

        Member member = memberService.validateVerifyMember(memberId);

        if(board.getMember() != member) {
            throw new BusinessLogicException(ExceptionCode.NOT_AUTHORIZED);
        }
    }
}
