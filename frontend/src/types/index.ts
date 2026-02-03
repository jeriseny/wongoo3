// Auth Types
export interface LoginRequest {
  email: string;
  password: string;
  rememberMe?: boolean;
}

export interface SignupRequest {
  email: string;
  nickname: string;
  phoneNumber: string;
  password: string;
  termsAgreeList: TermsAgree[];
}

export interface TermsAgree {
  termsType: string;
  agreed: boolean;
}

export interface WkToken {
  accessToken: string;
  refreshToken: string;
  accessTokenExpiresIn: number;
  refreshTokenExpiresIn: number;
}

export interface UserInfo {
  email: string;
  nickName: string;
  phoneNumber: string;
  profileImageUrl: string | null;
  role: string;
  isSocial: boolean;
  providerType: string | null;
  lastLoginAt: string;
  createdAt: string;
}

// Board Types
export interface Board {
  id: number;
  name: string;
  slug: string;
  description: string;
}

// Post Types
export interface Post {
  id: number;
  title: string;
  content: string;
  authorId: number;
  authorNickname: string;
  viewCount: number;
  boardId: number;
  boardName: string;
  boardSlug: string;
  createdAt: string;
  updatedAt: string;
}

export interface PostListItem {
  id: number;
  title: string;
  authorNickname: string;
  viewCount: number;
  boardSlug: string;
  boardName: string;
  createdAt: string;
}

export interface CreatePostRequest {
  title: string;
  content: string;
  boardSlug: string;
}

export interface UpdatePostRequest {
  title: string;
  content: string;
}

// Comment Types
export interface Comment {
  id: number;
  content: string;
  authorId: number;
  authorNickname: string;
  postId: number;
  createdAt: string;
  updatedAt: string;
}

export interface CreateCommentRequest {
  content: string;
}

// Page Types
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  empty: boolean;
}

// Stats Types
export interface Stats {
  postCount: number;
  userCount: number;
  commentCount: number;
}
