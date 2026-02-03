import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { postApi, statsApi } from '../api/client';
import { useAuthStore } from '../stores/authStore';
import PostCard from '../components/PostCard';
import type { PostListItem, Stats } from '../types';

export default function Home() {
  const { isAuthenticated } = useAuthStore();
  const [recentPosts, setRecentPosts] = useState<PostListItem[]>([]);
  const [popularPosts, setPopularPosts] = useState<PostListItem[]>([]);
  const [stats, setStats] = useState<Stats | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [recentRes, popularRes, statsRes] = await Promise.all([
          postApi.getList({ page: 0, size: 5, sortBy: 'LATEST' }),
          postApi.getList({ page: 0, size: 3, sortBy: 'VIEW_COUNT' }),
          statsApi.get(),
        ]);
        setRecentPosts(recentRes.data.content);
        setPopularPosts(popularRes.data.content);
        setStats(statsRes.data);
      } catch (err) {
        console.error('데이터 로딩 실패:', err);
      } finally {
        setIsLoading(false);
      }
    };
    fetchData();
  }, []);

  return (
    <div className="space-y-16">
      {/* Hero Section */}
      <section className="text-center py-16 bg-gradient-to-br from-blue-50 to-indigo-100 -mx-4 px-4 rounded-3xl">
        <h1 className="text-4xl md:text-5xl font-bold text-gray-900 mb-6">
          함께 소통하는 커뮤니티
        </h1>
        <p className="text-xl text-gray-600 mb-8 max-w-2xl mx-auto">
          자유롭게 이야기를 나누고, 정보를 공유하며, 새로운 사람들과 연결되세요.
        </p>
        <div className="flex flex-col sm:flex-row gap-4 justify-center">
          {isAuthenticated ? (
            <>
              <Link
                to="/board"
                className="px-8 py-4 bg-blue-600 text-white font-semibold rounded-xl hover:bg-blue-700 transition shadow-lg hover:shadow-xl"
              >
                게시판 바로가기
              </Link>
              <Link
                to="/post/write"
                className="px-8 py-4 bg-white text-blue-600 font-semibold rounded-xl hover:bg-gray-50 transition border-2 border-blue-600"
              >
                글 작성하기
              </Link>
            </>
          ) : (
            <>
              <Link
                to="/signup"
                className="px-8 py-4 bg-blue-600 text-white font-semibold rounded-xl hover:bg-blue-700 transition shadow-lg hover:shadow-xl"
              >
                시작하기
              </Link>
              <Link
                to="/login"
                className="px-8 py-4 bg-white text-blue-600 font-semibold rounded-xl hover:bg-gray-50 transition border-2 border-blue-600"
              >
                로그인
              </Link>
            </>
          )}
        </div>
      </section>

      {/* Features Section */}
      <section>
        <h2 className="text-2xl font-bold text-gray-900 text-center mb-10">
          서비스 특징
        </h2>
        <div className="grid md:grid-cols-3 gap-8">
          <div className="bg-white p-8 rounded-2xl shadow-sm border border-gray-100 text-center hover:shadow-md transition">
            <div className="w-16 h-16 bg-blue-100 rounded-2xl flex items-center justify-center mx-auto mb-6">
              <svg className="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 8h2a2 2 0 012 2v6a2 2 0 01-2 2h-2v4l-4-4H9a1.994 1.994 0 01-1.414-.586m0 0L11 14h4a2 2 0 002-2V6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2v4l.586-.586z" />
              </svg>
            </div>
            <h3 className="text-lg font-semibold text-gray-900 mb-3">자유로운 소통</h3>
            <p className="text-gray-600">
              다양한 주제로 자유롭게 이야기를 나누고 의견을 공유하세요.
            </p>
          </div>

          <div className="bg-white p-8 rounded-2xl shadow-sm border border-gray-100 text-center hover:shadow-md transition">
            <div className="w-16 h-16 bg-green-100 rounded-2xl flex items-center justify-center mx-auto mb-6">
              <svg className="w-8 h-8 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
              </svg>
            </div>
            <h3 className="text-lg font-semibold text-gray-900 mb-3">정보 공유</h3>
            <p className="text-gray-600">
              유용한 정보와 지식을 공유하고 함께 성장하세요.
            </p>
          </div>

          <div className="bg-white p-8 rounded-2xl shadow-sm border border-gray-100 text-center hover:shadow-md transition">
            <div className="w-16 h-16 bg-purple-100 rounded-2xl flex items-center justify-center mx-auto mb-6">
              <svg className="w-8 h-8 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
            </div>
            <h3 className="text-lg font-semibold text-gray-900 mb-3">커뮤니티</h3>
            <p className="text-gray-600">
              같은 관심사를 가진 사람들과 연결되어 네트워크를 넓혀보세요.
            </p>
          </div>
        </div>
      </section>

      {/* Quick Actions */}
      <section className="bg-gradient-to-r from-gray-50 to-gray-100 -mx-4 px-4 py-12 rounded-3xl">
        <h2 className="text-2xl font-bold text-gray-900 text-center mb-8">
          바로 시작하기
        </h2>
        <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-4 max-w-4xl mx-auto">
          <Link
            to="/board"
            className="flex items-center gap-3 p-4 bg-white rounded-xl border border-gray-200 hover:shadow-md transition"
          >
            <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
              <svg className="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z" />
              </svg>
            </div>
            <span className="font-medium text-gray-900">전체 게시글</span>
          </Link>

          {isAuthenticated ? (
            <>
              <Link
                to="/post/write"
                className="flex items-center gap-3 p-4 bg-white rounded-xl border border-gray-200 hover:shadow-md transition"
              >
                <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
                  <svg className="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                  </svg>
                </div>
                <span className="font-medium text-gray-900">글 작성</span>
              </Link>
              <Link
                to="/mypage"
                className="flex items-center gap-3 p-4 bg-white rounded-xl border border-gray-200 hover:shadow-md transition"
              >
                <div className="w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center">
                  <svg className="w-5 h-5 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                  </svg>
                </div>
                <span className="font-medium text-gray-900">마이페이지</span>
              </Link>
            </>
          ) : (
            <>
              <Link
                to="/signup"
                className="flex items-center gap-3 p-4 bg-white rounded-xl border border-gray-200 hover:shadow-md transition"
              >
                <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
                  <svg className="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
                  </svg>
                </div>
                <span className="font-medium text-gray-900">회원가입</span>
              </Link>
              <Link
                to="/login"
                className="flex items-center gap-3 p-4 bg-white rounded-xl border border-gray-200 hover:shadow-md transition"
              >
                <div className="w-10 h-10 bg-orange-100 rounded-lg flex items-center justify-center">
                  <svg className="w-5 h-5 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 16l-4-4m0 0l4-4m-4 4h14m-5 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h7a3 3 0 013 3v1" />
                  </svg>
                </div>
                <span className="font-medium text-gray-900">로그인</span>
              </Link>
            </>
          )}

          <a
            href="https://github.com"
            target="_blank"
            rel="noopener noreferrer"
            className="flex items-center gap-3 p-4 bg-white rounded-xl border border-gray-200 hover:shadow-md transition"
          >
            <div className="w-10 h-10 bg-gray-100 rounded-lg flex items-center justify-center">
              <svg className="w-5 h-5 text-gray-600" fill="currentColor" viewBox="0 0 24 24">
                <path fillRule="evenodd" d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z" clipRule="evenodd" />
              </svg>
            </div>
            <span className="font-medium text-gray-900">GitHub</span>
          </a>
        </div>
      </section>

      {/* Posts Section */}
      <section className="grid md:grid-cols-2 gap-8">
        {/* Recent Posts */}
        <div>
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-xl font-bold text-gray-900">최신 게시글</h2>
            <Link to="/board" className="text-blue-600 hover:underline text-sm font-medium">
              전체보기
            </Link>
          </div>
          {isLoading ? (
            <div className="flex justify-center py-10">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            </div>
          ) : recentPosts.length > 0 ? (
            <div className="space-y-3">
              {recentPosts.map((post) => (
                <PostCard key={post.id} post={post} compact />
              ))}
            </div>
          ) : (
            <div className="text-center py-10 bg-gray-50 rounded-xl">
              <p className="text-gray-500">아직 게시글이 없습니다.</p>
              {isAuthenticated && (
                <Link to="/post/write" className="text-blue-600 hover:underline text-sm mt-2 inline-block">
                  첫 글을 작성해보세요!
                </Link>
              )}
            </div>
          )}
        </div>

        {/* Popular Posts */}
        <div>
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-xl font-bold text-gray-900">인기 게시글</h2>
            <Link to="/board" className="text-blue-600 hover:underline text-sm font-medium">
              전체보기
            </Link>
          </div>
          {isLoading ? (
            <div className="flex justify-center py-10">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            </div>
          ) : popularPosts.length > 0 ? (
            <div className="space-y-3">
              {popularPosts.map((post, index) => (
                <Link
                  key={post.id}
                  to={`/post/${post.id}`}
                  className="flex items-center gap-4 p-4 bg-white rounded-xl border border-gray-100 hover:shadow-md transition"
                >
                  <span className="flex-shrink-0 w-8 h-8 bg-gradient-to-br from-blue-500 to-indigo-600 text-white font-bold rounded-full flex items-center justify-center text-sm">
                    {index + 1}
                  </span>
                  <div className="min-w-0 flex-1">
                    <h3 className="font-medium text-gray-900 truncate">{post.title}</h3>
                    <div className="flex items-center gap-2 text-xs text-gray-500 mt-1">
                      <span>{post.authorNickname}</span>
                      <span>·</span>
                      <span>조회 {post.viewCount}</span>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          ) : (
            <div className="text-center py-10 bg-gray-50 rounded-xl">
              <p className="text-gray-500">아직 게시글이 없습니다.</p>
            </div>
          )}
        </div>
      </section>

      {/* Stats Section */}
      <section className="bg-gray-900 text-white -mx-4 px-4 py-12 rounded-3xl">
        <h2 className="text-2xl font-bold text-center mb-10">커뮤니티 현황</h2>
        <div className="grid grid-cols-3 gap-8 max-w-3xl mx-auto text-center">
          <div>
            <div className="text-4xl font-bold text-blue-400 mb-2">
              {stats?.postCount ?? '-'}
            </div>
            <div className="text-gray-400">게시글</div>
          </div>
          <div>
            <div className="text-4xl font-bold text-green-400 mb-2">
              {stats?.userCount ?? '-'}
            </div>
            <div className="text-gray-400">회원</div>
          </div>
          <div>
            <div className="text-4xl font-bold text-purple-400 mb-2">
              {stats?.commentCount ?? '-'}
            </div>
            <div className="text-gray-400">댓글</div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      {!isAuthenticated && (
        <section className="text-center py-12 bg-gradient-to-r from-blue-600 to-indigo-600 -mx-4 px-4 rounded-3xl">
          <h2 className="text-2xl md:text-3xl font-bold text-white mb-4">
            지금 바로 시작하세요
          </h2>
          <p className="text-blue-100 mb-8 max-w-xl mx-auto">
            무료로 가입하고 커뮤니티의 일원이 되어보세요.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link
              to="/signup"
              className="inline-block px-8 py-4 bg-white text-blue-600 font-semibold rounded-xl hover:bg-gray-100 transition shadow-lg"
            >
              무료 회원가입
            </Link>
            <Link
              to="/board"
              className="inline-block px-8 py-4 bg-transparent text-white font-semibold rounded-xl hover:bg-white/10 transition border-2 border-white/50"
            >
              둘러보기
            </Link>
          </div>
        </section>
      )}
    </div>
  );
}
