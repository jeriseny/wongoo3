import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { userApi } from '../api/client';
import { formatDate } from '../utils/formatDate';

export default function MyPage() {
  const navigate = useNavigate();
  const { user, logout, fetchUser } = useAuthStore();

  const [isEditingInfo, setIsEditingInfo] = useState(false);
  const [isChangingPassword, setIsChangingPassword] = useState(false);

  const [nickname, setNickname] = useState(user?.nickName || '');
  const [phoneNumber, setPhoneNumber] = useState(user?.phoneNumber || '');

  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [newPasswordConfirm, setNewPasswordConfirm] = useState('');

  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleUpdateInfo = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      await userApi.updateInfo(nickname, phoneNumber);
      await fetchUser();
      setIsEditingInfo(false);
      alert('정보가 수정되었습니다.');
    } catch {
      alert('정보 수정에 실패했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleChangePassword = async (e: React.FormEvent) => {
    e.preventDefault();

    if (newPassword !== newPasswordConfirm) {
      alert('새 비밀번호가 일치하지 않습니다.');
      return;
    }

    setIsSubmitting(true);

    try {
      await userApi.changePassword(currentPassword, newPassword);
      setIsChangingPassword(false);
      setCurrentPassword('');
      setNewPassword('');
      setNewPasswordConfirm('');
      alert('비밀번호가 변경되었습니다.');
    } catch {
      alert('비밀번호 변경에 실패했습니다. 현재 비밀번호를 확인해주세요.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  if (!user) return null;

  return (
    <div className="max-w-2xl mx-auto">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">마이페이지</h1>

      {/* Profile Info */}
      <section className="bg-white rounded-2xl shadow-sm border border-gray-200 p-8 mb-6">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-xl font-bold text-gray-900">내 정보</h2>
          {!isEditingInfo && (
            <button
              onClick={() => setIsEditingInfo(true)}
              className="text-blue-600 hover:underline text-sm"
            >
              수정
            </button>
          )}
        </div>

        {isEditingInfo ? (
          <form onSubmit={handleUpdateInfo} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                닉네임
              </label>
              <input
                type="text"
                value={nickname}
                onChange={(e) => setNickname(e.target.value)}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                전화번호
              </label>
              <input
                type="tel"
                value={phoneNumber}
                onChange={(e) => setPhoneNumber(e.target.value)}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div className="flex gap-3">
              <button
                type="submit"
                disabled={isSubmitting}
                className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50"
              >
                저장
              </button>
              <button
                type="button"
                onClick={() => {
                  setIsEditingInfo(false);
                  setNickname(user.nickName);
                  setPhoneNumber(user.phoneNumber);
                }}
                className="px-4 py-2 text-gray-600 hover:text-gray-900"
              >
                취소
              </button>
            </div>
          </form>
        ) : (
          <div className="space-y-4">
            <div className="flex border-b border-gray-100 pb-3">
              <span className="w-28 text-gray-500">이메일</span>
              <span className="text-gray-900">{user.email}</span>
            </div>
            <div className="flex border-b border-gray-100 pb-3">
              <span className="w-28 text-gray-500">닉네임</span>
              <span className="text-gray-900">{user.nickName}</span>
            </div>
            <div className="flex border-b border-gray-100 pb-3">
              <span className="w-28 text-gray-500">전화번호</span>
              <span className="text-gray-900">{user.phoneNumber}</span>
            </div>
            <div className="flex border-b border-gray-100 pb-3">
              <span className="w-28 text-gray-500">가입 유형</span>
              <span className="text-gray-900">
                {user.isSocial ? `소셜 (${user.providerType})` : '일반'}
              </span>
            </div>
            <div className="flex">
              <span className="w-28 text-gray-500">가입일</span>
              <span className="text-gray-900">{formatDate(user.createdAt)}</span>
            </div>
          </div>
        )}
      </section>

      {/* Change Password (Only for local users) */}
      {!user.isSocial && (
        <section className="bg-white rounded-2xl shadow-sm border border-gray-200 p-8 mb-6">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-xl font-bold text-gray-900">비밀번호 변경</h2>
            {!isChangingPassword && (
              <button
                onClick={() => setIsChangingPassword(true)}
                className="text-blue-600 hover:underline text-sm"
              >
                변경
              </button>
            )}
          </div>

          {isChangingPassword ? (
            <form onSubmit={handleChangePassword} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  현재 비밀번호
                </label>
                <input
                  type="password"
                  value={currentPassword}
                  onChange={(e) => setCurrentPassword(e.target.value)}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  새 비밀번호
                </label>
                <input
                  type="password"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  새 비밀번호 확인
                </label>
                <input
                  type="password"
                  value={newPasswordConfirm}
                  onChange={(e) => setNewPasswordConfirm(e.target.value)}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div className="flex gap-3">
                <button
                  type="submit"
                  disabled={isSubmitting}
                  className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50"
                >
                  변경하기
                </button>
                <button
                  type="button"
                  onClick={() => {
                    setIsChangingPassword(false);
                    setCurrentPassword('');
                    setNewPassword('');
                    setNewPasswordConfirm('');
                  }}
                  className="px-4 py-2 text-gray-600 hover:text-gray-900"
                >
                  취소
                </button>
              </div>
            </form>
          ) : (
            <p className="text-gray-500">비밀번호를 변경하려면 변경 버튼을 클릭하세요.</p>
          )}
        </section>
      )}

      {/* Logout */}
      <section className="bg-white rounded-2xl shadow-sm border border-gray-200 p-8">
        <h2 className="text-xl font-bold text-gray-900 mb-4">로그아웃</h2>
        <p className="text-gray-500 mb-4">현재 기기에서 로그아웃합니다.</p>
        <button
          onClick={handleLogout}
          className="px-6 py-3 bg-red-600 text-white font-medium rounded-lg hover:bg-red-700 transition"
        >
          로그아웃
        </button>
      </section>
    </div>
  );
}
