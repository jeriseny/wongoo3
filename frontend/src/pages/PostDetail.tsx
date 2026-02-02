import { useEffect, useState, useCallback } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { postApi, commentApi } from '../api/client';
import { useAuthStore } from '../stores/authStore';
import CommentItem from '../components/CommentItem';
import Pagination from '../components/Pagination';
import LoadingSpinner from '../components/common/LoadingSpinner';
import { formatDateTime } from '../utils/formatDate';
import type { Post, Comment, Page } from '../types';

export default function PostDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuthStore();

  const [post, setPost] = useState<Post | null>(null);
  const [comments, setComments] = useState<Page<Comment> | null>(null);
  const [commentPage, setCommentPage] = useState(0);
  const [newComment, setNewComment] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const postId = Number(id);
  const isAuthor = user && post && user.nickName === post.authorNickname;

  const fetchPost = useCallback(async () => {
    try {
      const response = await postApi.getDetail(postId);
      setPost(response.data);
    } catch {
      alert('게시글을 찾을 수 없습니다.');
      navigate('/');
    }
  }, [postId, navigate]);

  const fetchComments = useCallback(async (page: number) => {
    try {
      const response = await commentApi.getList(postId, page, 20);
      setComments(response.data);
    } catch {
      console.error('Failed to fetch comments');
    }
  }, [postId]);

  useEffect(() => {
    setIsLoading(true);
    Promise.all([fetchPost(), fetchComments(0)]).finally(() => setIsLoading(false));
  }, [fetchPost, fetchComments]);

  const handleDelete = async () => {
    if (!confirm('게시글을 삭제하시겠습니까?')) return;
    try {
      await postApi.delete(postId);
      alert('게시글이 삭제되었습니다.');
      navigate('/');
    } catch {
      alert('삭제에 실패했습니다.');
    }
  };

  const handleCommentSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newComment.trim()) return;

    setIsSubmitting(true);
    try {
      await commentApi.create(postId, { content: newComment });
      setNewComment('');
      fetchComments(0);
      setCommentPage(0);
    } catch {
      alert('댓글 작성에 실패했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  if (isLoading) {
    return <LoadingSpinner />;
  }

  if (!post) return null;

  return (
    <div className="max-w-3xl mx-auto">
      {/* Post */}
      <article className="bg-white rounded-2xl shadow-sm border border-gray-200 p-8 mb-8">
        <header className="mb-6">
          <h1 className="text-3xl font-bold text-gray-900 mb-4">{post.title}</h1>
          <div className="flex items-center justify-between text-sm text-gray-500">
            <div className="flex items-center gap-4">
              <span className="font-medium text-gray-700">{post.authorNickname}</span>
              <span>{formatDateTime(post.createdAt)}</span>
              <span>조회 {post.viewCount}</span>
            </div>
            {isAuthor && (
              <div className="flex gap-3">
                <Link
                  to={`/post/edit/${post.id}`}
                  className="text-blue-600 hover:underline"
                >
                  수정
                </Link>
                <button
                  onClick={handleDelete}
                  className="text-red-600 hover:underline"
                >
                  삭제
                </button>
              </div>
            )}
          </div>
        </header>

        <div className="prose max-w-none">
          <p className="text-gray-700 whitespace-pre-wrap leading-relaxed">
            {post.content}
          </p>
        </div>
      </article>

      {/* Comments */}
      <section className="bg-white rounded-2xl shadow-sm border border-gray-200 p-8">
        <h2 className="text-xl font-bold text-gray-900 mb-6">
          댓글 {comments?.totalElements || 0}
        </h2>

        {isAuthenticated && (
          <form onSubmit={handleCommentSubmit} className="mb-8">
            <textarea
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
              placeholder="댓글을 작성해주세요"
              rows={3}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
            />
            <div className="mt-3 flex justify-end">
              <button
                type="submit"
                disabled={isSubmitting || !newComment.trim()}
                className="px-6 py-2 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition"
              >
                {isSubmitting ? '작성 중...' : '댓글 작성'}
              </button>
            </div>
          </form>
        )}

        {comments && comments.content.length > 0 ? (
          <>
            <div className="divide-y divide-gray-100">
              {comments.content.map((comment) => (
                <CommentItem
                  key={comment.id}
                  comment={comment}
                  onUpdate={() => fetchComments(commentPage)}
                  onDelete={() => fetchComments(commentPage)}
                />
              ))}
            </div>

            <Pagination
              currentPage={comments.number}
              totalPages={comments.totalPages}
              onPageChange={(page) => {
                setCommentPage(page);
                fetchComments(page);
              }}
            />
          </>
        ) : (
          <p className="text-center text-gray-500 py-8">
            아직 댓글이 없습니다. 첫 번째 댓글을 작성해보세요!
          </p>
        )}
      </section>

      <div className="mt-6">
        <Link
          to="/"
          className="inline-flex items-center text-gray-600 hover:text-gray-900"
        >
          ← 목록으로 돌아가기
        </Link>
      </div>
    </div>
  );
}
