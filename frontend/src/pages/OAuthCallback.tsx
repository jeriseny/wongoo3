import { useEffect, useMemo } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import LoadingSpinner from '../components/common/LoadingSpinner';

export default function OAuthCallback() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { setTokens } = useAuthStore();

  const accessToken = searchParams.get('accessToken');
  const refreshToken = searchParams.get('refreshToken');
  const errorParam = searchParams.get('error');

  const error = useMemo(() => {
    if (errorParam) {
      return decodeURIComponent(errorParam);
    }
    if (!accessToken || !refreshToken) {
      return '토큰을 받지 못했습니다.';
    }
    return null;
  }, [errorParam, accessToken, refreshToken]);

  useEffect(() => {
    if (accessToken && refreshToken && !errorParam) {
      setTokens(accessToken, refreshToken);
      navigate('/', { replace: true });
    }
  }, [accessToken, refreshToken, errorParam, setTokens, navigate]);

  if (error) {
    return (
      <div className="min-h-[60vh] flex items-center justify-center">
        <div className="text-center">
          <div className="text-red-500 text-xl mb-4">로그인 실패</div>
          <p className="text-gray-600 mb-4">{error}</p>
          <button
            onClick={() => navigate('/login')}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
          >
            로그인 페이지로 돌아가기
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-[60vh] flex flex-col items-center justify-center">
      <LoadingSpinner inline size="md" />
      <p className="text-gray-600 mt-4">로그인 처리 중...</p>
    </div>
  );
}
