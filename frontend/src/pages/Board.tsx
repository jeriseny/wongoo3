import { useEffect, useState } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { postApi, boardApi } from '../api/client';
import { useAuthStore } from '../stores/authStore';
import PostCard from '../components/PostCard';
import Pagination from '../components/Pagination';
import SearchBar from '../components/SearchBar';
import LoadingSpinner from '../components/common/LoadingSpinner';
import ErrorAlert from '../components/common/ErrorAlert';
import type { PostListItem, Page, Board, SearchType, SortType } from '../types';

export default function BoardPage() {
  const { slug } = useParams<{ slug: string }>();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();
  const [boards, setBoards] = useState<Board[]>([]);
  const [posts, setPosts] = useState<Page<PostListItem> | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  // 검색 상태
  const [searchType, setSearchType] = useState<SearchType>('TITLE');
  const [keyword, setKeyword] = useState('');
  const [sortBy, setSortBy] = useState<SortType>('LATEST');

  const currentBoard = boards.find((b) => b.slug === slug);

  useEffect(() => {
    boardApi.getList().then((res) => setBoards(res.data)).catch(() => {});
  }, []);

  const fetchPosts = async (page: number) => {
    setIsLoading(true);
    try {
      const response = await postApi.getList({
        page,
        size: 10,
        boardSlug: slug,
        searchType: keyword ? searchType : undefined,
        keyword: keyword || undefined,
        sortBy,
      });
      setPosts(response.data);
      setError('');
    } catch {
      setError('게시글을 불러오는데 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  // 게시판 변경 시 페이지 초기화
  useEffect(() => {
    setCurrentPage(0);
  }, [slug]);

  // 페이지/게시판/정렬 변경 시 게시글 로드
  useEffect(() => {
    fetchPosts(currentPage);
  }, [currentPage, slug, sortBy]);

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleSearch = () => {
    setCurrentPage(0);
    fetchPosts(0);
  };

  const handleSortChange = (newSort: SortType) => {
    setSortBy(newSort);
    setCurrentPage(0);
  };

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            {currentBoard?.name || '전체 게시판'}
          </h1>
          <p className="text-gray-600 mt-1">
            {currentBoard?.description || '모든 게시글을 확인하세요'}
          </p>
        </div>
        {isAuthenticated && (
          <Link
            to={slug ? `/post/write?board=${slug}` : '/post/write'}
            className="px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition shadow-sm"
          >
            새 글 작성
          </Link>
        )}
      </div>

      {/* Board Tabs */}
      <div className="flex gap-2 mb-6 overflow-x-auto pb-2">
        <button
          onClick={() => navigate('/board')}
          className={`px-4 py-2 rounded-lg font-medium whitespace-nowrap transition ${
            !slug
              ? 'bg-blue-600 text-white'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          }`}
        >
          전체
        </button>
        {boards.map((board) => (
          <button
            key={board.id}
            onClick={() => navigate(`/board/${board.slug}`)}
            className={`px-4 py-2 rounded-lg font-medium whitespace-nowrap transition ${
              slug === board.slug
                ? 'bg-blue-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
            }`}
          >
            {board.name}
          </button>
        ))}
      </div>

      {/* Search Bar */}
      <SearchBar
        searchType={searchType}
        keyword={keyword}
        sortBy={sortBy}
        onSearchTypeChange={setSearchType}
        onKeywordChange={setKeyword}
        onSortChange={handleSortChange}
        onSearch={handleSearch}
      />

      {/* 검색 결과 표시 */}
      {keyword && posts && (
        <div className="mb-4 text-gray-600">
          <span className="font-medium">"{keyword}"</span> 검색 결과
          <span className="ml-2">({posts.totalElements}건)</span>
        </div>
      )}

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
          <div className="text-gray-400 text-6xl mb-4">
            {keyword ? '🔍' : '📝'}
          </div>
          <h2 className="text-xl font-medium text-gray-600 mb-2">
            {keyword ? '검색 결과가 없습니다' : '아직 게시글이 없습니다'}
          </h2>
          <p className="text-gray-500">
            {keyword ? '다른 검색어로 시도해보세요' : '첫 번째 글을 작성해보세요!'}
          </p>
          {!keyword && isAuthenticated && (
            <Link
              to={slug ? `/post/write?board=${slug}` : '/post/write'}
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
