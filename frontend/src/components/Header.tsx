import { Link } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';

export default function Header() {
  const { isAuthenticated, user, logout } = useAuthStore();

  return (
    <header className="bg-white shadow-sm border-b border-gray-200">
      <div className="max-w-5xl mx-auto px-4 py-4">
        <div className="flex items-center justify-between">
          <Link to="/" className="text-2xl font-bold text-blue-600">
            Wongoo V2
          </Link>

          <nav className="flex items-center gap-4">
            <Link
              to="/board"
              className="px-4 py-2 text-gray-600 hover:text-gray-900 transition font-medium"
            >
              게시판
            </Link>
            {isAuthenticated ? (
              <>
                <span className="text-gray-600">
                  안녕하세요, <strong>{user?.nickName}</strong>님
                </span>
                <Link
                  to="/post/write"
                  className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
                >
                  글쓰기
                </Link>
                <Link
                  to="/mypage"
                  className="px-4 py-2 text-gray-600 hover:text-gray-900 transition"
                >
                  마이페이지
                </Link>
                <button
                  onClick={logout}
                  className="px-4 py-2 text-gray-600 hover:text-gray-900 transition"
                >
                  로그아웃
                </button>
              </>
            ) : (
              <>
                <Link
                  to="/login"
                  className="px-4 py-2 text-gray-600 hover:text-gray-900 transition"
                >
                  로그인
                </Link>
                <Link
                  to="/signup"
                  className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
                >
                  회원가입
                </Link>
              </>
            )}
          </nav>
        </div>
      </div>
    </header>
  );
}
