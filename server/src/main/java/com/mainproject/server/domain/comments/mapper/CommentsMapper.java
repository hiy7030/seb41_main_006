package com.mainproject.server.domain.comments.mapper;

import java.util.List;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import com.mainproject.server.awsS3.dto.S3UpFileResponse;
import com.mainproject.server.awsS3.entity.S3UpFile;
import com.mainproject.server.domain.board.entity.Board;
import com.mainproject.server.domain.comments.dto.CommentsDto;
import com.mainproject.server.domain.comments.dto.CommentsLikeDto;
import com.mainproject.server.domain.comments.entity.Comments;
import com.mainproject.server.domain.comments.entity.CommentsLike;
import com.mainproject.server.domain.member.dto.MemberDto;
import com.mainproject.server.domain.member.entity.Member;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentsMapper {

	@Mappings({@Mapping(source = "memberId", target = "member.memberId"),
	@Mapping(source = "boardId", target = "board.boardId")})
	Comments commentsPostDtoToComments(CommentsDto.Post commentsPostDto);

	Comments commentsPatchDtoToComments(CommentsDto.Patch commentsPatchDto);

	@Mappings({@Mapping(source = "board.boardId", target = "boardId"),
	@Mapping(source = "parentComments.commentsId", target = "parentId")})
	CommentsDto.Response commentsToCommentsResponseDto(Comments comments);

	@Mapping(source = "s3UpFile.upFileId", target = "profileImage.upFileId")
	@Mapping(source = "s3UpFile.upFileName", target = "profileImage.upFileName")
	@Mapping(source = "s3UpFile.upFileUrl", target = "profileImage.upFileUrl")
	MemberDto.ResponseOnlyMemberName memberToMemberNameDto(Member member);


	default CommentsLikeDto.Response commentsLikeToCommentsLikeResponseDto(Optional<CommentsLike> commentsLike){
		Member member = commentsLike.get().getMember();
		Comments comments = commentsLike.get().getComments();

		CommentsLikeDto.Response commentsLikeResponseDto = CommentsLikeDto.Response
			.builder()
			.commentsLikeId(commentsLike.get().getCommentsLikeId())
			.memberId(member.getMemberId())
			.commentsId(comments.getCommentsId())
			.likeStatus(commentsLike.get().likeStatus)
			.build();

		return commentsLikeResponseDto;
	}

	List<CommentsDto.Response> commentsToCommentsResponseDtos(List<Comments> comments);
}
