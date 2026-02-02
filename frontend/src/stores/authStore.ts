import { create } from 'zustand';
import { authApi, userApi } from '../api/client';
import type { UserInfo, LoginRequest, SignupRequest } from '../types';

interface AuthState {
  isAuthenticated: boolean;
  user: UserInfo | null;
  isLoading: boolean;

  login: (data: LoginRequest) => Promise<void>;
  signup: (data: SignupRequest) => Promise<void>;
  logout: () => void;
  fetchUser: () => Promise<void>;
  initialize: () => Promise<void>;
  setTokens: (accessToken: string, refreshToken: string) => Promise<void>;
}

export const useAuthStore = create<AuthState>((set) => ({
  isAuthenticated: false,
  user: null,
  isLoading: true,

  login: async (data: LoginRequest) => {
    const response = await authApi.login(data);
    const { accessToken, refreshToken } = response.data;
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);

    const userResponse = await userApi.getMyInfo();
    set({ isAuthenticated: true, user: userResponse.data });
  },

  signup: async (data: SignupRequest) => {
    await authApi.signup(data);
  },

  logout: () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    set({ isAuthenticated: false, user: null });
  },

  fetchUser: async () => {
    try {
      const response = await userApi.getMyInfo();
      set({ isAuthenticated: true, user: response.data });
    } catch {
      set({ isAuthenticated: false, user: null });
    }
  },

  initialize: async () => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      try {
        const response = await userApi.getMyInfo();
        set({ isAuthenticated: true, user: response.data, isLoading: false });
      } catch {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        set({ isAuthenticated: false, user: null, isLoading: false });
      }
    } else {
      set({ isLoading: false });
    }
  },

  setTokens: async (accessToken: string, refreshToken: string) => {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);

    try {
      const response = await userApi.getMyInfo();
      set({ isAuthenticated: true, user: response.data });
    } catch {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      set({ isAuthenticated: false, user: null });
      throw new Error('사용자 정보를 가져오는데 실패했습니다.');
    }
  },
}));
