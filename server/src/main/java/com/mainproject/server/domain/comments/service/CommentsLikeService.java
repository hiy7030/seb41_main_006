package com.mainproject.server.domain.comments.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mainproject.server.domain.board.entity.BoardLike;
import com.mainproject.server.domain.comments.entity.Comments;
import com.mainproject.server.domain.comments.entity.CommentsLike;
import com.mainproject.server.domain.comments.repository.CommentsLikeRepository;
import com.mainproject.server.domain.comments.repository.CommentsRepository;
import com.mainproject.server.domain.member.entity.Member;
import com.mainproject.server.domain.member.service.MemberService;
import com.mainproject.server.domain.LikeStatus;

@Service
public class CommentsLikeService {

	private final MemberService memberService;
	private final CommentsService commentsService;
	private final CommentsLikeRepository commentsLikeRepository;

	public CommentsLikeService(MemberService memberService, CommentsService commentsService,
		 CommentsLikeRepository commentsLikeRepository) {
		this.memberService = memberService;
		this.commentsService = commentsService;
		this.commentsLikeRepository = commentsLikeRepository;
	}

	// ----- 댓글 좋아요
	@Transactional
	public Optional<CommentsLike> likeComments(Long commentsId, Long memberId) {
		Member findMember = memberService.validateVerifyMember(memberId);
		Comments findComments = commentsService.findVerifiedComments(commentsId);

		Optional<CommentsLike> oCommentsLike = commentsLikeRepository.findByMemberAndComments(findMember, findComments);
		return Optional.of(oCommentsLike.map(like -> {
			if (like.getLikeStatus().equals(LikeStatus.LIKE)) {
				like.setLikeStatus(LikeStatus.CANCEL);
			} else {
				like.setLikeStatus(LikeStatus.LIKE);
			}
			return like;
		}).orElseGet(() -> {
			CommentsLike commentsLike = new CommentsLike();
			commentsLike.setMember(findMember);
			commentsLike.setComments(findComments);
			return commentsLikeRepository.save(commentsLike);
		}));
	}

	public List<Comments> setCommentsLiked(List<Comments> comments) {
		for(Comments c : comments) {
			List<Long> likedMembers = findLikedMembers(c.getCommentsId());
			c.setlikedMembers(likedMembers);
		}
		return comments;
	}

	public List<Long> findLikedMembers(long commentId) {
		List<Long> likedMembers = commentsLikeRepository.findMemberIdsByCommentsIdAndLikeStatus(commentId, LikeStatus.LIKE);
		return likedMembers;
	}
}
