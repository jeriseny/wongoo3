import React, { useEffect, Suspense } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { useAuthStore } from './stores/authStore';

import Layout from './components/Layout';
import ProtectedRoute from './components/ProtectedRoute';

// 메인 페이지 - 즉시 로드
import Home from './pages/Home';
import Board from './pages/Board';
import PostDetail from './pages/PostDetail';

// 인증/마이페이지/글쓰기 - 지연 로드
const Login = React.lazy(() => import('./pages/Login'));
const Signup = React.lazy(() => import('./pages/Signup'));
const PostWrite = React.lazy(() => import('./pages/PostWrite'));
const MyPage = React.lazy(() => import('./pages/MyPage'));
const OAuthCallback = React.lazy(() => import('./pages/OAuthCallback'));

// 페이지 로딩 fallback
const PageLoader = () => (
  <div className="min-h-[50vh] flex items-center justify-center">
    <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600" />
  </div>
);

function App() {
  const { initialize, isLoading } = useAuthStore();

  useEffect(() => {
    initialize();
  }, [initialize]);

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <BrowserRouter>
      <Suspense fallback={<PageLoader />}>
        <Routes>
          <Route element={<Layout />}>
            {/* Public routes - 자주 접근하는 페이지 */}
            <Route path="/" element={<Home />} />
            <Route path="/board" element={<Board />} />
            <Route path="/board/:slug" element={<Board />} />
            <Route path="/post/:id" element={<PostDetail />} />

            {/* Public routes - 지연 로드 */}
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/oauth/callback" element={<OAuthCallback />} />

            {/* Protected routes - 지연 로드 */}
            <Route element={<ProtectedRoute />}>
              <Route path="/post/write" element={<PostWrite />} />
              <Route path="/post/edit/:id" element={<PostWrite />} />
              <Route path="/mypage" element={<MyPage />} />
            </Route>
          </Route>
        </Routes>
      </Suspense>
    </BrowserRouter>
  );
}

export default App;
