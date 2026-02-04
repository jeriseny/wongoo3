# Frontend

React 19 + TypeScript + Tailwind + Vite

## Structure

```
src/
├── api/client.ts       # Axios, API 모듈
├── components/         # Header, Layout, PostCard, Pagination
├── pages/              # Board, PostDetail, PostWrite, Login, Signup, MyPage
├── stores/authStore.ts # Zustand 인증 상태
├── utils/              # formatDate, tokenManager
└── types/index.ts      # TypeScript 타입
```

## Routes

| Path | Component | Auth |
|------|-----------|------|
| `/`, `/board`, `/board/:slug` | Board | - |
| `/post/:id` | PostDetail | - |
| `/post/write` | PostWrite | O |
| `/post/edit/:id` | PostWrite | O |
| `/login` | Login | - |
| `/signup` | Signup | - |
| `/mypage` | MyPage | O |

## API

```typescript
postApi.getList({ boardSlug, page, size })
postApi.create({ title, content, boardSlug })
postApi.toggleLike(postId)
authApi.login({ email, password })
userApi.getMyInfo()
```

## Commands

```bash
npm run dev      # 개발 서버 :5173
npm run build    # 빌드
npm run lint     # ESLint
```
