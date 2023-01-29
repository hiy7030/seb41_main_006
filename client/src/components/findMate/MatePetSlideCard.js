import styled from 'styled-components';
import { RowCenterBox } from '../FlexBoxs';
import { AiOutlineMan, AiOutlineWoman } from 'react-icons/ai';
import { HiXMark, HiCheck } from 'react-icons/hi2';
import { BrownDog } from '../common/DogSvg';
import PetProfileImage from '../common/PetProfileImage';

const PetSlideCardBox = styled.div`
  width: 100%;
  height: 250px;
  /* box-shadow: 2px 4px 4px rgba(0, 0, 0, 0.1);
  background-color: white;
  border-radius: 20px; */
  display: flex;
  flex-direction: column;
  padding: 0.25rem;
  color: var(--main-font-color);
`;

const PetImageBox = styled.div`
  width: 100%;
  height: 220px;

  > svg {
    width: 100%;
    height: 100%;
  }
`;

const PetInfoBox = styled.div`
  display: flex;
  flex-direction: column;
  /* align-items: center; */

  margin-top: 0.5rem;

  span,
  svg {
    color: var(--main-font-color);
    font-size: 0.875rem;
  }

  > .pet--name {
    color: var(--main-font-color);
    font-weight: 600;
    font-size: 1.25rem;
  }

  > .pet--info {
    display: flex;
    align-items: center;
    gap: 0.5rem;
  }
  > .pet--aboutDog {
    font-weight: 600;
    margin-top: 0.125rem;
    /* text-align: right; */
  }
`;

const MatePetSlideCard = ({ pet }) => {
  return (
    <PetSlideCardBox>
      {pet?.profileImage?.upFileUrl ? (
        <PetProfileImage
          width="100%"
          height="220px"
          src={pet?.profileImage?.upFileUrl}
          name={pet.name}
        />
      ) : (
        <PetImageBox>
          <BrownDog></BrownDog>
        </PetImageBox>
      )}
      <PetInfoBox>
        <span className="pet--name">{pet.name}</span>
        <span className="pet--breed">{pet.breed}</span>
        <div className="pet--info">
          <span>{pet.age}세</span>
          <span>/</span>
          {pet.gender === 'M' ? <AiOutlineMan /> : <AiOutlineWoman />}
          <span>/</span>
          <span>
            {pet.neutered ? (
              <RowCenterBox>
                중성화
                <HiCheck />
              </RowCenterBox>
            ) : (
              <RowCenterBox>
                중성화
                <HiXMark />
              </RowCenterBox>
            )}
          </span>
        </div>
        <span className="pet--aboutDog">{pet.aboutDog}</span>
      </PetInfoBox>
    </PetSlideCardBox>
  );
};

export default MatePetSlideCard;
