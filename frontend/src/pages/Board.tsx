import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { postApi } from '../api/client';
import { useAuthStore } from '../stores/authStore';
import PostCard from '../components/PostCard';
import Pagination from '../components/Pagination';
import LoadingSpinner from '../components/common/LoadingSpinner';
import ErrorAlert from '../components/common/ErrorAlert';
import type { PostListItem, Page } from '../types';

export default function Home() {
  const { isAuthenticated } = useAuthStore();
  const [posts, setPosts] = useState<Page<PostListItem> | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchPosts = async (page: number) => {
    setIsLoading(true);
    try {
      const response = await postApi.getList(page, 10);
      setPosts(response.data);
      setError('');
    } catch {
      setError('게시글을 불러오는데 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchPosts(currentPage);
  }, [currentPage]);

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <div>
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">게시판</h1>
          <p className="text-gray-600 mt-1">자유롭게 글을 작성하고 소통해보세요</p>
        </div>
        {isAuthenticated && (
          <Link
            to="/post/write"
            className="px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition shadow-sm"
          >
            새 글 작성
          </Link>
        )}
      </div>

      <ErrorAlert message={error} className="mb-6" />

      {isLoading ? (
        <LoadingSpinner />
      ) : posts && posts.content.length > 0 ? (
        <>
          <div className="space-y-4">
            {posts.content.map((post) => (
              <PostCard key={post.id} post={post} />
            ))}
          </div>

          <Pagination
            currentPage={posts.number}
            totalPages={posts.totalPages}
            onPageChange={handlePageChange}
          />
        </>
      ) : (
        <div className="text-center py-20">
          <div className="text-gray-400 text-6xl mb-4">📝</div>
          <h2 className="text-xl font-medium text-gray-600 mb-2">
            아직 게시글이 없습니다
          </h2>
          <p className="text-gray-500">첫 번째 글을 작성해보세요!</p>
          {isAuthenticated && (
            <Link
              to="/post/write"
              className="inline-block mt-4 px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition"
            >
              글 작성하기
            </Link>
          )}
        </div>
      )}
    </div>
  );
}
