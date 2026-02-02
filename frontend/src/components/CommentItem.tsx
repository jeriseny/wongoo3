import { useState } from 'react';
import type { Comment } from '../types';
import { useAuthStore } from '../stores/authStore';
import { commentApi } from '../api/client';
import { formatDate } from '../utils/formatDate';

interface Props {
  comment: Comment;
  onUpdate: () => void;
  onDelete: () => void;
}

export default function CommentItem({ comment, onUpdate, onDelete }: Props) {
  const { user } = useAuthStore();
  const [isEditing, setIsEditing] = useState(false);
  const [editContent, setEditContent] = useState(comment.content);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const isAuthor = user && user.nickName === comment.authorNickname;

  const handleUpdate = async () => {
    if (!editContent.trim()) return;
    setIsSubmitting(true);
    try {
      await commentApi.update(comment.id, { content: editContent });
      setIsEditing(false);
      onUpdate();
    } catch {
      alert('댓글 수정에 실패했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDelete = async () => {
    if (!confirm('댓글을 삭제하시겠습니까?')) return;
    try {
      await commentApi.delete(comment.id);
      onDelete();
    } catch {
      alert('댓글 삭제에 실패했습니다.');
    }
  };

  return (
    <div className="py-4 border-b border-gray-100 last:border-0">
      <div className="flex items-center justify-between mb-2">
        <div className="flex items-center gap-2">
          <span className="font-medium text-gray-900">{comment.authorNickname}</span>
          <span className="text-sm text-gray-500">{formatDate(comment.createdAt, 'datetime-short')}</span>
        </div>
        {isAuthor && !isEditing && (
          <div className="flex gap-2">
            <button
              onClick={() => setIsEditing(true)}
              className="text-sm text-gray-500 hover:text-blue-600"
            >
              수정
            </button>
            <button
              onClick={handleDelete}
              className="text-sm text-gray-500 hover:text-red-600"
            >
              삭제
            </button>
          </div>
        )}
      </div>

      {isEditing ? (
        <div className="space-y-2">
          <textarea
            value={editContent}
            onChange={(e) => setEditContent(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
            rows={3}
          />
          <div className="flex gap-2">
            <button
              onClick={handleUpdate}
              disabled={isSubmitting}
              className="px-3 py-1 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700 disabled:opacity-50"
            >
              저장
            </button>
            <button
              onClick={() => {
                setIsEditing(false);
                setEditContent(comment.content);
              }}
              className="px-3 py-1 text-gray-600 text-sm hover:text-gray-900"
            >
              취소
            </button>
          </div>
        </div>
      ) : (
        <p className="text-gray-700 whitespace-pre-wrap">{comment.content}</p>
      )}
    </div>
  );
}
