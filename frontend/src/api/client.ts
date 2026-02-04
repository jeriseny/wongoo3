import axios from 'axios';
import { tokenManager } from '../utils/tokenManager';
import type {
  LoginRequest,
  SignupRequest,
  WkToken,
  UserInfo,
  Board,
  Post,
  PostListItem,
  CreatePostRequest,
  UpdatePostRequest,
  PostSearchParams,
  Comment,
  CreateCommentRequest,
  Page,
  Stats,
  LikeResponse,
} from '../types';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor - add auth token
api.interceptors.request.use((config) => {
  const token = tokenManager.getAccessToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor - handle 401
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      const refreshToken = tokenManager.getRefreshToken();
      if (refreshToken && !error.config._retry) {
        error.config._retry = true;
        try {
          const { data } = await axios.post<WkToken>('/api/auth/reissue', {
            token: refreshToken,
            rememberMe: false,
          });
          tokenManager.setTokens(data.accessToken, data.refreshToken);
          error.config.headers.Authorization = `Bearer ${data.accessToken}`;
          return api(error.config);
        } catch {
          tokenManager.clearTokens();
          // React Router를 통한 리다이렉트를 위해 이벤트 발생
          window.dispatchEvent(new CustomEvent('auth:logout'));
        }
      }
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authApi = {
  login: (data: LoginRequest) =>
    api.post<WkToken>('/auth/login/local', data),

  signup: (data: SignupRequest) =>
    api.post<number>('/user/signup', data),

  reissue: (token: string) =>
    api.post<WkToken>('/auth/reissue', { token, rememberMe: false }),
};

// User API
export const userApi = {
  getMyInfo: () =>
    api.get<UserInfo>('/user/info'),

  updateInfo: (nickname: string, phoneNumber: string) =>
    api.patch('/user/info', { nickname, phoneNumber }),

  changePassword: (currentPassword: string, newPassword: string) =>
    api.post('/user/change-password', null, {
      params: { currentPassword, newPassword },
    }),
};

// Board API
export const boardApi = {
  getList: () =>
    api.get<Board[]>('/board'),

  getBySlug: (slug: string) =>
    api.get<Board>(`/board/${slug}`),
};

// Post API
export const postApi = {
  getList: (params: PostSearchParams = {}) =>
    api.get<Page<PostListItem>>('/post', {
      params: {
        page: params.page ?? 0,
        size: params.size ?? 10,
        boardSlug: params.boardSlug,
        searchType: params.searchType,
        keyword: params.keyword,
        sortBy: params.sortBy ?? 'LATEST',
      },
    }),

  getDetail: (postId: number) =>
    api.get<Post>(`/post/${postId}`),

  create: (data: CreatePostRequest) =>
    api.post<Post>('/post', data),

  update: (postId: number, data: UpdatePostRequest) =>
    api.patch<Post>(`/post/${postId}`, data),

  delete: (postId: number) =>
    api.delete(`/post/${postId}`),

  getLikeStatus: (postId: number) =>
    api.get<LikeResponse>(`/post/${postId}/like`),

  toggleLike: (postId: number) =>
    api.post<LikeResponse>(`/post/${postId}/like`),
};

// Comment API
export const commentApi = {
  getList: (postId: number, page = 0, size = 20) =>
    api.get<Page<Comment>>(`/post/${postId}/comment`, { params: { page, size } }),

  create: (postId: number, data: CreateCommentRequest) =>
    api.post<Comment>(`/post/${postId}/comment`, data),

  update: (commentId: number, data: CreateCommentRequest) =>
    api.patch<Comment>(`/comment/${commentId}`, data),

  delete: (commentId: number) =>
    api.delete(`/comment/${commentId}`),
};

// Stats API
export const statsApi = {
  get: () => api.get<Stats>('/stats'),
};

export default api;
