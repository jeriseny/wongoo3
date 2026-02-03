import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { postApi } from '../api/client';
import LoadingSpinner from '../components/common/LoadingSpinner';
import { getErrorMessage } from '../utils/errorMessage';

export default function PostWrite() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEdit = Boolean(id);

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isFetching, setIsFetching] = useState(isEdit);

  useEffect(() => {
    if (isEdit && id) {
      setIsFetching(true);
      postApi
        .getDetail(Number(id))
        .then((response) => {
          setTitle(response.data.title);
          setContent(response.data.content);
        })
        .catch((err) => {
          alert(getErrorMessage(err, '게시글을 찾을 수 없습니다.'));
          navigate('/');
        })
        .finally(() => setIsFetching(false));
    }
  }, [id, isEdit, navigate]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!title.trim() || !content.trim()) {
      alert('제목과 내용을 모두 입력해주세요.');
      return;
    }

    setIsLoading(true);

    try {
      if (isEdit && id) {
        await postApi.update(Number(id), { title, content });
        alert('게시글이 수정되었습니다.');
        navigate(`/post/${id}`);
      } else {
        const response = await postApi.create({ title, content });
        alert('게시글이 작성되었습니다.');
        navigate(`/post/${response.data.id}`);
      }
    } catch (err) {
      alert(getErrorMessage(err, isEdit ? '수정에 실패했습니다.' : '작성에 실패했습니다.'));
    } finally {
      setIsLoading(false);
    }
  };

  if (isFetching) {
    return <LoadingSpinner />;
  }

  return (
    <div className="max-w-3xl mx-auto">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">
        {isEdit ? '게시글 수정' : '새 글 작성'}
      </h1>

      <form onSubmit={handleSubmit} className="bg-white rounded-2xl shadow-sm border border-gray-200 p-8">
        <div className="mb-6">
          <label htmlFor="title" className="block text-sm font-medium text-gray-700 mb-2">
            제목
          </label>
          <input
            type="text"
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            maxLength={200}
            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="제목을 입력하세요"
          />
        </div>

        <div className="mb-6">
          <label htmlFor="content" className="block text-sm font-medium text-gray-700 mb-2">
            내용
          </label>
          <textarea
            id="content"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            rows={15}
            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
            placeholder="내용을 입력하세요"
          />
        </div>

        <div className="flex items-center justify-end gap-4">
          <button
            type="button"
            onClick={() => navigate(-1)}
            className="px-6 py-3 text-gray-600 font-medium rounded-lg hover:bg-gray-100 transition"
          >
            취소
          </button>
          <button
            type="submit"
            disabled={isLoading}
            className="px-6 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition"
          >
            {isLoading ? '저장 중...' : isEdit ? '수정하기' : '작성하기'}
          </button>
        </div>
      </form>
    </div>
  );
}
