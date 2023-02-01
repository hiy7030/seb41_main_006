import defaultRequest from './defaultRequest';

const authRequest = defaultRequest.create();

authRequest.interceptors.request.use(
  function (config) {
    // 요청을 보내기 전에 헤더에 토큰값 설정
    const AUTH_TOKEN = localStorage.getItem('AccessToken');
    config.headers['Authorization'] = AUTH_TOKEN;

    return config;
  },
  function (error) {
    // 오류 요청을 보내기전 수행할 일
    // ...
    console.log(error);
    return Promise.reject(error);
  }
);

authRequest.interceptors.response.use(
  function (response) {
    return response;
  },
  async function (error) {
    // config는 어떤 요청이었는지에 대한 정보를 담고 있다.
    // const {config, response} = error
    // const { response } = error;
    // if (response?.status === 401) {
    //   const originalRequest = config

    //   401 에러인데 토큰이 만료된 에러라면 -> reissue // 요청했던 거 다시 이어가면 좋을텐디
    //   if(token이 만료된 에러){
    //     reissue
    //   }
    //   401로 요청 실패했던 요청 새로운 accessToken으로 재요청
    //   return axios(originalRequest);
    // }

    if (error?.response?.status === 401) {
      // 401 에러일 때 실행
      const refreshToken = localStorage.getItem('refreshToken');
      if (refreshToken) {
        await defaultRequest
          .post(
            `/auth/reissue`,
            {},
            {
              headers: { Refresh: refreshToken },
            }
          )
          .then((res) => {
            localStorage.setItem('AccessToken', res.headers.authorization);
          });
      }
      if (
        error.response.data.message === 'Member Not Found' ||
        error.response.data.message === '자격 증명에 실패하였습니다.'
      ) {
        alert('이메일과 비밀 번호를 정확히 입력해 주세요');
      } else {
        alert('로그인 후 이용해 주세요');
      }
    }
    return Promise.reject(error);
  }
);

export default authRequest;
