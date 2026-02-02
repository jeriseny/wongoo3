import axios from 'axios';
import type {
  LoginRequest,
  SignupRequest,
  WkToken,
  UserInfo,
  Post,
  PostListItem,
  CreatePostRequest,
  UpdatePostRequest,
  Comment,
  CreateCommentRequest,
  Page,
  Stats,
} from '../types';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor - add auth token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
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
      const refreshToken = localStorage.getItem('refreshToken');
      if (refreshToken && !error.config._retry) {
        error.config._retry = true;
        try {
          const { data } = await axios.post<WkToken>('/api/auth/reissue', {
            token: refreshToken,
            rememberMe: false,
          });
          localStorage.setItem('accessToken', data.accessToken);
          localStorage.setItem('refreshToken', data.refreshToken);
          error.config.headers.Authorization = `Bearer ${data.accessToken}`;
          return api(error.config);
        } catch {
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
          window.location.href = '/login';
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
    api.post<UserInfo>('/user/info'),

  updateInfo: (nickname: string, phoneNumber: string) =>
    api.patch('/user/info', null, { params: { nickname, phoneNumber } }),

  changePassword: (currentPassword: string, newPassword: string) =>
    api.post('/user/change-password', null, {
      params: { currentPassword, newPassword },
    }),
};

// Post API
export const postApi = {
  getList: (page = 0, size = 10) =>
    api.get<Page<PostListItem>>('/post', { params: { page, size } }),

  getDetail: (postId: number) =>
    api.get<Post>(`/post/${postId}`),

  create: (data: CreatePostRequest) =>
    api.post<Post>('/post', data),

  update: (postId: number, data: UpdatePostRequest) =>
    api.patch<Post>(`/post/${postId}`, data),

  delete: (postId: number) =>
    api.delete(`/post/${postId}`),
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
