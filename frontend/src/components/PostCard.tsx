import { Link } from 'react-router-dom';
import { formatDate } from '../utils/formatDate';
import type { PostListItem } from '../types';

interface Props {
  post: PostListItem;
  compact?: boolean;
}

export default function PostCard({ post, compact = false }: Props) {

  if (compact) {
    return (
      <Link
        to={`/post/${post.id}`}
        className="block bg-white rounded-lg border border-gray-100 p-4 hover:shadow-md transition"
      >
        <h3 className="font-medium text-gray-900 truncate mb-1">{post.title}</h3>
        <div className="flex items-center gap-2 text-xs text-gray-500">
          <span>{post.authorNickname}</span>
          <span>·</span>
          <span>{formatDate(post.createdAt)}</span>
        </div>
      </Link>
    );
  }

  return (
    <Link
      to={`/post/${post.id}`}
      className="block bg-white rounded-xl shadow-sm border border-gray-200 p-6 hover:shadow-md hover:border-blue-300 transition"
    >
      <h2 className="text-xl font-semibold text-gray-900 mb-2 line-clamp-1">
        {post.title}
      </h2>
      <div className="flex items-center gap-4 text-sm text-gray-500">
        <span className="font-medium text-gray-700">{post.authorNickname}</span>
        <span>·</span>
        <span>{formatDate(post.createdAt)}</span>
        <span>·</span>
        <span>조회 {post.viewCount}</span>
      </div>
    </Link>
  );
}
