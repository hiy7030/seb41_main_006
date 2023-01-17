import { useState } from 'react';
import useOpenUserInfoModal from '../hooks/useOpenUserInfoModal';

import styled from 'styled-components';
import Container from '../components/Container';
import AddComment from '../components/matePost/AddComment';
import CommentList from '../components/matePost/CommentList';
import DeleteModal from '../components/DeleteModal';
import UserInfoCard from '../components/myPage/UsetInfoCard';
import PostMeetInfo from '../components/matePost/PostMeetInfo';
import { OpenBtn, CloseBtn } from '../components/Button';
import { FaHeart, FaRegHeart } from 'react-icons/fa';

import { convertCreatedAt } from '../utils/dateConvert';
import { dummyUserInfo } from '../api/dummyData/dummyUserInfo';
import { dummyComments } from '../api/dummyData/dummyData';

const ContainerBox = styled(Container)`
  padding-top: 20px;

  .comment {
    background-color: yellow;
    height: 100px;
  }
`;

const HeaderContainer = styled.div`
  display: flex;
  flex-direction: column;
  border-bottom: 1px solid #a79689;

  .post-title {
    display: flex;
    height: 40px;
    align-items: center;

    .title {
      margin-right: 30px;
      font-size: 26px;
      font-weight: bold;
      color: #401809;
      text-align: center;
    }
  }

  .post-info {
    display: flex;
    /* margin: 0 10px; */
    align-items: center;
    height: 24px;
    margin-bottom: 8px;

    .post-createAt {
      margin-right: 16px;
      font-size: 14px;
      color: #a79689;
    }

    .post-like {
      font-size: 14px;
      color: #ca7c62;

      span {
        padding-left: 3px;
      }
    }

    .post-btn {
      margin-left: auto;
    }

    .post-edit,
    .post-del,
    .post-like-btn {
      margin-left: 10px;
      color: #401809;
      border: none;
      background-color: transparent;
      font-size: 16px;
    }

    .post-like-btn {
      padding-right: 16px;
      color: #ca7c62;
    }
  }
`;

const MainContainer = styled.div`
  display: flex;
  margin: 20px 10px 0 10px;

  .post-content {
    /* width: 720px; */
    width: 100%;
    height: 300px;
  }

  .comment-cnt {
    color: #401809;
    margin-bottom: 6px;
  }

  .right-box {
    width: 100%;
    margin-left: 80px;
    padding: 2.5rem 1.5rem;
    border-radius: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;

    height: 80rem;

    background-color: white;
    box-shadow: 0 4px 4px rgba(0, 0, 0, 0.1);
  }

  .user-info {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .post-meet-info {
    position: sticky;
    top: calc(var(--header-height) + 1.25rem);
    width: 100%;
    margin-top: 2rem;
    padding-top: 2rem;
    border-top: 1px solid var(--main-font-color);
  }
`;

const PostDetailPage = () => {
  const [modal, setModal] = useState(false);
  const handleClickUser = useOpenUserInfoModal(1);

  const handelDelClick = () => {
    // modalView(true, '정말 삭제하시겠습니까?');
    setModal(true);
  };

  return (
    <ContainerBox>
      <HeaderContainer>
        <div className="post-title">
          <div className="title">같이 산책해요~!!</div>
          <OpenBtn>모집중</OpenBtn>
          <CloseBtn>모집마감</CloseBtn>
        </div>
        <div className="post-info">
          <div className="post-createAt">{convertCreatedAt(new Date())}</div>
          <div className="post-like">
            <FaHeart />
            <span>3</span>
          </div>
          <div className="post-btn">
            <button className="post-edit">수정</button>
            <button className="post-del" onClick={handelDelClick}>
              삭제
            </button>
            <button className="post-like-btn">
              <FaRegHeart />
            </button>
          </div>
        </div>
      </HeaderContainer>
      <MainContainer>
        <div className="left-box">
          <div className="post-content">
            안녕하세요~ 말티즈 둥이 키우는 집사입니다. 귀엽고 깜찍하고
            사랑스럽고 예쁜 아이입니다. 같이 산책하면서 재밌는 시간 보내요! 생각
            있으신 분은 댓글 달아주세요~!
          </div>
          <div className="comment-cnt">
            <h3>댓글 {dummyComments.length}개</h3>
          </div>
          <AddComment />
          <CommentList />
          {modal ? <DeleteModal /> : null}
        </div>
        <div className="right-box">
          <button className="user-info" onClick={handleClickUser}>
            <UserInfoCard userInfo={dummyUserInfo[0]} />
          </button>
          <div className="post-meet-info">
            <PostMeetInfo />
          </div>
        </div>
      </MainContainer>
    </ContainerBox>
  );
};

export default PostDetailPage;
